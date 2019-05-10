package de.erdbeerbaerlp.guilib.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.erdbeerbaerlp.guilib.components.GuiComponent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BetterGuiScreen extends GuiScreen {
	private final List<GuiComponent> components;
	private int nextComponentID = 0;
	private int pages = 0;
	private int currentPage = 0;
	public BetterGuiScreen() {
		components =  new ArrayList<GuiComponent>();
		components.clear();
		buildGui();
		initGui();
	}
	/**
	 * Add your components here!
	 */
	public final void initGui() {
		
	};
	public int getCurrentPage() {
		return currentPage;
	}
	public abstract void buildGui();
	@Override
	public final void updateScreen() {
		updateGui();
		for(GuiComponent comp : components) {
			comp.updateComponent();
		}

	}
	public abstract void updateGui();
	@Override
	public abstract boolean doesGuiPauseGame();
	public abstract boolean doesEscCloseGui();
	public void nextPage() {
		if(currentPage < pages) currentPage++; 
	}
	public void prevPage() {
		if(currentPage > 0) currentPage--; 
	}
	public void setPage(int page){
		this.currentPage = page;
	}
	/**
	 * Use this to add your components
	 * @param component
	 */
	public final void addComponent(GuiComponent component) {
		component.setId(nextComponentID);
		nextComponentID++;
		this.components.add(component);
	}
	/**
	 * Use this to add multiple components at once
	 * @param component
	 */
	public final void addAllComponents(GuiComponent...components) {
		for(GuiComponent c : components) {
			this.addComponent(c);
		}
	}
	public void setAmountOfPages(int pages) {
		this.pages = pages;
	}
	public void assignComponentToPage(GuiComponent comp, int page) {
		comp.assignToPage(page);
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
		drawBackground();
		for(final GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1) if(comp.getAssignedPage() != currentPage) continue;
			comp.draw(mouseX, mouseY, partialTicks);
		}
		//Second for to not have components overlap the tooltips
		for(final GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1) if(comp.getAssignedPage() != currentPage) continue;
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
	protected void drawBackground() {
        if (this.mc.world != null)
        {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        }
        else
        {
        	GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(0.0D, (double)this.height, 0.0D).tex(0.0D, (double)((float)this.height / 32.0F + (float)0)).color(64, 64, 64, 255).endVertex();
            bufferbuilder.pos((double)this.width, (double)this.height, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)this.height / 32.0F + (float)0)).color(64, 64, 64, 255).endVertex();
            bufferbuilder.pos((double)this.width, 0.0D, 0.0D).tex((double)((float)this.width / 32.0F), (double)0).color(64, 64, 64, 255).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)0).color(64, 64, 64, 255).endVertex();
            tessellator.draw();
        }
	}
	private final boolean isHovered(GuiComponent comp, int mouseX, int mouseY) {
		if(comp.getAssignedPage() != -1 ) if(comp.getAssignedPage() != currentPage) return false;
		final int x = comp.getX();
		final int y = comp.getY();
		final int w = comp.getWidth();
		final int h = comp.getHeight();
		return (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h);
	}
	public final GuiComponent getComponent(int index){
		return components.get(index);
	}
	public final void openGui(GuiScreen gui) {
		if(gui == null) mc.displayGuiScreen((GuiScreen)null);
		mc.displayGuiScreen(gui);
	}
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		for(GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1) if(comp.getAssignedPage() != currentPage) continue;
			comp.mouseClick(mouseX, mouseY, mouseButton);
		}
	}
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

		for(GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1) if(comp.getAssignedPage() != currentPage) continue;
			comp.mouseReleased(mouseX, mouseY, state);
		}
	}
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {

		for(GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1 ) if(comp.getAssignedPage() != currentPage) continue;
			comp.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

		for(GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1 ) if(comp.getAssignedPage() != currentPage) continue;
			comp.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}
	@Override
	public void handleMouseInput() throws IOException {
		for(GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1 ) if(comp.getAssignedPage() != currentPage) continue;
			comp.handleMouseInput();
		}
		super.handleMouseInput();
	}
	@Override
	public void handleKeyboardInput() throws IOException {
		for(GuiComponent comp : components) {
			if(comp.getAssignedPage() != -1 ) if(comp.getAssignedPage() != currentPage) continue;
			comp.handleKeyboardInput();
		}
		super.handleKeyboardInput();
	}
}
