package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class CheckBox extends GuiCheckBox implements IGuiComponent{
	private String[] tooltips = new String[0];
	private Runnable callback;
	
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
	public void draw(int mouseX, int mouseY, float partial) {
		if(!visible) return;
		this.drawButton(Minecraft.getMinecraft(),mouseX, mouseY, partial);
	}
	@Override
	public void setIsChecked(boolean isChecked) {
		super.setIsChecked(isChecked);
		onChange();
	}
	@Override
	public final void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		super.drawButton(mc, mouseX, mouseY, partial);
	}
	@Override
	public void setID(int id) {
		this.id = id;
	}
	@Override
	public boolean isVisible() {
		return this.visible;
	}
	@Override
	public void setTooltips(String... strings) {
		this.tooltips = strings;
	}
	@Override
	public String[] getTooltips() {
		return tooltips;
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
			playPressSound(mc.getSoundHandler());
		}
	}
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

	}
	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, this.enabled ? 1.0F : 0.5f));
	}
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
			this.setIsChecked(!isChecked());
			return true;
		}
		return false;
	}
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {


	}
	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		mouseDragged(mc, mouseX, mouseY);

	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
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
	private int page = -1;
	@Override
	public void assignToPage(int page) {
		this.page = page;
	}

	@Override
	public int getAssignedPage() {
		return this.page;
	}
}
