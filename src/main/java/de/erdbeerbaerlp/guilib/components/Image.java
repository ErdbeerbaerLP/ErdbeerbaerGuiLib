package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Image extends GuiComponent {
    private final DynamicTexture image;
    private final ResourceLocation resLoc;
    private Runnable callback;
    private String errorTooltip = "";

    public Image(int x, int y, int width, int height, String imageURL) {
        super(x, y, width, height);
        BufferedImage img;
        try {
            img = loadImageFromURL(imageURL);
        } catch (IOException e) {
            errorTooltip = e.getCause().getLocalizedMessage();
            image = null;
            resLoc = null;
            return;
        }
        image = new DynamicTexture(img);
        resLoc = null;

    }

    public Image(int x, int y, int width, int height, URL imageURL) {
        this(x, y, width, height, imageURL.toString());
    }

    public Image(int x, int y, int width, int height, File imageFile) {
        super(x, y, width, height);
        BufferedImage img;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            errorTooltip = e.getCause().getLocalizedMessage();
            image = null;
            resLoc = null;
            return;
        }
        image = new DynamicTexture(img);
        resLoc = null;
    }

    public Image(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        super(x, y, width, height);
        image = null;
        resLoc = resourceLocation;
    }

    @Override
    public String[] getTooltips() {
        if (image == null && resLoc == null) {
            return ArrayUtils.addAll(super.getTooltips(), "", "§cError loading image:", "§c" + errorTooltip);
        }
        return super.getTooltips();
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
                mc.getTextureManager().bindTexture(errorIcon);
                drawModalRectWithCustomSizedTexture(getX() + getWidth() / 2, getY() + getHeight() / 2, 0, 0, 16, 16, 16, 16);
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
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if (mousePressed(mouseX, mouseY) && enabled && visible) {
            onClick();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }
}
