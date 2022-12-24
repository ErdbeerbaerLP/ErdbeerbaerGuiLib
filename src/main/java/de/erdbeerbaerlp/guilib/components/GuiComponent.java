package de.erdbeerbaerlp.guilib.components;

import com.mojang.blaze3d.vertex.PoseStack;
import de.erdbeerbaerlp.guilib.McMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

@SuppressWarnings("unused")
public abstract class GuiComponent extends AbstractWidget {
    protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    protected static final ResourceLocation errorIcon = new ResourceLocation(McMod.MODID, "textures/gui/imgerror.png");
    protected final Font renderer;
    protected Minecraft mc = Minecraft.getInstance();
    protected int id;
    protected boolean hovered; //Sometimes used by components
    protected boolean visible = true, enabled = true;
    int scrollOffsetX = 0, scrollOffsetY = 0;
    private String[] tooltips = new String[0];
    private int assignedPage = -1;

    public GuiComponent(int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn, Component.literal(""));
        this.renderer = Minecraft.getInstance().font;
        this.setX(xIn);
        this.setY(yIn);
        this.width = widthIn;
        this.height = heightIn;
    }


    /**
     * @return can the component have a tooltip?
     */
    public boolean canHaveTooltip() {
        return true;
    }

    /**
     * Gets tooltips assigned to the component
     *
     * @return tooltips
     */
    public String[] getTooltips() {
        return this.tooltips;
    }

    /**
     * Sets the tooltip(s) of this component
     *
     * @param strings tooltip(s)
     */
    public final void setTooltips(String... strings) {
        this.tooltips = strings;
    }

    protected int getYImage(boolean isHovered) {
        int i = 1;
        if (!this.isEnabled()) {

            i = 0;
        } else if (isHovered) {
            i = 2;
        }

        return i;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return true;
    }

    /**
     * @return Is this component visible?
     */
    public final boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the components visibility state
     *
     * @param visible visible?
     */
    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return is component enabled?
     */
    public final boolean isEnabled() {
        return this.enabled;
    }

    /**
     * @param enable should it be enabled?
     */
    public final void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    /**
     * @return X position
     */
    public final int getX() {
        return getX() + scrollOffsetX;
    }

    /**
     * Sets the component´s X position
     *
     * @param x X position
     */
    public void setX(int x) {
        this.setX(x);
    }

    /**
     * @return Y position
     */
    public final int getY() {
        return getY() + scrollOffsetY;
    }

    /**
     * Sets the component´s Y position
     *
     * @param y Y position
     */
    public void setY(int y) {
        this.setY(y);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    /**
     * Gets the component width
     *
     * @return width
     */
    @Override
    public final int getWidth() {
        return width;
    }

    /**
     * Sets the component width
     *
     * @param width Width
     */
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public final int getHeight() {
        return height;
    }

    /**
     * Gets the component height
     *
     * @return height
     */
    public final int getComponentHeight() {
        return getHeight();
    }

    /**
     * Sets the component height
     *
     * @param height Height
     */
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Plays a press sound
     */
    public void playPressSound() {
        mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, enabled ? 1f : 0.5f));
    }

    /**
     * Draws the component
     */
    @Override
    public abstract void render(PoseStack poseStack, int mouseX, int mouseY, float partial);

    /**
     * Called on mouse click
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseClick(mouseX, mouseY, button);
        return true;
    }

    public abstract void mouseClick(double mouseX, double mouseY, int button);

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        return isVisible() && ((mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getComponentHeight()));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        this.mouseRelease(mouseX, mouseY, state);
        return true;
    }

    /**
     * Called on mouse release
     */
    public abstract void mouseRelease(double mouseX, double mouseY, int state);

    /**
     * Fired when a key is typed (except F11 which toggles full screen)
     */
    public abstract boolean charTyped(char typedChar, int keyCode);


    /**
     * Disables this component
     */
    public final void disable() {
        setEnabled(false);
    }

    /**
     * Enables this component
     */
    public final void enable() {
        setEnabled(true);
    }

    /**
     * Hides this component
     */
    public final void hide() {
        setVisible(false);
    }

    /**
     * Shows this component
     */
    public final void show() {
        setVisible(true);
    }

    /**
     * Assigns this component to an page
     * -1 means global
     * Default: -1
     *
     * @param page Page to assign to
     */
    public final void assignToPage(int page) {
        assignedPage = page;
    }

    /**
     * Gets the page this component is assigned to
     *
     * @return Assigned page
     */
    public final int getAssignedPage() {
        return assignedPage;
    }

    /**
     * Sets the ID of this component
     * (almost unused!)
     *
     * @param id ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the position of this component
     *
     * @param x X position
     * @param y Y position
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Gets called on GUI Close, used to stop threads and/or free memory
     */
    public void unload() {

    }
}
