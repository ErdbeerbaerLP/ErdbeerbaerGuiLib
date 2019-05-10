package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiUtils;
//GuiCheckBox
public class CheckBox extends GuiComponent{
	private Runnable callback;
	private int boxWidth;
	private String displayString;
	private boolean isChecked = false;
	public CheckBox(int xPos, int yPos, String displayString, boolean isChecked) {
		super(xPos, yPos, 0 , 0);
		this.displayString = displayString;
		this.isChecked = isChecked;
		this.boxWidth = 11;
	    this.height = 11;
	    this.width = this.boxWidth + 2 + Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString);
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
	public void draw(int mouseX, int mouseY, float partial) {
		if (this.visible)
        {
            this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.boxWidth && mouseY < this.getY() + this.height;
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.getX(), this.getY(), 0, 46, this.boxWidth, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
//            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;

            if (packedFGColour != 0)
            {
                color = packedFGColour;
            }
            else if (!this.enabled)
            {
                color = 10526880;
            }

            if (this.isChecked)
                this.drawCenteredString(mc.fontRenderer, "x", this.getX() + this.boxWidth / 2 + 1, this.getY() + 1, 14737632);

            this.drawString(mc.fontRenderer, displayString, this.getX() + this.boxWidth + 2, this.getY() + 2, color);
        }
	}
	public boolean isChecked()
    {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }
	public void onChange() { 
		if(this.callback != null)this.callback.run();
	}
	public final void setChangeListener(Runnable r) {
		this.callback = r;
	}
	@Override
	public void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
			onChange();
			playPressSound();
		}
	}
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

	}
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(this.visible && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height) {
			this.setIsChecked(!isChecked());
			return true;
		}
		return false;
	}
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		// TODO Auto-generated method stub
		
	}
}
