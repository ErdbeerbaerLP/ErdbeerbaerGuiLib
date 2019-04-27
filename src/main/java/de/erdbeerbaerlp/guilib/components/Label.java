package de.erdbeerbaerlp.guilib.components;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;

public class Label extends GuiLabel implements IGuiComponent{

	public Label(FontRenderer font, int x, int y, int width, int height, int color) {
		super(font, -1, x, y, width, height, color);
		this.x = x;
		this.y = y;
		this.visible = true;
	}
	public Label(FontRenderer font, int x, int y) {
		this(font, x, y, 150, 20, -1);
	}
	public Label(FontRenderer font, String text, int x, int y) {
		this(font, x, y);
		this.addLine(text);
	}
	@Override
	public void setID(int id) {
		this.id = id;
	}
	@Override
	public boolean canHaveTooltip() {
		return false;
	}
	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return this.visible;
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
