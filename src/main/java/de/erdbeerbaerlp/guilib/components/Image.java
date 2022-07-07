package de.erdbeerbaerlp.guilib.components;

import com.icafe4j.image.ImageIO;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.erdbeerbaerlp.guilib.McMod;
import de.erdbeerbaerlp.guilib.util.ImageUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.gui.GuiUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

public class Image extends GuiComponent {

    private final DynamicTexture loadingTexture = new DynamicTexture(32, 32, true);
    private final UUID imageUUID = UUID.randomUUID();
    private DynamicTexture image;
    private GifThread gif;
    private ResourceLocation resLoc;
    private Runnable callback;
    private String errorTooltip = "";
    /**
     * Used to know when to switch between loading circle and image (when loading from URL)
     */
    private boolean imgLoaded = true;
    private GifThread loadingGif;
    private String imageURL = null;

    private boolean keepAspectRatio = true;
    private boolean acceptsNewImage = true;
    private boolean doGifLoop = true;
    private boolean shouldKeepSize = true;
    private boolean resizingImage = true;

    /**
     * Creates an empty image
     *
     * @param x         X position
     * @param y         Y position
     * @param width     Image width (might be resized!)
     * @param height    Image hight (might be resized!)
     * @param doGifLoop Should GIFs loop in this Image?
     */
    public Image(int x, int y, int width, int height, boolean doGifLoop) {
        this(x, y, width, height);
        this.doGifLoop = doGifLoop;
    }

    /**
     * Creates an empty image without width and height. That will be set when setting an image
     *
     * @param x         X position
     * @param y         Y position
     * @param doGifLoop Should GIFs loop in this Image?
     */
    public Image(int x, int y, boolean doGifLoop) {
        this(x, y);
        this.doGifLoop = doGifLoop;
    }

