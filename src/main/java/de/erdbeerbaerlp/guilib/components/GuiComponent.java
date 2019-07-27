package de.erdbeerbaerlp.guilib.components;

import de.erdbeerbaerlp.guilib.McMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("unused")
public abstract class GuiComponent extends Gui {
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    protected final FontRenderer fontRenderer;
    public int packedFGColour; //FML  (what does this do?)
    protected Minecraft mc = Minecraft.getMinecraft();
    protected int width;
    protected int height;
    protected int id;
    protected boolean hovered; //Sometimes used by components
    protected boolean visible = true, enabled = true;
    protected static final ResourceLocation errorIcon = new ResourceLocation(McMod.MODID, "textures/gui/imgerror.png");
    private int x;
    private int y;
    private String[] tooltips = new String[0];
    private int assignedPage = -1;

    protected GuiComponent(int x, int y, int width, int height) {
        this.fontRenderer = mc.fontRenderer;
        this.setX(x);
        this.setY(y);
        this.width = width;
        this.height = height;
    }

    /**
     * This function resizes the image and returns the BufferedImage object that can be drawn
     */
    final BufferedImage scaleImage(final BufferedImage img, int width, int height) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(width, height, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, width, height, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    final BufferedImage loadImageFromURL(String url) throws IOException {
        final HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
        httpcon.addRequestProperty("User-Agent", "Minecraft");
        final BufferedImage img = ImageIO.read(httpcon.getInputStream());
        httpcon.disconnect();
        return img;
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

    /**
     * @return Is this component visible?
     */
    public final boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the components visibility state
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
        return x;
    }

    /**
     * Sets the component´s X position
     * @param x X position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return Y position
     */
    public final int getY() {
        return y;
    }

    /**
     * Sets the component´s Y position
     * @param y Y position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the component width
     * @return width
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Sets the component width
     * @param width Width
     */
    public final void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the component height
     * @return height
     */
    public final int getHeight() {
        return height;
    }

    /**
     * Sets the component height
     * @param height Height
     */
    public final void setHeight(int height) {
        this.height = height;
    }

    /**
     * Plays a press sound
     */
    public void playPressSound() {
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, this.enabled ? 1.0F : 0.5f));
    }

    /**
     * Draws the component
     */
    public abstract void draw(int mouseX, int mouseY, float partial);

    /**
     * Called on mouse click
     */
    public abstract void mouseClick(int mouseX, int mouseY, int mouseButton);

    /**
     * Called on mouse release
     */
    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public abstract void keyTyped(char typedChar, int keyCode);

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    public abstract void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);

    /**
     * Updates the component on gui update
     */
    public void updateComponent() {

    }

    public void handleMouseInput() {

    }

    public void handleKeyboardInput() {

    }

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
     * @param page Page to assign to
     */
    public final void assignToPage(int page) {
        assignedPage = page;
    }

    /**
     * Gets the page this component is assigned to
     * @return Assigned page
     */
    public final int getAssignedPage() {
        return assignedPage;
    }

    /**
     * Sets the ID of this component
     * (almost unused!)
     * @param id ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the position of this component
     * @param x X position
     * @param y Y position
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

}
