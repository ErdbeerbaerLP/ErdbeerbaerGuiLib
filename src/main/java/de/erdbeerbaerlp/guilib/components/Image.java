package de.erdbeerbaerlp.guilib.components;

import de.erdbeerbaerlp.guilib.McMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Image extends GuiComponent {
    private final DynamicTexture image;
    private final ResourceLocation resLoc;
    private static final ResourceLocation errorLoading = new ResourceLocation(McMod.MODID, "textures/gui/imgerror.png");
    private Runnable callback;

    public Image(int x, int y, int width, int height, String imageURL, boolean resizeIfNeeded) {
        super(x, y, width, height);
        BufferedImage img;
        try {
            img = ImageIO.read(new URL(imageURL));
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
            resLoc = null;
            return;
        }
        if (resizeIfNeeded)
            if (img.getWidth() > width || img.getHeight() > height) {
                image = new DynamicTexture(scaleImage(img, getWidth(), getHeight()));
                resLoc = null;
                return;
            }
        image = new DynamicTexture(img);
        resLoc = null;

    }

    public Image(int x, int y, int width, int height, URL imageURL, boolean resizeIfNeeded) {
        this(x, y, width, height, imageURL.toString(), resizeIfNeeded);
    }

    public Image(int x, int y, int width, int height, File imageFile, boolean resizeIfNeeded) {
        super(x, y, width, height);
        BufferedImage img;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
            resLoc = null;
            return;
        }
        if (resizeIfNeeded)
            if (img.getWidth() > width || img.getHeight() > height) {
                img = scaleImage(img, getWidth(), getHeight());
            }
        image = new DynamicTexture(img);
        resLoc = null;
    }

    public Image(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        super(x, y, width, height);
        image = null;
        resLoc = resourceLocation;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partial) {
        if (visible) {
            if (image == null && resLoc != null) {
                mc.getTextureManager().bindTexture(resLoc);
                drawModalRectWithCustomSizedTexture(getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
            } else if (image != null && resLoc == null) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(mc.renderEngine.getDynamicTextureLocation("image", image));
                drawModalRectWithCustomSizedTexture(getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
            } else {
                mc.getTextureManager().bindTexture(errorLoading);
                drawModalRectWithCustomSizedTexture(getX(), getY(), 0, 0, 16, 16, 16, 16);
            }
        }
    }

    public void onClick() {
        if (this.callback != null) callback.run();
    }

    private boolean mousePressed(int mouseX, int mouseY) {
        return this.visible && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mousePressed(mouseX, mouseY) && enabled && visible) {
            onClick();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }
}
