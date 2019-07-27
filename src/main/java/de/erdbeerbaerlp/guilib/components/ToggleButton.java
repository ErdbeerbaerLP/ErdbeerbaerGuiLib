package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.awt.*;

@SuppressWarnings("unused")
public class ToggleButton extends Button {
    private boolean value = false;
    private DrawType drawType = DrawType.COLORED_LINE;
    private ResourceLocation offIcon = null;

    /**
     * Creates an new ToggleButton
     *
     * @param xPos          X positon
     * @param yPos          Y Position
     * @param displayString Button Text
     */
    public ToggleButton(int xPos, int yPos, String displayString) {
        this(xPos, yPos, 100, displayString);
    }

    /**
     * Creates an new ToggleButton
     *
     * @param xPos          X positon
     * @param yPos          Y Position
     * @param width         Button Width
     * @param displayString Default String
     */
    public ToggleButton(int xPos, int yPos, int width, String displayString) {
        this(xPos, yPos, width, 20, displayString);
    }

    /**
     * Creates an new ToggleButton
     * @param xPos X positon
     * @param yPos Y Position
     * @param width Button Width
     * @param displayString Default String
     * @param icon Resource Location for an icon
     */
    public ToggleButton(int xPos, int yPos, int width, String displayString, ResourceLocation icon) {
        this(xPos, yPos, width, 20, displayString, icon);
    }

    /**
     * Creates an new ToggleButton
     * @param xPos X positon
     * @param yPos Y Position
     * @param width Button Width
     * @param displayString Default String
     * @param height Button Height
     */
    public ToggleButton(int xPos, int yPos, int width, int height, String displayString) {
        this(xPos, yPos, width, height, displayString, null);
    }

    /**
     * Creates an new ToggleButton
     * @param xPos X positon
     * @param yPos Y Position
     * @param width Button Width
     * @param displayString Default String
     * @param icon Resource Location for an icon
     * @param height Button Height
     */
    public ToggleButton(int xPos, int yPos, int width, int height, String displayString, ResourceLocation icon) {
        super(xPos, yPos, width, height, displayString, icon);
    }

    /**
     * Creates an new ToggleButton
     * @param xPos X positon
     * @param yPos Y Position
     * @param icon Resource Location for an icon
     */
    public ToggleButton(int xPos, int yPos, String string, ResourceLocation icon) {
        this(xPos, yPos, 100, string, icon);
    }

    /**
     * Creates an new ToggleButton
     * @param xPos X positon
     * @param yPos Y Position
     * @param onIcon Icon image displayed for ON / TRUE
     * @param offIcon Icon image displayed for OFF / FALSE
     */
    public ToggleButton(int xPos, int yPos, ResourceLocation onIcon, ResourceLocation offIcon) {
        this(xPos, yPos, 20, "", onIcon);
        this.offIcon = offIcon;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partial) {
        if (this.visible) {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            int k = this.getHoverState(this.hovered);
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.getX(), this.getY(), 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
//			this.mouseDragged(mc, mouseX, mouseY);
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
            if (BUTTON_ICON != null) {
                Minecraft.getMinecraft().getTextureManager().bindTexture((this.offIcon != null && (this.drawType == DrawType.STRING_OR_ICON || this.drawType == DrawType.BOTH) ? (this.value ? BUTTON_ICON : offIcon) : BUTTON_ICON));
                drawModalRectWithCustomSizedTexture(bx + 2, getY() + 2, 0, 0, 16, 16, 16, 16);

                // ! MODIFY X !
                bx += 2 + 16;
                mwidth -= 16;
            }
            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");
            if (strWidth > mwidth - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, mwidth - 6 - ellipsisWidth).trim() + "...";

            this.drawCenteredString(mc.fontRenderer, buttonText + (((drawType == DrawType.STRING_OR_ICON || drawType == DrawType.BOTH) && this.offIcon == null) ? (this.value ? "ON" : "OFF") : ""), bx + mwidth / 2, this.getY() + (this.height - 8) / 2, color);
        }
        if (this.drawType == DrawType.COLORED_LINE || this.drawType == DrawType.BOTH)
            drawRect(this.getX() + 6, this.getY() + height - 3, this.getX() + this.width - 6, this.getY() + height - 4, value ? Color.GREEN.getRGB() : Color.red.getRGB());
    }

    /**
     * Sets the DrawType of the button
     */
    public void setDrawType(DrawType type) {
        this.drawType = type;
    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {

        if (mousePressed(mc, mouseX, mouseY)) {
            playPressSound();
            this.value = !this.value;
            onClick();

        }
    }

    /**
     * Gets the button value
     */
    public boolean getValue() {
        return value;
    }

    /**
     * Sets the button value
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    public enum DrawType {
        /**
         * Draws An colored line below the string
         */
        COLORED_LINE,
        /**
         * Draws an specified string after the display string
         * Or toggles the icons
         */
        STRING_OR_ICON,
        /**
         * Displays an colored line AND toggles the string/icon
         */
        BOTH;

        /**
         * Only used for the ExampleGUI
         */
        public String getName() {
            switch (this) {
                case COLORED_LINE:
                    return "Colored Line";
                case STRING_OR_ICON:
                    return "String or Icon";
                case BOTH:
                    return "All";
            }
            return name();
        }
    }
}
