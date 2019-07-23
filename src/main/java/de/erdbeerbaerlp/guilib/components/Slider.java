package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;

@SuppressWarnings("unused")
public class Slider extends GuiComponent {


    protected Runnable action;
    protected double prevValue;
    protected double minValue;
    protected double maxValue;
    protected double sliderValue;
    protected String dispString;
    protected String suffix;
    protected boolean showDecimal;
    protected int precision;
    protected String displayString;
    protected boolean drawString;
    protected boolean dragging;

    public Slider(int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, Runnable changeAction) {
        this(xPos, yPos, 150, 20, displayStr, "", minVal, maxVal, currentVal, true, true, changeAction);
    }

    public Slider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
        this(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
    }

    public Slider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, Runnable changeAction) {
        super(xPos, yPos, width, height);
        this.action = changeAction;
        minValue = minVal;
        maxValue = maxVal;
        sliderValue = (currentVal - minValue) / (maxValue - minValue);
        dispString = prefix;
        suffix = suf;
        showDecimal = showDec;
        String val;

        if (showDecimal) {
            val = Double.toString(sliderValue * (maxValue - minValue) + minValue);
            precision = Math.min(val.substring(val.indexOf(".") + 1).length(), 4);
        } else {
            val = Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue));
            precision = 0;
        }

        displayString = dispString + val + suffix;

        drawString = drawStr;
        if (!drawString) {
            displayString = "";
        }

    }

    public void setAction(Runnable action) {
        this.action = action;
    }


    public void updateSlider() {
        if (this.sliderValue < 0.0F) {
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F) {
            this.sliderValue = 1.0F;
        }

        String val;

        if (showDecimal) {
            val = Double.toString(sliderValue * (maxValue - minValue) + minValue);

            if (val.substring(val.indexOf(".") + 1).length() > precision) {
                val = val.substring(0, val.indexOf(".") + precision + 1);

                if (val.endsWith(".")) {
                    val = val.substring(0, val.indexOf(".") + precision);
                }
            } else {
                while (val.substring(val.indexOf(".") + 1).length() < precision) {
                    //noinspection StringConcatenationInLoop
                    val = val + "0";
                }
            }
        } else {
            val = Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue));
        }

        if (drawString) {
            displayString = dispString + val + suffix;
        }
        if (prevValue != getValue()) this.onValueChanged();
    }

    public void onValueChanged() {
        if (this.action != null) action.run();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partial) {
        if (this.visible) {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            int k = this.getHoverState(this.hovered);
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.getX(), this.getY(), 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            this.mouseClickMove(mouseX, mouseY, 0, 0);
            int color = 14737632;

            if (packedFGColour != 0) {
                color = packedFGColour;
            } else if (!this.enabled) {
                color = 10526880;
            } else if (this.hovered) {
                color = 16777120;
            }

            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

            this.drawCenteredString(mc.fontRenderer, buttonText, this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);
        }
    }

    private int getHoverState(boolean hovered) {
        return 0;
    }


    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        this.prevValue = getValue();
        if (this.mousePressed(mc, mouseX, mouseY)) {
            playPressSound();
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height) {
            this.sliderValue = (float) (mouseX - (this.getX() + 4)) / (float) (this.width - 8);
            updateSlider();
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        this.prevValue = getValue();
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (mouseX - (this.getX() + 4)) / (float) (this.width - 8);
                updateSlider();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.getX() + (int) (this.sliderValue * (float) (this.width - 8)), this.getY(), 0, 66, 4, 20);
            this.drawTexturedModalRect(this.getX() + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.getY(), 196, 66, 4, 20);
        }
    }

    public int getValueInt() {
        return (int) Math.round(sliderValue * (maxValue - minValue) + minValue);
    }

    public double getValue() {
        return sliderValue * (maxValue - minValue) + minValue;
    }
}
