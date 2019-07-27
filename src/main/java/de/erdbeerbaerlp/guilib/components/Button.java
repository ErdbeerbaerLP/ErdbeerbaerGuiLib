package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

@SuppressWarnings("unused")
public class Button extends GuiComponent {

    protected DynamicTexture BUTTON_ICON_IMAGE;
    protected ResourceLocation BUTTON_ICON;
    protected String displayString;
    private Runnable callback;
    private String errorTooltip = "";

    /**
     * Creates a Button
     *
     * @param xPos          x position
     * @param yPos          y position
     * @param displayString Button Text
     */
    public Button(int xPos, int yPos, String displayString) {
        this(xPos, yPos, 100, displayString);
    }

    /**
     * Creates a Button
     *
     * @param xPos          x position
     * @param yPos          y position
     * @param width         Button width
     * @param displayString Button Text
     */
    public Button(int xPos, int yPos, int width, String displayString) {
        this(xPos, yPos, width, 20, displayString);
    }

    /**
     * Creates a Button with icon
     * @param xPos x position
     * @param yPos y position
     * @param width Button width
     * @param displayString Button Text
     * @param icon ResourceLocation to an Icon image (16x16!)
     */
    public Button(int xPos, int yPos, int width, String displayString, ResourceLocation icon) {
        this(xPos, yPos, width, 20, displayString, icon);
    }

    /**
     * Creates a Button
     * @param xPos x position
     * @param yPos y position
     * @param width Button width
     * @param height Button height
     * @param displayString Button Text
     */
    public Button(int xPos, int yPos, int width, int height, String displayString) {
        this(xPos, yPos, width, height, displayString, (ResourceLocation) null);
    }

    /**
     * Creates a Button with icon
     * @param xPos x position
     * @param yPos y position
     * @param width Button width
     * @param height Button height
     * @param displayString Button Text
     * @param icon ResourceLocation to an Icon image (16x16!)
     */
    public Button(int xPos, int yPos, int width, int height, String displayString, ResourceLocation icon) {
        super(xPos, yPos, width, height);
        this.BUTTON_ICON = icon;
        this.displayString = displayString;
    }

    /**
     * Creates a Button with icon
     * @param xPos x position
     * @param yPos y position
     * @param width Button width
     * @param height Button height
     * @param displayString Button Text
     * @param iconURL URL to an Icon image (16x16!)
     */
    public Button(int xPos, int yPos, int width, int height, String displayString, String iconURL) {
        this(xPos, yPos, width, height, displayString);
        try {
            this.BUTTON_ICON_IMAGE = new DynamicTexture(loadImageFromURL(iconURL));
        } catch (IOException e) {
            errorTooltip = e.getCause().getLocalizedMessage();
        }
        this.displayString = displayString;
    }

    /**
     * Creates a Button with icon
     * @param xPos x position
     * @param yPos y position
     * @param width Button width
     * @param height Button height
     * @param displayString Button Text
     * @param iconURL URL to an Icon image (16x16!)
     */
    public Button(int xPos, int yPos, int width, int height, String displayString, URL iconURL) {
        this(xPos, yPos, width, height, displayString, iconURL.toString());


    }

    /**
     * Creates a Button with icon
     * @param xPos x position
     * @param yPos y position
     * @param width Button width
     * @param height Button height
     * @param displayString Button Text
     * @param icon Icon as BufferedImage (16x16!)
     */
    public Button(int xPos, int yPos, int width, int height, String displayString, BufferedImage icon) {
        super(xPos, yPos, width, height);
        this.BUTTON_ICON_IMAGE = new DynamicTexture(icon);
        this.displayString = displayString;
    }

    /**
     * Creates a Button with icon
     *
     * @param xPos          x position
     * @param yPos          y position
     * @param displayString Button text
     * @param icon          ResourceLocation to an Icon image (16x16!)
     */
    public Button(int xPos, int yPos, String displayString, ResourceLocation icon) {
        this(xPos, yPos, 100, displayString, icon);
    }

    /**
     * Creates a Button with icon
     * @param xPos x position
     * @param yPos y position
     * @param icon ResourceLocation to an Icon image (16x16!)
     */
    public Button(int xPos, int yPos, ResourceLocation icon) {
        this(xPos, yPos, 20, "", icon);
    }

