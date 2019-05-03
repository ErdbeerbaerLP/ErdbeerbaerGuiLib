package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;

public class Button extends GuiButtonExt implements IGuiComponent{
	protected ResourceLocation BUTTON_ICON;
	private String[] tooltips = new String[0];
	private Runnable callback;
	public static class DefaultButtonIcons {
		public static final ResourceLocation ADD = new ResourceLocation("eguilib:textures/gui/buttonicons/add.png");
		public static final ResourceLocation DELETE = new ResourceLocation("eguilib:textures/gui/buttonicons/delete.png");
		public static final ResourceLocation PLAY = new ResourceLocation("eguilib:textures/gui/buttonicons/play.png");
		public static final ResourceLocation PAUSE = new ResourceLocation("eguilib:textures/gui/buttonicons/pause.png");
		public static final ResourceLocation STOP = new ResourceLocation("eguilib:textures/gui/buttonicons/stop.png");
		public static final ResourceLocation SAVE = new ResourceLocation("eguilib:textures/gui/buttonicons/save.png");
		public static final ResourceLocation NEW = new ResourceLocation("eguilib:textures/gui/buttonicons/new.png");
		public static final ResourceLocation FILE = new ResourceLocation("eguilib:textures/gui/buttonicons/file.png");
		public static final ResourceLocation FILE_TXT = new ResourceLocation("eguilib:textures/gui/buttonicons/file_txt.png");
		public static final ResourceLocation FILE_NBT = new ResourceLocation("eguilib:textures/gui/buttonicons/file_nbt.png");
		public static final ResourceLocation FILE_BIN = new ResourceLocation("eguilib:textures/gui/buttonicons/file_bin.png");
	}
	public Button(int xPos, int yPos, String displayString) {
		this(xPos, yPos, 100, displayString);
	}
	
	public Button(int xPos, int yPos, int width, String displayString) {
		this(xPos, yPos, width, 20, displayString);
	}
	public Button(int xPos, int yPos, int width, String displayString, ResourceLocation icon) {
		this(xPos, yPos, width, 20, displayString, icon);
	}
	public Button(int xPos, int yPos, int width, int height, String displayString) {
		this(xPos, yPos, width, height, displayString, null);
	}
	public Button(int xPos, int yPos, int width, int height, String displayString, ResourceLocation icon) {
		super(-1, xPos, yPos, width, height, displayString);
		this.BUTTON_ICON = icon;
	}
	
	public Button(int xPos, int yPos, String string, ResourceLocation icon) {
		this(xPos, yPos, 100, string, icon);
	}

	public Button(int xPos, int yPos, ResourceLocation icon) {
		this(xPos, yPos, 20, "", icon);
	}

	public final void setID(int id){
		this.id = id;
	}
	public final void setClickListener(Runnable r) {
		this.callback = r;
	}
	public final void onClick() {
		if(this.callback != null) this.callback.run();
	}
	@Override
	public void setTooltips(String... tooltips) {
		this.tooltips  = tooltips;
	}
	/**
     * Draws this button to the screen.
     */
	@Override
	public void draw(int mouseX, int mouseY, float partial) {
        if (this.visible)
        {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int k = this.getHoverState(this.hovered);
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;

            if (packedFGColour != 0)
            {
                color = packedFGColour;
            }
            else if (!this.enabled)
            {
                color = 10526880;
            }
            else if (this.hovered)
            {
                color = 16777120;
            }
            int bx = this.x;
            int mwidth = this.width;
            if(BUTTON_ICON != null) {
            	Minecraft.getMinecraft().getTextureManager().bindTexture(BUTTON_ICON);
				drawModalRectWithCustomSizedTexture(bx + 2, y + 2, 0, 0, 16, 16, 16, 16);

				// ! MODIFY X !
				bx += 2 + 16;
				mwidth -= 16;
			}
            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");
            if (strWidth > mwidth - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, mwidth - 6 - ellipsisWidth).trim() + "...";

            this.drawCenteredString(mc.fontRenderer, buttonText, bx + mwidth / 2, this.y + (this.height - 8) / 2, color);
        }
        
    }
	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public String[] getTooltips() {
		return this.tooltips;
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

	public final void setText(String text) {
		this.displayString = text;
	}

	@Override
	public void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if(mousePressed(mc, mouseX, mouseY)) {
			playPressSound(mc.getSoundHandler());
			onClick();
			
		}
	}
	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, this.enabled ? 1.0F : 0.5f));
	}
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
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
	
}
