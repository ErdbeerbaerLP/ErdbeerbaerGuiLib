package de.erdbeerbaerlp.guilib.components;

import de.erdbeerbaerlp.guilib.McMod;
import de.erdbeerbaerlp.guilib.util.ImageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class Image extends GuiComponent {

    private final DynamicTexture loadingTexture = new DynamicTexture(32, 32, true);
    private final UUID imageUUID = UUID.randomUUID();
    private final DynamicTexture image;
    private GifThread gif;
    private ResourceLocation resLoc;
    private Runnable callback;
    private String errorTooltip = "";
    /**
     * Used to know when to switch between loading circle and image (when loading from URL)
     */
    private boolean imgLoaded = false;
    private GifThread loadingGif;
    private String imageURL;

    private boolean keepAspectRatio = true;
    private boolean acceptsNewImage = true;
    private boolean doGifLoop = true;
    private boolean shouldKeepSize = true;

    /**
     * Creates an image from an URL
     *
     * @param x        X position
     * @param y        Y position
     * @param width    Image width (might be resized!)
     * @param height   Image hight (might be resized!)
     * @param imageURL URL to image
     */
    public Image(int x, int y, int width, int height, String imageURL) {
        super(x, y, width, height);
        image = new DynamicTexture(getWidth(), getHeight(), true);
        setImageURL(imageURL);
    }

    /**
     * Creates an image from an URL
     *
     * @param x        X position
     * @param y        Y position
     * @param width    Image width (might be resized!)
     * @param height   Image hight (might be resized!)
     * @param imageURL URL to image
     */
    public Image(int x, int y, int width, int height, URL imageURL) {
        this(x, y, width, height, imageURL.toString());
    }

    /**
     * Creates an image from an URL
     *
     * @param x        X position
     * @param y        Y position
     * @param width    Image width (might be resized!)
     * @param height   Image hight (might be resized!)
     * @param imageURL URL to image
     */
    public Image(int x, int y, int width, int height, String imageURL, boolean doGifLoop) {
        this(x, y, width, height, imageURL);
        this.doGifLoop = doGifLoop;
    }

    /**
     * Creates an image from an URL
     *
     * @param x        X position
     * @param y        Y position
     * @param width    Image width (might be resized!)
     * @param height   Image hight (might be resized!)
     * @param imageURL URL to image
     */
    public Image(int x, int y, int width, int height, URL imageURL, boolean doGifLoop) {
        this(x, y, width, height, imageURL.toString(), doGifLoop);
    }

    /**
     * Creates an image from an File
     *
     * @param x         X position
     * @param y         Y position
     * @param width     Image width (might be resized!)
     * @param height    Image hight (might be resized!)
     * @param imageFile File to the image
     */
    public Image(int x, int y, int width, int height, File imageFile) {
        super(x, y, width, height);
        imgLoaded = true;
        NativeImage img;
        try {
            final FileInputStream is = new FileInputStream(imageFile);
            img = NativeImage.read(is);
            is.close();
        } catch (IOException e) {
            errorTooltip = (e.getCause() == null) ? e.getClass().getCanonicalName() + ": " + e.getMessage() : e.getCause().getLocalizedMessage();
            image = null;
            resLoc = null;
            return;
        }
        image = new DynamicTexture(img);
        resLoc = mc.getTextureManager().getDynamicTextureLocation("image_" + imageUUID.toString().toLowerCase(), image);
    }

    /**
     * Creates an image from an ResourceLocation
     *
     * @param x                X position
     * @param y                Y position
     * @param width            Image width (might be resized!)
     * @param height           Image hight (might be resized!)
     * @param resourceLocation ResourceLocation of the image
     */
    public Image(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        super(x, y, width, height);
        image = null;
        resLoc = resourceLocation;
        imgLoaded = true;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
        this.reloadImage();
    }

    private void reloadImage() {
        setImageURL(imageURL);
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
            return ArrayUtils.addAll(super.getTooltips(), "", "\u00A7cError loading image:", "\u00A7c" + errorTooltip);
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

    /**
     * @return true, if this Image allows changing URL
     */
    public boolean setImageURL(String url) {
        if (!acceptsNewImage) return false;
        if (gif != null && gif.isAlive()) gif.interrupt();
        try {
            do {
                image.setTextureData(new NativeImage(getWidth(), getHeight(), true));
            } while (image.getTextureData().getBytes().length == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imgLoaded = false;
        errorTooltip = "";
        imageURL = url;
        acceptsNewImage = false;
        final Thread t = new Thread(() -> {
            System.out.println("Starting to load " + url);
            try {
                if (ImageUtil.isURLGif(url)) {
                    System.out.println("URL is gif");
                    final ByteArrayInputStream is = ImageUtil.getInputStreamFromImageURL(url);
                    if (!shouldKeepSize) {
                        final BufferedImage img = ImageIO.read(is);
                        this.setWidth(img.getWidth());
                        this.setHeight(img.getHeight());
                    }
                    this.gif = new GifThread(is, image, keepAspectRatio, doGifLoop);
                    gif.start();
                    is.close();
                } else
                    try {
                        final ByteArrayInputStream is = ImageUtil.getInputStreamFromImageURL(url);
                        if (!shouldKeepSize) {
                            final BufferedImage img = ImageIO.read(is);
                            this.setWidth(img.getWidth());
                            this.setHeight(img.getHeight());
                        }
                        is.close();
                        do {
                            image.setTextureData(ImageUtil.loadImageFromURL(url, keepAspectRatio, getWidth(), getHeight()));
                        } while (image.getTextureData().getBytes().length == 0);
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
            resLoc = mc.getTextureManager().getDynamicTextureLocation("image_" + imageUUID.toString().toLowerCase(), image);
            acceptsNewImage = true;
            imgLoaded = true;
        });
        t.setDaemon(true);
        t.setName("Image download " + imageUUID.toString());
        t.start();
        return true;
    }

    public boolean setImageURL(URL url) {
        return setImageURL(url.toString());
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        if (image != null) image.updateDynamicTexture();
        if (!errorTooltip.isEmpty()) {
            final int c = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 30).getRGB();
            GuiUtils.drawGradientRect(getBlitOffset(), this.getX(), this.getY(), this.getX() + this.width, this.getY() + height, c, c);
            mc.getTextureManager().bindTexture(errorIcon);
            blit(getX() + getWidth() / 2 - 8, getY() + getHeight() / 2 - 8, 0, 0, 16, 16, 16, 16);
            return;
        }
        if (imgLoaded) {
            if (loadingGif != null && loadingGif.isAlive()) loadingGif.interrupt();
            if (resLoc == null) System.err.println("RESLOC == NULL!");
            mc.getTextureManager().bindTexture(resLoc);
            blit(getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        } else {
            if (loadingGif == null || !loadingGif.isAlive()) {
                try {
                    loadingGif = new GifThread(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(McMod.MODID, "textures/gui/loading.gif")).getInputStream(), loadingTexture, keepAspectRatio, doGifLoop);
                } catch (IOException ignored) {
                }
            }
            if (!loadingGif.isAlive()) loadingGif.start();
            final int c = new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 40).getRGB();
            GuiUtils.drawGradientRect(getBlitOffset(), this.getX(), this.getY(), this.getX() + this.width, this.getY() + height, c, c);
            loadingTexture.updateDynamicTexture();
            mc.getTextureManager().bindTexture(mc.getTextureManager().getDynamicTextureLocation("loading-gif_" + imageUUID.toString().toLowerCase(), loadingTexture));
            blit(getX() + getWidth() / 2 - 16, getY() + getHeight() / 2 - 16, 0, 0, 32, 32, 32, 32);
        }
    }

    @Override
    public final void mouseClick(double mouseX, double mouseY, int mouseButton) {
        onClick();
    }

    @Override
    public void mouseRelease(double mouseX, double mouseY, int state) {

    }

    @Override
    public void unload() {
        System.out.println("Closing...");
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