    /**
     * Add a click listener / callback
     * @param r Listener
     */
    public final void setClickListener(Runnable r) {
        this.callback = r;
    }

    /**
     * Gets called on button click
     */
    public void onClick() {
        if (this.callback != null) this.callback.run();
    }

    @Override
    public String[] getTooltips() {
        if (BUTTON_ICON == null && BUTTON_ICON_IMAGE == null) {
            return ArrayUtils.addAll(super.getTooltips(), "", "§cError loading image:", "§c" + errorTooltip);
        }
        return super.getTooltips();
    }
    @Override
    public void draw(int mouseX, int mouseY, float partial) {
        if (this.visible) {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            int k = this.getHoverState(this.hovered);
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.getX(), this.getY(), 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            int color = 14737632;

            if (packedFGColour != 0) {
                color = packedFGColour;
            } else if (!this.enabled) {
                color = 10526880;
            } else if (this.hovered) {
                color = 16777120;
            }
            int bx = this.getX();
            int mwidth = this.width;
            if (BUTTON_ICON != null && BUTTON_ICON_IMAGE == null) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(BUTTON_ICON);
                drawModalRectWithCustomSizedTexture(bx + 2, getY() + 2, 0, 0, 16, 16, 16, 16);

                // ! MODIFY X !
                bx += 2 + 16;
                mwidth -= 16;
            } else if (BUTTON_ICON_IMAGE != null && BUTTON_ICON == null) {
                mc.getTextureManager().bindTexture(mc.renderEngine.getDynamicTextureLocation("icon", BUTTON_ICON_IMAGE));
                drawModalRectWithCustomSizedTexture(bx + 2, getY(), 0, 0, 16, 16, 16, 16);
                bx += 2 + 16;
                mwidth -= 16;
            } else //noinspection ConstantConditions
                if (BUTTON_ICON_IMAGE == null && BUTTON_ICON == null && !errorTooltip.equals("")) {
                    mc.getTextureManager().bindTexture(errorIcon);
                    drawModalRectWithCustomSizedTexture(bx + 2, getY(), 0, 0, 16, 16, 16, 16);
                    bx += 2 + 16;
                    mwidth -= 16;
                }
            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");
            if (strWidth > mwidth - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, mwidth - 6 - ellipsisWidth).trim() + "...";

            this.drawCenteredString(mc.fontRenderer, buttonText, bx + mwidth / 2, this.getY() + (this.height - 8) / 2, color);
        }

    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    public final void setText(String text) {
        this.displayString = text;
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if (mousePressed(mc, mouseX, mouseY)) {
            playPressSound();
            if (enabled) onClick();
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.visible && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
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

    public static class DefaultButtonIcons {
        public static final ResourceLocation ADD = new ResourceLocation("eguilib:textures/gui/buttonicons/add.png");
        public static final ResourceLocation DELETE = new ResourceLocation("eguilib:textures/gui/buttonicons/delete.png");
        public static final ResourceLocation PLAY = new ResourceLocation("eguilib:textures/gui/buttonicons/play.png");
        public static final ResourceLocation PAUSE = new ResourceLocation("eguilib:textures/gui/buttonicons/pause.png");
        public static final ResourceLocation STOP = new ResourceLocation("eguilib:textures/gui/buttonicons/stop.png");
        public static final ResourceLocation SAVE = new ResourceLocation("eguilib:textures/gui/buttonicons/save.png");
        public static final ResourceLocation NEW = new ResourceLocation("eguilib:textures/gui/buttonicons/new.png");
        public static final ResourceLocation FILE = new ResourceLocation("eguilib:textures/gui/buttonicons/file.png");
        public static final ResourceLocation FILE_TXT = new ResourceLocation("eguilib:textures/gui/buttonicons/file_txt.png");
        public static final ResourceLocation FILE_NBT = new ResourceLocation("eguilib:textures/gui/buttonicons/file_nbt.png");
        public static final ResourceLocation FILE_BIN = new ResourceLocation("eguilib:textures/gui/buttonicons/file_bin.png");
        public static final ResourceLocation ARROW_RIGHT = new ResourceLocation("eguilib:textures/gui/buttonicons/arrow-right.png");
        public static final ResourceLocation ARROW_LEFT = new ResourceLocation("eguilib:textures/gui/buttonicons/arrow-left.png");

    }

}
