package de.erdbeerbaerlp.guilib.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.erdbeerbaerlp.guilib.components.IGuiComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BetterGuiScreen extends GuiScreen {
	private final List<IGuiComponent> components;
	private int nextComponentID = 0;
	public BetterGuiScreen() {
		components =  new ArrayList<IGuiComponent>();
		components.clear();
		buildGui();
		initGui();
	}
	/**
	 * Add your components here!
	 */
	public final void initGui() {
		
	};
	public abstract void buildGui();
	@Override
	public final void updateScreen() {
		updateGui();
		for(IGuiComponent comp : components) {
			comp.updateComponent();
		}

	}
	public abstract void updateGui();
	@Override
	public abstract boolean doesGuiPauseGame();
	public abstract boolean doesEscCloseGui();
	/**
	 * Use this to add your components
	 * @param component
	 */
	public final void addComponent(IGuiComponent component) {
		component.setID(nextComponentID);
		nextComponentID++;
		this.components.add(component);
	}
	/**
	 * Use this to add multiple components at once
	 * @param component
	 */
	public final void addAllComponents(IGuiComponent...components) {
		for(IGuiComponent c : components) {
			this.addComponent(c);
		}
	}
	@Override
	@Deprecated
	/**
	 * Use addComponent instead!!!<br>This method does no longer do anything!
	 */
	protected final <T extends GuiButton> T addButton(T buttonIn) {return null;}
	protected final void actionPerformed(GuiButton button) throws IOException {}
	@Override
	public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
		for(final IGuiComponent comp : components) {
			comp.draw(mouseX, mouseY, partialTicks);
		}
		//Second for to not have components overlap the tooltips
		for(final IGuiComponent comp : components) {
			if(comp.canHaveTooltip() && isHovered(comp, mouseX, mouseY) && comp.isVisible()) {

				final ArrayList<String> list = new ArrayList<String>();
				if(comp.getTooltips() != null) {
					for(String s : comp.getTooltips()) {
						list.add(s);
					}
				}
				if(!list.isEmpty()) drawHoveringText(list, mouseX, mouseY);
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


	public final IGuiComponent getComponent(int index){
		return components.get(index);
	}
	public FontRenderer getFontRenderer() {

		return this.fontRenderer;
	}
	public final void openGui(GuiScreen gui) {
		if(gui == null) mc.displayGuiScreen((GuiScreen)null);
		mc.displayGuiScreen(gui);
	}
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		for(IGuiComponent comp : components) {
			comp.mouseClick(mouseX, mouseY, mouseButton);
		}
	}
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

		for(IGuiComponent comp : components) {
			comp.mouseReleased(mouseX, mouseY, state);
		}
	}
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {

		for(IGuiComponent comp : components) {
			comp.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

		for(IGuiComponent comp : components) {
			comp.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}
	@Override
	public void handleMouseInput() throws IOException {
		for(IGuiComponent comp : components) {
			comp.handleMouseInput();
		}
		super.handleMouseInput();
	}
	@Override
	public void handleKeyboardInput() throws IOException {
		for(IGuiComponent comp : components) {
			comp.handleKeyboardInput();
		}
		super.handleKeyboardInput();
	}
}
