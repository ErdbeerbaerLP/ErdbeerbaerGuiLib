package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;

public class Label extends GuiLabel implements IGuiComponent{

	public Label(int x, int y, int width, int height, int color) {
		super(Minecraft.getMinecraft().fontRenderer, -1, x, y, width, height, color);
		this.x = x;
		this.y = y;
		this.visible = true;
	}
	public Label(int x, int y) {
		this(x, y, 150, 20, -1);
	}
	public Label(String text, int x, int y) {
		this(x, y);
		this.addLine(text);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partial) {
		if(!visible) return;
		this.drawLabel(Minecraft.getMinecraft(), mouseX, mouseY);
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
		
		return this.visible;
	}
	@Override
	public int getX() {
		
		return x;
	}

	@Override
	public int getY() {
		
		return y;
	}

	@Override
	public int getWidth() {
		
		return width;
	}

	@Override
	public int getHeight() {
		
		return height;
	}
	@Override
	public void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {
		
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
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		this.visible = visible;
	}

	@Override
	public void setEnabled(boolean enable) {
	}
}
