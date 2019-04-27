package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import scala.util.Random;

public class TextField extends GuiTextField implements IGuiComponent{
	private String[] tooltip = new String[0];
	private final Random r = new Random();
	private boolean acceptsColors = false;
	public TextField(FontRenderer fontRenderer, int x, int y, int width, int height) {
		super(-1, fontRenderer, x, y, width, height);
		setMaxStringLength(100);
	}
	@Override
	public void writeText(String textToWrite) {
		final String colorCodePlaceholder = "[COLOR"+r.nextInt(1000)+"]";
		if(acceptsColors) {
			textToWrite = textToWrite.replace("\00A7", colorCodePlaceholder);
			super.writeText(textToWrite);
			this.setText(getText().replace(colorCodePlaceholder, "\00A7"));
		}else {
			super.writeText(textToWrite);
		}
	}
	public void setTooltip(String... strings) {
		this.tooltip = strings;
	}
	public void setAcceptsColors(boolean acceptsColors) {
		this.acceptsColors = acceptsColors;
	}
	public void setWidth(int width) {
		// TODO Auto-generated method stub
		this.width = width;
	}
	@Override
	public void setID(int id) {
		this.id = id;
	}
	@Override
	public String[] getTooltips() {
		// TODO Auto-generated method stub
		return this.tooltip;
	}
	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return this.getVisible();
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
	
}
