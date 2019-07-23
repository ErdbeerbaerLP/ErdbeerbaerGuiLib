package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class EnumSlider extends Slider {
    private Runnable action;
    private Enum<?> enumValue;
    private Enum<?>[] enumValues;

    private int prevIndex;

    public <T extends Enum<T>> EnumSlider(int xPos, int yPos, String displayStr, Class<T> enumClass, T currentVal, Runnable changeAction) {
        this(xPos, yPos, 150, 20, displayStr, "", enumClass, currentVal, true, changeAction);
    }

    public <T extends Enum<T>> EnumSlider(int xPos, int yPos, int width, int height, String prefix, String suf, Class<T> enumClass, T currentVal, boolean drawStr) {
        this(xPos, yPos, width, height, prefix, suf, enumClass, currentVal, drawStr, null);
    }

    public <T extends Enum<T>> EnumSlider(int xPos, int yPos, int width, int height, String prefix, String suf, Class<T> enumClass, T currentVal, boolean drawStr, Runnable changeAction) {
        super(xPos, yPos, width, height, prefix, suf, -1, -1, -1, false, drawStr, null);

        this.action = changeAction;
        this.enumValue = currentVal;
        this.enumValues = enumClass.getEnumConstants();
        this.maxValue = enumValues.length;
        this.showDecimal = false;
        this.minValue = 0;
        this.sliderValue = (getCurrentIndex() - minValue) / (maxValue - minValue);
        //Try to get custom name
        String val = "";
        for (Method m : this.enumValue.getClass().getMethods()) {
            if (m.getName().equals("getName") && m.getParameterTypes().length == 0 && m.getReturnType() == String.class) {
                if (!m.isAccessible()) m.setAccessible(true);
                try {
                    val = (String) m.invoke(enumValue);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        displayString = dispString + (val.isEmpty() ? this.enumValue.name() : val) + suffix;

        drawString = drawStr;
        if (!drawString) {
            displayString = "";
        }

    }

    public Enum<?> getEnum() {
        return enumValue;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partial) {
        super.draw(mouseX, mouseY, partial);
    }

    @Override
    public void updateSlider() {
        if (this.sliderValue < 0.0F) {
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F) {
            this.sliderValue = 1.0F;
        }
        String val = this.enumValue.name();
        //Try to get custom name
        for (Method m : this.enumValue.getClass().getMethods()) {
            if (m.getName().equals("getName") && m.getParameterTypes().length == 0 && m.getReturnType() == String.class) {
                if (!m.isAccessible()) m.setAccessible(true);
                try {
                    val = (String) m.invoke(enumValue);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (drawString) {
            displayString = dispString + val + suffix;
        }
        if (prevIndex != getCurrentIndex()) this.onValueChanged();
    }

    public void onValueChanged() {
        if (this.action != null) action.run();
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        this.prevIndex = getCurrentIndex();
        if (this.visible) {
            if (this.dragging) {
                final int index = getCurrentIndex();
                this.sliderValue = (float) (mouseX - (this.getX() + 4)) / (float) (this.width - 8);
                int sliderValue = (int) Math.round(this.sliderValue * (maxValue - minValue) + this.minValue);
                if (sliderValue < 0) sliderValue = 0;
                if (index == -1) {
                    this.enumValue = enumValues[0];
                } else {
                    if (sliderValue < this.enumValues.length) {
                        this.enumValue = enumValues[sliderValue];
                    }
                }
                updateSlider();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.getX() + (int) (this.sliderValue * (float) (this.width - 8)), this.getY(), 0, 66, 4, 20);
            this.drawTexturedModalRect(this.getX() + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.getY(), 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        if (superMousePressed(par2, par3)) {
            final int index = getCurrentIndex();
            playPressSound();
            this.sliderValue = (float) (par2 - (this.getX() + 4)) / (float) (this.width - 8);
            int sliderValue = (int) Math.round(this.sliderValue * (maxValue - minValue) + this.minValue);
            if (sliderValue < 0) return false;
            if (index == -1) {
                this.enumValue = enumValues[0];
            } else {
                if (sliderValue < this.enumValues.length) {
                    this.enumValue = enumValues[sliderValue];
                }
            }
            updateSlider();
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    private int getCurrentIndex() {
        int out = 0;
        for (Enum<?> e : this.enumValues) {
            if (e.name().equals(this.enumValue.name())) return out;
            out++;
        }
        return -1;
    }

    public boolean superMousePressed(int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        this.prevIndex = getCurrentIndex();
        if (this.mousePressed(mc, mouseX, mouseY)) {
            playPressSound();
        }
    }
}
