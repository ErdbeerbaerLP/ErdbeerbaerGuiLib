package de.erdbeerbaerlp.guilib.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.erdbeerbaerlp.guilib.components.Button;
import de.erdbeerbaerlp.guilib.components.IGuiComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BetterGuiScreen extends GuiScreen {
	private final List<IGuiComponent> components;
	private int nextComponentID = 0;
	public BetterGuiScreen() {
		components =  new ArrayList<IGuiComponent>();
		initGui();
	}
	/**
	 * Add your components here!
	 */
	public abstract void initGui();
	@Override
	public abstract void updateScreen();
	@Override
	public abstract boolean doesGuiPauseGame();
	/**
	 * Use this to add your components
	 * @param component
	 */
	public final void addComponent(IGuiComponent component) {
		component.setID(nextComponentID);
		nextComponentID++;
		if(component instanceof GuiButton) {
			this.addButton((GuiButton) component);
			return;
		}
		this.components.add(component);
	}
	@Override
	protected final <T extends GuiButton> T addButton(T buttonIn) {
		// TODO Auto-generated method stub
		return super.addButton(buttonIn);
	}
	protected final void actionPerformed(GuiButton button) throws IOException {
		for(IGuiComponent comp : this.components) {
			if(comp instanceof Button) {
				if(button.id == ((Button)comp).id) ((Button) comp).onClick();
			}
		}
	}
	@Override
	public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub

		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		for(IGuiComponent comp : components) {
			
			if(comp instanceof GuiTextField) {
				((GuiTextField)comp).drawTextBox();;
			}
			if(comp instanceof GuiLabel) {
				((GuiLabel)comp).drawLabel(mc, mouseX, mouseY);
			}
			if(comp instanceof GuiSlider) {
				((GuiSlider)comp).drawButton(mc, mouseX, mouseY, partialTicks);
			}
			if(comp.canHaveTooltip() && isHovered(comp, mouseX, mouseY) && comp.isVisible()) {
				
				ArrayList<String> list = new ArrayList<String>();
				for(String s : comp.getTooltips()) {
					list.add(s);
				}
				if(!list.isEmpty())drawHoveringText(list, mouseX, mouseY);
			}
		}
		
		
	}
	private final boolean isHovered(IGuiComponent comp, int mouseX, int mouseY) {
		final int x = comp.getX();
		final int y = comp.getY();
		final int w = comp.getWidth();
		final int h = comp.getHeight();
		return (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h);
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		// TODO Auto-generated method stub
		for(IGuiComponent comp : components) {
			if(comp instanceof GuiTextField) {
				((GuiTextField)comp).mouseClicked(mouseX, mouseY, mouseButton);
			}
			if(comp instanceof GuiSlider) {
				((GuiSlider)comp).mousePressed(mc, mouseX, mouseY);
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(IGuiComponent comp : components) {
			if(comp instanceof GuiSlider) {
				((GuiSlider)comp).mouseReleased(mouseX, mouseY);
			}
		}
		super.mouseReleased(mouseX, mouseY, state);
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for(IGuiComponent comp : components) {
			if(comp instanceof GuiTextField) {
				((GuiTextField)comp).textboxKeyTyped(typedChar, keyCode);
			}
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	public final IGuiComponent getComponent(int index){
		return components.get(index);
	}
	public FontRenderer getFontRenderer() {
		// TODO Auto-generated method stub
		 return this.fontRenderer;
	}
	public final void openGui(GuiScreen gui) {
		mc.displayGuiScreen(gui);
	}

}
