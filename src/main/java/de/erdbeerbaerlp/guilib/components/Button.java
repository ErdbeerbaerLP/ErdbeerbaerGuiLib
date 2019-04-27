package de.erdbeerbaerlp.guilib.components;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;

public final class Button extends GuiButtonExt implements IGuiComponent{
	private ButtonIcon BUTTON_ICON;
	private String[] tooltips = new String[0];
	private Runnable callback;
	public enum ButtonIcon{
		NONE(null),
		ADD(new ResourceLocation("guilib:textures/gui/buttonIcons/add.png")),
		DELETE(new ResourceLocation("eguilib:textures/gui/buttonIcons/delete.png")),
		PLAY(new ResourceLocation("eguilib:textures/gui/buttonIcons/play.png")),
		PAUSE(new ResourceLocation("eguilib:textures/gui/buttonIcons/pause.png")),
		STOP(new ResourceLocation("eguilib:textures/gui/buttonIcons/stop.png")),
		SAVE(new ResourceLocation("eguilib:textures/gui/buttonIcons/save.png")),
		NEW(new ResourceLocation("eguilib:textures/gui/buttonIcons/new.png")),
		FILE(new ResourceLocation("eguilib:textures/gui/buttonIcons/file.png")),
		FILE_TXT(new ResourceLocation("eguilib:textures/gui/buttonIcons/file_txt.png")),
		FILE_NBT(new ResourceLocation("eguilib:textures/gui/buttonIcons/file_nbt.png")),
		FILE_BIN(new ResourceLocation("eguilib:textures/gui/buttonIcons/file_bin.png"));
		
		private final ResourceLocation location;
		ButtonIcon(ResourceLocation loc) {
			this.location = loc;
		}
		public ResourceLocation getResourceLocation() {
			return location;
		}
	}
	public Button(int xPos, int yPos, String displayString) {
		this(xPos, yPos, 100, displayString);
	}
	
	public Button(int xPos, int yPos, int width, String displayString) {
		this(xPos, yPos, width, 20, displayString);
	}
	public Button(int xPos, int yPos, int width, String displayString, ButtonIcon icon) {
		this(xPos, yPos, width, 20, displayString, icon);
	}
	public Button(int xPos, int yPos, int width, int height, String displayString) {
		this(xPos, yPos, width, height, displayString, ButtonIcon.NONE);
	}
	public Button(int xPos, int yPos, int width, int height, String displayString, ButtonIcon icon) {
		super(-1, xPos, yPos, width, height, displayString);
		this.BUTTON_ICON = icon;
	}
	
	public Button(int xPos, int yPos, String string, ButtonIcon icon) {
		this(xPos, yPos, 100, string, icon);
	}

	public final void setID(int id){
		this.id = id;
	}
	public final void addClickListener(Runnable r) {
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
	public final void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
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
            if(BUTTON_ICON != ButtonIcon.NONE) {
            	Minecraft.getMinecraft().getTextureManager().bindTexture(BUTTON_ICON.getResourceLocation());
				drawModalRectWithCustomSizedTexture(bx + 2, y + 2, 0, 0, 16, 16, 16, 16);

				// ! MODIFY X !
				bx += 2 + 16;
			}
            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

            this.drawCenteredString(mc.fontRenderer, buttonText, bx + this.width / 2, this.y + (this.height - 8) / 2, color);
            
            
            if(this.hovered && this.tooltips.length != 0) {
            	//Draw hovering text
            }
        }
        
    }
	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return this.visible;
	}

	@Override
	public String[] getTooltips() {
		// TODO Auto-generated method stub
		return this.tooltips;
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
