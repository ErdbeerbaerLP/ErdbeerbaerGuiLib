package de.erdbeerbaerlp.guilib.gui;

import com.google.common.collect.Lists;
import de.erdbeerbaerlp.guilib.components.GuiComponent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public abstract class BetterGuiScreen extends GuiScreen {
    private final List<GuiComponent> components = new ArrayList<>();
    private int nextComponentID = 0;
    private int pages = 0;
    private int currentPage = 0;

    /**
     * The constructor, you need to call this!
     */
    public BetterGuiScreen() {
        buildGui();
    }

    public final void initGui() {
    }

    /**
     * Gets current open page
     *
     * @return current page
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Add your components here!
     */
    public abstract void buildGui();

    @Override
    public final void updateScreen() {
        updateGui();
        for (GuiComponent comp : components) {
            comp.updateComponent();
        }

    }

    /**
     * Gets called often to e.g. update components postions
     */
    public abstract void updateGui();

    @Override
    public abstract boolean doesGuiPauseGame();

    /**
     * Should pressing ESC close the GUI?
     */
    public abstract boolean doesEscCloseGui();

    /**
     * Open the next page of the GUI (if available)
     */
    public void nextPage() {
        if (currentPage < pages) currentPage++;
    }

    /**
     * Open the previous page of the GUI (if available)
     */
    public void prevPage() {
        if (currentPage > 0) currentPage--;
    }

    /**
     * Sets the gui to a specific Page (if it is available!)
     *
     * @param page Page to set the GUI to
     */
    public void setPage(int page) {
        if (page < pages)
            this.currentPage = page;
    }

    /**
     * Use this to add your components
     *
     * @param component The component to add
     */
    public final void addComponent(GuiComponent component) {
        this.components.add(component);
    }

    /**
     * Use this to add multiple components at once
     *
     * @param components The components to add
     */
    public final void addAllComponents(GuiComponent... components) {
        for (GuiComponent c : components) {
            this.addComponent(c);
        }
    }

    /**
     * Sets the amount of pages this GUI has
     * @param pages Amount of pages
     */
    public void setAmountOfPages(int pages) {
        this.pages = pages;
    }

    /**
     * Assigns a component to the page by calling comp.assignToPage(page)
     * @param comp The component to assign
     * @param page The page for the component
     */
    public void assignComponentToPage(GuiComponent comp, int page) {
        comp.assignToPage(page);
    }

    @Override
    @Deprecated
    protected final <T extends GuiButton> T addButton(T buttonIn) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    protected final void actionPerformed(GuiButton button) {
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawBackground();
        for (final GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.draw(mouseX, mouseY, partialTicks);
        }
        //Second for to not have components overlap the tooltips
        for (final GuiComponent comp : Lists.reverse(components)) { //Reversing to call front component
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            if (comp.canHaveTooltip() && isHovered(comp, mouseX, mouseY) && comp.isVisible()) {

                final ArrayList<String> list = new ArrayList<>();
                if (comp.getTooltips() != null) {
                    list.addAll(Arrays.asList(comp.getTooltips()));
                }
                if (!list.isEmpty()) {
                    drawHoveringText(list, mouseX, mouseY);
                    break;
                }
            }
        }


    }

    /**
     * Override to draw a custom background
     */
    protected void drawBackground() {
        if (this.mc.world != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(0.0D, (double) this.height, 0.0D).tex(0.0D, (double) ((float) this.height / 32.0F + (float) 0)).color(64, 64, 64, 255).endVertex();
            bufferbuilder.pos((double) this.width, (double) this.height, 0.0D).tex((double) ((float) this.width / 32.0F), (double) ((float) this.height / 32.0F + (float) 0)).color(64, 64, 64, 255).endVertex();
            bufferbuilder.pos((double) this.width, 0.0D, 0.0D).tex((double) ((float) this.width / 32.0F), (double) 0).color(64, 64, 64, 255).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) 0).color(64, 64, 64, 255).endVertex();
            tessellator.draw();
        }
    }

    private boolean isHovered(GuiComponent comp, int mouseX, int mouseY) {
        if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) return false;
        final int x = comp.getX();
        final int y = comp.getY();
        final int w = comp.getWidth();
        final int h = comp.getHeight();
        return (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h);
    }

    public final GuiComponent getComponent(int index) {
        return components.get(index);
    }

    /**
     * Opens an GUI
     * @param gui GuiScreen or null
     */
    public final void openGui(GuiScreen gui) {
        if (gui == null) mc.displayGuiScreen(null);
        else mc.displayGuiScreen(gui);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)  {

        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.mouseClick(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 && doesEscCloseGui()) {
            //noinspection RedundantCast
            this.mc.displayGuiScreen((GuiScreen) null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.handleMouseInput();
        }
        super.handleMouseInput();
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.handleKeyboardInput();
        }
        super.handleKeyboardInput();
    }
}
