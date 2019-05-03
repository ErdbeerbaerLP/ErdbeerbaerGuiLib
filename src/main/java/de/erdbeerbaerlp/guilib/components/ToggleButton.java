package de.erdbeerbaerlp.guilib.components;

import java.awt.Color;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class ToggleButton extends Button {
	private boolean value = false;
	private DrawType drawType = DrawType.COLORED_LINE;
	private ResourceLocation offIcon = null;
	public static enum DrawType{

		COLORED_LINE,STRING_OR_ICON,BOTH;

		public String getName() {
			switch(this) {
			case COLORED_LINE:
				return "Colored Line";
			case STRING_OR_ICON:
				return "String or Icon";
			case BOTH:
				return "All";
			}
			return name();
		}
	}
	public ToggleButton(int xPos, int yPos, String displayString) {
		this(xPos, yPos, 100, displayString);
	}

	public ToggleButton(int xPos, int yPos, int width, String displayString) {
		this(xPos, yPos, width, 20, displayString);
	}

	public ToggleButton(int xPos, int yPos, int width, String displayString, ResourceLocation icon) {
		this(xPos, yPos, width, 20, displayString, icon);
	}

	public ToggleButton(int xPos, int yPos, int width, int height, String displayString) {
		this(xPos, yPos, width, height, displayString, null);
	}

	public ToggleButton(int xPos, int yPos, int width, int height, String displayString, ResourceLocation icon) {
		super(xPos, yPos, width, height, displayString, icon);
	}

	public ToggleButton(int xPos, int yPos, String string, ResourceLocation icon) {
		this(xPos, yPos, 100, string, icon);
	}

	public ToggleButton(int xPos, int yPos, ResourceLocation onIcon, ResourceLocation offIcon) {
		this(xPos, yPos, 20, "", onIcon);
		this.offIcon = offIcon;
	}
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
				Minecraft.getMinecraft().getTextureManager().bindTexture((this.offIcon != null && (this.drawType == DrawType.STRING_OR_ICON || this.drawType == DrawType.BOTH) ? (this.value? BUTTON_ICON : offIcon) : BUTTON_ICON));
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

			this.drawCenteredString(mc.fontRenderer, buttonText +(((drawType == DrawType.STRING_OR_ICON || drawType == DrawType.BOTH) && this.offIcon == null) ? (this.value?"ON":"OFF"):""), bx + mwidth / 2, this.y + (this.height - 8) / 2, color);
		}
		if(this.drawType == DrawType.COLORED_LINE || this.drawType == DrawType.BOTH) drawRect(this.x + 6, this.y + height - 3, this.x + this.width -6,  this.y + height - 4, value?Color.GREEN.getRGB():Color.red.getRGB());
	}
	public void setDrawType(DrawType type) {
		// TODO Auto-generated method stub
		this.drawType  = type;
	}
	public void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {

		if(mousePressed(mc, mouseX, mouseY)) {
			playPressSound(mc.getSoundHandler());
			this.value = !this.value;
			onClick();

		}
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	public boolean getValue() {
		return value;
	}
}
