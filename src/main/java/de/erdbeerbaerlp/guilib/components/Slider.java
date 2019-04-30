package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraftforge.fml.client.config.GuiSlider;

public class Slider extends GuiSlider implements IGuiComponent {


	private Runnable action;
	private String[] tooltips;
	private double prevValue;

	public Slider(int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, Runnable changeAction) {
		this(xPos, yPos, 150, 20, displayStr, "", minVal, maxVal, currentVal, true, true, changeAction);
	}
	public Slider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
		this(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
	}
	public Slider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, Runnable changeAction) {
		super(-1, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
		this.action = changeAction;
	}
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	@Override
	public void setID(int id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	@Override
	public void updateSlider() {
        if (this.sliderValue < 0.0F)
        {
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F)
        {
            this.sliderValue = 1.0F;
        }

        String val;

        if (showDecimal)
        {
            val = Double.toString(sliderValue * (maxValue - minValue) + minValue);

            if (val.substring(val.indexOf(".") + 1).length() > precision)
            {
                val = val.substring(0, val.indexOf(".") + precision + 1);

                if (val.endsWith("."))
                {
                    val = val.substring(0, val.indexOf(".") + precision);
                }
            }
            else
            {
                while (val.substring(val.indexOf(".") + 1).length() < precision)
                {
                    val = val + "0";
                }
            }
        }
        else
        {
            val = Integer.toString((int)Math.round(sliderValue * (maxValue - minValue) + minValue));
        }

        if(drawString)
        {
            displayString = dispString + val + suffix;
        }
        if(prevValue != getValue()) this.onValueChanged();
	}
	public void onValueChanged() {
		if(this.action != null) action.run();
	}
	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		this.visible = visible;
	}

	@Override
	public void setEnabled(boolean enable) {
		// TODO Auto-generated method stub
		this.enabled = enable;
	}
	@Override
	public void setTooltips(String... strings) {
		this.tooltips = strings;
	}
	public String[] getTooltips() {
		return tooltips;
	}
	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial) {
		drawButton(mc, mouseX, mouseY, partial);
	}

	@Override
	public void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.prevValue = getValue();
		this.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		this.mouseReleased(mouseX, mouseY);

	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		// TODO Auto-generated method stub
		this.prevValue = getValue();
		this.mouseDragged(mc, mouseX, mouseY);
	}

}
