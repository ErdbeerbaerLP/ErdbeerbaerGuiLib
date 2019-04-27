package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class CheckBox extends GuiCheckBox implements IGuiComponent{
	private String[] tooltips = new String[0];
	public CheckBox(int xPos, int yPos, String displayString, boolean isChecked) {
		super(-1, xPos, yPos, displayString, isChecked);
	}
	public CheckBox(int xPos, int yPos, String displayString) {
		this(xPos, yPos, displayString, false);
	}
	public CheckBox(int xPos, int yPos) {
		this(xPos, yPos, "", false);
	}
	public CheckBox(int xPos, int yPos, int width, int height) {
		this(xPos, yPos, "", false);
		this.width = width;
		this.height = height;
	}
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		super.drawButton(mc, mouseX, mouseY, partial);
	}
	@Override
	public void setID(int id) {
		this.id = id;
	}
	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return this.visible;
	}
	@Override
	public void setTooltips(String... strings) {
		this.tooltips = strings;
	}
	@Override
	public String[] getTooltips() {
		// TODO Auto-generated method stub
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

}