    /**
     * Creates an empty image
     *
     * @param x      X position
     * @param y      Y position
     * @param width  Image width (might be resized!)
     * @param height Image hight (might be resized!)
     */
    public Image(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Creates an empty image without width and height. That will be set when setting an image
     *
     * @param x X position
     * @param y Y position
     */
    public Image(int x, int y) {
        super(x, y, 0, 0);
        shouldKeepSize = false;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
        this.reloadImage();
    }

    /**
     * Reloads image if it was loaded from URL
     */
    private void reloadImage() {
        if (imageURL != null) loadImage(imageURL);
    }

    /**
     * @param shouldKeepSize Defines if the width and height of this image component will be changed on image load
     */
    public void setShouldKeepSize(boolean shouldKeepSize) {
        this.shouldKeepSize = shouldKeepSize;
    }

    @Override
    public String[] getTooltips() {
        if (!errorTooltip.isEmpty()) {
            return ArrayUtils.addAll(super.getTooltips(), "", ChatFormatting.RED + "Error loading image:",
                    ChatFormatting.RED + errorTooltip);
        }
        return super.getTooltips();
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setGifNotLooping() {
        doGifLoop = false;
    }

    public void setGifLooping() {
        doGifLoop = true;
    }

    public void loadImage(final File file) {
        try {
            final FileInputStream is = new FileInputStream(file);
            loadImage(is);
        } catch (IOException e) {
            errorTooltip = (e.getCause() == null) ? e.getClass().getCanonicalName() + ": " + e.getMessage() : e.getCause().getLocalizedMessage();
            image = null;
            resLoc = null;
            acceptsNewImage = true;
        }
    }

    public void loadImage(final ResourceLocation res) {
        if (!acceptsNewImage) return;
        final Optional<Resource> resource = mc.getResourceManager().getResource(res);
        if (resource.isEmpty()) return;
        try {
            loadImage(resource.get().open());
        } catch (IOException e) {
            errorTooltip = (e.getCause() == null) ? e.getClass().getCanonicalName() + ": " + e.getMessage() : e.getCause().getLocalizedMessage();
            image = null;
            resLoc = null;
            acceptsNewImage = true;
        }
    }

    public void loadImage(String url) {
        if (!acceptsNewImage) return;
        this.imageURL = url;
        imgLoaded = false;
        errorTooltip = "";
        acceptsNewImage = false;
        System.out.println("Starting to load " + url);
        final Thread t = new Thread(() -> {
            final ByteArrayInputStream is;
            try {
                is = ImageUtil.getInputStreamFromImageURL(url);
                acceptsNewImage = true;
                loadImage(is);
            } catch (IOException e) {
                e.printStackTrace();
                errorTooltip = (e.getCause() == null) ? e.getClass().getCanonicalName() + ": " + e.getMessage() : e.getCause().getLocalizedMessage();
                image = null;
                resLoc = null;
                acceptsNewImage = true;
            }
        });
        t.setDaemon(true);
        t.setName("Image download " + imageUUID.toString());
        t.start();

    }

    public boolean isResizingImage() {
        return resizingImage;
    }

    /**
     * Allows you to set if the visible image will be resized if it is smaller<br>
     * Does NOT affect larger images!!
     */
    public void setResizingImage(boolean resizingImage) {
        this.resizingImage = resizingImage;
    }

    /**
     * Note: Closes InputStream when finished!
     */
    public void loadImage(final InputStream inp) {
        if (gif != null && gif.isAlive()) gif.interrupt();
        mc.execute(() -> {
            try {
                final ByteArrayInputStream is = ImageUtil.convertToByteArrayIS(inp); //To always allow reset()
                if (!shouldKeepSize || getWidth() == 0 || getComponentHeight() == 0) {
                    final BufferedImage img = ImageIO.read(is);
                    this.setWidth(img.getWidth());
                    this.setHeight(img.getHeight());
                }
                is.reset();
                do {
                    image = new DynamicTexture(getWidth(), getComponentHeight(), true);
                } while (image.getPixels().asByteArray().length == 0);
                mc.execute(image::upload);
                imgLoaded = false;
                errorTooltip = "";
                acceptsNewImage = false;
                final Thread t = new Thread(() -> {
                    try {
                        if (ImageUtil.isISGif(is)) {
                            this.gif = new GifThread(is, image, keepAspectRatio, doGifLoop, resizingImage);
                            gif.start();
                        } else
                            try {
                                do {
                                    image.setPixels(ImageUtil.getImageFromIS(is, keepAspectRatio, getWidth(), getComponentHeight(), resizingImage));
                                } while (image.getPixels().asByteArray().length == 0);
                                mc.execute(image::upload);
                                is.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                errorTooltip = (e.getCause() == null) ? e.getClass().getCanonicalName() + ": " + e.getMessage() : e.getCause().getLocalizedMessage();
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorTooltip = (e.getCause() == null) ? e.getClass().getCanonicalName() + ": " + e.getMessage() : e.getCause().getLocalizedMessage();
                        resLoc = null;
                        acceptsNewImage = true;
                        return;
                    }
                    resLoc = mc.getTextureManager().register("image_" + imageUUID.toString().toLowerCase(), image);
                    acceptsNewImage = true;
                    imgLoaded = true;
                });
                t.setDaemon(true);
                t.setName("Image loader " + imageUUID.toString());
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadImage(URL url) {
        loadImage(url.toString());
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partial) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        if (!errorTooltip.isEmpty()) {
            final int c = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 30).getRGB();
            GuiUtils.drawGradientRect(poseStack.last().pose(), getBlitOffset(), this.getX(), this.getY(), this.getX() + this.width, this.getY() + height, c, c);
            RenderSystem.setShaderTexture(0, errorIcon);
            blit(poseStack, getX() + getWidth() / 2 - 8, getY() + getComponentHeight() / 2 - 8, 0, 0, 16, 16, 16, 16);
            return;
        }
        if (imgLoaded) {
            if (image == null) {
                final int c = new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 40).getRGB();
                GuiUtils.drawGradientRect(poseStack.last().pose(), getBlitOffset(), this.getX(), this.getY(), this.getX() + this.width, this.getY() + height, c, c);
                return;
            }
            if (loadingGif != null && loadingGif.isAlive()) loadingGif.interrupt();
            RenderSystem.setShaderTexture(0, resLoc);
            blit(poseStack, getX(), getY(), 0, 0, getWidth(), getComponentHeight(), getWidth(), getComponentHeight());
        } else {
            if (loadingGif == null || !loadingGif.isAlive()) {
                try {
                    loadingGif = new GifThread(ImageUtil.convertToByteArrayIS(mc.getResourceManager().getResource(new ResourceLocation(McMod.MODID, "textures/gui/loading.gif")).get().open()), loadingTexture, keepAspectRatio, doGifLoop, resizingImage);
                } catch (IOException ignored) {
                }
            }
            if (!loadingGif.isAlive()) loadingGif.start();
            final int c = new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 40).getRGB();
            GuiUtils.drawGradientRect(poseStack.last().pose(), getBlitOffset(), this.getX(), this.getY(), this.getX() + this.width, this.getY() + height, c, c);
            RenderSystem.setShaderTexture(0, mc.getTextureManager().register("loading-gif_" + imageUUID.toString().toLowerCase(), loadingTexture));
            blit(poseStack, getX() + getWidth() / 2 - 16, getY() + getComponentHeight() / 2 - 16, 0, 0, 32, 32, 32, 32);
        }
    }

    @Override
    public final void mouseClick(double mouseX, double mouseY, int mouseButton) {
        if (isEnabled()) {
            onClick();
        }
    }

    @Override
    public void mouseRelease(double mouseX, double mouseY, int state) {

    }

    @Override
    public void unload() {
        if (gif != null && gif.isAlive()) gif.interrupt();
        if (loadingGif != null && loadingGif.isAlive()) loadingGif.interrupt();
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        return false;
    }

    /**
     * Called when clicking the image
     */
    public void onClick() {
        if (this.callback != null) callback.run();
    }

    /**
     * Sets the callback being run when clicking on the image
     *
     * @param callback Runnable to run
     */
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

}
