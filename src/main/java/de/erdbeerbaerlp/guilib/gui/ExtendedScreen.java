package de.erdbeerbaerlp.guilib.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import de.erdbeerbaerlp.guilib.components.GuiComponent;
import de.erdbeerbaerlp.guilib.components.TextField;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import org.jline.reader.Widget;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public abstract class ExtendedScreen extends Screen {
    private final List<GuiComponent> components = new ArrayList<>();
    private final int nextComponentID = 0;
    private int currentPage = 0;
    private Screen parentGui;

    /**
     * The constructor, you need to call this!
     *
     * @param parentGui The gui this was opened from. Can be null
     */
    public ExtendedScreen(Screen parentGui) {
        super(Component.literal("An GUI"));
        this.parentGui = parentGui;
        buildGui();
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

    /**
     * Gets called often to e.g. update components postions
     */
    public abstract void updateGui();

    @Override
    public boolean isPauseScreen() {
        return doesGuiPauseGame();
    }

    /**
     * Should this GUI pause Singleplayer game when open?
     */
    public abstract boolean doesGuiPauseGame();

    /**
     * Should pressing ESC close the GUI?
     */
    public abstract boolean doesEscCloseGui();

    /**
     * Open the next page of the GUI (if available)
     */
    public void nextPage() {
        if (currentPage < Integer.MAX_VALUE) currentPage++;
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
        if (page < Integer.MAX_VALUE)
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
     * Use this to add your components
     *
     * @param component The component to add
     * @param page      The page to register the component to
     */
    public final void addComponent(GuiComponent component, int page) {
        this.components.add(component);
        component.assignToPage(page);
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
     * Assigns a component to the page by calling comp.assignToPage(page)
     *
     * @param comp The component to assign
     * @param page The page for the component
     */
    public void assignComponentToPage(GuiComponent comp, int page) {
        comp.assignToPage(page);
    }

    @Override
    @Deprecated
    protected final <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T buttonIn) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    protected final void actionPerformed(Widget button) {
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            if (comp.isVisible() && mouseX >= comp.getX() && mouseY >= comp.getY() && mouseX < comp.getX() + comp.getWidth() && mouseY < comp.getY() + comp.getComponentHeight())
                comp.mouseScrolled(mouseX, mouseY, scroll);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            if (comp.isVisible())
                comp.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.keyReleased(keyCode, scanCode, modifiers);
        }
        return false;
    }

    private boolean unloadOnClose = true;

    public Screen getParentGui() {
        return parentGui;
    }

    public void setParentGui(Screen parentGui) {
        this.parentGui = parentGui;
    }

    @Override
    public final void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        updateGui();
        renderBackground(poseStack);
        for (final GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            if (comp.isVisible()) comp.render(poseStack, mouseX, mouseY, partialTicks);
        }
        //Second for to not have components overlap the tooltips
        for (final GuiComponent comp : Lists.reverse(components)) { //Reversing to call front component
            if (!comp.isVisible() || (comp.getAssignedPage() != -1 && comp.getAssignedPage() != currentPage)) continue;
            if (comp.canHaveTooltip() && isHovered(comp, mouseX, mouseY)) {

                final MutableComponent txt = Component.literal("");
                if (comp.getTooltips() != null) {
                    for (int i = 0; i < comp.getTooltips().length; i++) {
                        txt.append(Component.literal(comp.getTooltips()[i] + (i == comp.getTooltips().length - 1 ? "" : "\n")));
                    }
                }
                if (!txt.getSiblings().isEmpty()) {
                    renderTooltip(poseStack, minecraft.font.split(txt, Math.max(this.width / 2, 220)), mouseX, mouseY);
                    break;
                }
            }
        }


    }

    protected boolean forceDirtBackground() {
        return false;
    }

    /**
     * @return Resource Location of GUIs Background, defaults to {@link Screen#BACKGROUND_LOCATION}
     */
    protected ResourceLocation getBackground() {
        return BACKGROUND_LOCATION;
    }

    private boolean isHovered(GuiComponent comp, int mouseX, int mouseY) {
        if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) return false;
        final int x = comp.getX();
        final int y = comp.getY();
        final int w = comp.getWidth();
        final int h = comp.getComponentHeight();
        return (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h);
    }

    public final GuiComponent getComponent(int index) {
        return components.get(index);
    }

    /**
     * Opens an GUI
     *
     * @param gui GuiScreen or null
     */
    public final void openGui(Screen gui) {
        minecraft.setScreen(gui);
    }

    /**
     * Closes this GUI, returning to parent or null
     */
    public void close() {
        openGui(parentGui);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            if (comp.clicked(mouseX, mouseY) || comp instanceof TextField)
                comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
        return true;
    }

    public boolean charTyped(char character, int keyCode) {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.charTyped(character, keyCode);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.mouseRelease(mouseX, mouseY, mouseButton);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (keyCode == 256 && doesEscCloseGui()) {
            //noinspection RedundantCast
            this.minecraft.setScreen((Screen) null);

            if (this.minecraft.screen == null) {
                this.minecraft.setWindowActive(true);
            }
        }
        for (GuiComponent comp : components) {
            if (comp.getAssignedPage() != -1) if (comp.getAssignedPage() != currentPage) continue;
            comp.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
        }
        return true;
    }

    /**
     * Override to draw a custom background
     */
    public void renderBackground(PoseStack poseStack) {
        if (this.minecraft.level != null && !forceDirtBackground()) {
            this.fillGradient(poseStack, 0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            float f = 32.0F;
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(0.0D, this.height, 0.0D).uv(0.0F, (float) this.height / 32.0F + (float) 0).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex(this.width, this.height, 0.0D).uv((float) this.width / 32.0F, (float) this.height / 32.0F + (float) 0).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex(this.width, 0.0D, 0.0D).uv((float) this.width / 32.0F, (float) 0).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, 0.0D).uv(0.0F, (float) 0).color(64, 64, 64, 255).endVertex();
            tesselator.end();
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, poseStack));
        }
    }

    private BooleanConsumer confirmCallback;

    @Override
    public boolean handleComponentClicked(Style style) {
        return super.handleComponentClicked(style);
    }

    @Override
    public void onClose() {
        if (unloadOnClose)
            for (GuiComponent comp : components) {
                comp.unload();
            }
    }

    private void confirmed(boolean confirmed) {
        confirmCallback.accept(confirmed);
        openGui(ExtendedScreen.this);
        confirmCallback = null;
        unloadOnClose = true;
    }

    /**
     * Prompts the user to open an url
     */
    public final void openURL(String URL) {
        confirmCallback = (b) -> runUrl(b, URL);
        unloadOnClose = false;
        final ConfirmLinkScreen s = new ConfirmLinkScreen(this::confirmed, URL, true);
        openGui(s);
    }

    /**
     * Callback for URL opening
     *
     * @param bool
     * @param URL
     */
    private void runUrl(boolean bool, String URL) {
        if (bool)
            Util.getPlatform().openUri(URL);
    }

    /**
     * Creates a yes/no confirmation screen
     *
     * @param title    Title of the confirmation
     * @param text     Text of the confirmation
     * @param callback Callback to execute on confirmation
     */
    public final void openYesNo(String title, String text, final BooleanConsumer callback) {
        confirmCallback = callback;
        unloadOnClose = false;
        final ConfirmScreen s = new ConfirmScreen(this::confirmed, Component.literal(title), Component.literal(text));
        openGui(s);
    }

    /**
     * Creates a custom confirmation screen
     *
     * @param title       Title of the confirmation
     * @param text        Text of the confirmation
     * @param trueButton  Text of the button that returns true
     * @param falseButton Text of the button that returns false
     * @param callback    Callback to execute on confirmation
     */
    public final void openConfirmation(String title, String text, String trueButton, String falseButton, final BooleanConsumer callback) {
        confirmCallback = callback;
        unloadOnClose = false;
        final ConfirmScreen s = new ConfirmScreen(this::confirmed, Component.literal(title), Component.literal(text), Component.literal(trueButton), Component.literal(falseButton));
        openGui(s);
    }
}
