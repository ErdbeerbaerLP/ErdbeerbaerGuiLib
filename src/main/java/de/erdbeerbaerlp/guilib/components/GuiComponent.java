package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public abstract class GuiComponent extends Gui{
	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
	protected Minecraft mc = Minecraft.getMinecraft();
	private int x;
	private int y;
	protected int width;
	protected int height;
	private String[] tooltips = new String[0];
	protected final FontRenderer fontRenderer;
	protected int id;
    public int packedFGColour; //FML  (what does this do?)
    protected boolean hovered; //Sometimes used by components
	private int assignedPage = -1;
	protected boolean visible = true , enabled = true;
	protected GuiComponent(int x, int y, int width, int height) {
		this.fontRenderer = mc.fontRenderer;
		this.setX(x);
		this.setY(y);
		this.width = width;
		this.height = height;
	}
	public boolean canHaveTooltip() {
		return true;
	}
	public final void setTooltips(String... strings) {
		this.tooltips  = strings;
	}
	public final String[] getTooltips() {
		return this.tooltips;
	}
	
	public final boolean isVisible() {
		return this.visible;
	}
	public final boolean isEnabled() {
		return this.enabled;
	}
	public final void setVisible(boolean visible) {
		this.visible = visible;
	}
	public final void setEnabled(boolean enable) {
		this.enabled = enable;
	}
	
	public final int getX() {
		return x;
	}
	public final int getY() {
		return y;
	}
	public final int getWidth() {
		return width;
	}
	public final int getHeight() {
		return height;
	}
	public void playPressSound() {
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, this.enabled ? 1.0F : 0.5f));
	}
	
	public abstract void draw(int mouseX, int mouseY, float partial);
	public abstract void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException;
	public abstract void mouseReleased(int mouseX, int mouseY, int state);
	public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
	public abstract void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);
	public void updateComponent() {
		
	}
	public void handleMouseInput() {
		
	}
	public void handleKeyboardInput() {
		
	}
	
	
	public final void disable() {
		setEnabled(false);
	}
	public final void enable() {
		setEnabled(true);
	}
	
	public final void hide() {
		setVisible(false);
	}
	public final void show() {
		setVisible(true);
	}
	public final void assignToPage(int page) {
		assignedPage = page;
	}
	public final int getAssignedPage() {
		return assignedPage;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}
	
}
