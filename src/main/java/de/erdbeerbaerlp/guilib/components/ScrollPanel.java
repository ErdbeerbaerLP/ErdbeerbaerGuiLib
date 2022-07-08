package de.erdbeerbaerlp.guilib.components;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;

public class ScrollPanel extends GuiComponent {
    protected final int border = 4;
    private final int barWidth = 6;
    private final ArrayList<GuiComponent> components = new ArrayList<>();
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    protected float scrollDistance;
    protected boolean captureMouse = true;
    private ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("minecraft", "textures/gui/options_background.png");
    private int barLeft;
    private boolean scrolling;
    private int contentHeight;

    public ScrollPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.contentHeight = height;
        recalc();
    }

    private void recalc() {
        this.top = getY();
        this.left = getX();
        this.bottom = height + this.top;
        this.right = width + this.left;
        this.barLeft = this.left + this.getWidth() - barWidth;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        recalc();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        recalc();
    }

    public void setBackgroundTexture(ResourceLocation bg) {
        BACKGROUND_LOCATION = bg;
    }

    @Override
    public void unload() {
        for (GuiComponent c : components)
            c.unload();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        recalc();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        recalc();
    }

    protected int getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
        recalc();
    }

    /**
     * Use this to add your components
     *
     * @param component The component to add
     */
    public final void addComponent(GuiComponent component) {
        this.components.add(component);
    }

    protected void drawBackground() {
    }

    private final ArrayList<String> tooltips = new ArrayList<>();

    private int getMaxScroll() {
        return this.getContentHeight() - (this.height - this.border);
    }

    private void applyScrollLimits() {
        int max = getMaxScroll();

        if (max < 0) {
            max /= 2;
        }

        if (this.scrollDistance < 0.0F) {
            this.scrollDistance = 0.0F;
        }

        if (this.scrollDistance > max) {
            this.scrollDistance = max;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (scroll != 0) {
            this.scrollDistance += -scroll * getScrollAmount();
            applyScrollLimits();
            return true;
        }
        return false;
    }

    protected int getScrollAmount() {
        return 20;
    }

    protected void drawPanel(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        for (final GuiComponent c : components) {
            c.scrollOffsetY = this.getY() - (int) scrollDistance;
            c.scrollOffsetX = this.getX();
            if (c.isVisible()) c.render(poseStack, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * Resizes the content height to just perfectly fit all components
     */
    public void fitContent() {
        for (final GuiComponent c : components) {
            if (((c.getY() - c.scrollOffsetY) + c.getComponentHeight() + 10) > contentHeight) {
                contentHeight = (c.getY() - c.scrollOffsetY) + c.getComponentHeight() + 10;
            }
        }
    }

    @Override
    public boolean canHaveTooltip() {
        return true;
    }

    @Override
    public void mouseRelease(double mouseX, double mouseY, int state) {
        this.scrolling = false;
        if (mouseX >= left && mouseX <= right) {
            for (GuiComponent comp : components) {
                if (comp.isVisible())
                    comp.mouseReleased(mouseX, mouseY, state);
            }
        }
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

    @Override
    public boolean charTyped(char typedChar, int keyCode) {

        if (isEnabled())
            for (GuiComponent comp : components) {
                comp.charTyped(typedChar, keyCode);
            }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isEnabled())
            for (GuiComponent comp : components) {
                comp.keyPressed(keyCode, scanCode, modifiers);
            }
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (GuiComponent comp : components) {
            comp.keyReleased(keyCode, scanCode, modifiers);
        }
        return true;
    }

    private boolean isMouseInComponent(double mouseX, double mouseY, GuiComponent comp) {
        return (mouseX >= comp.getX() && mouseY >= comp.getY() && mouseX < comp.getX() + comp.getWidth() && mouseY < comp.getY() + comp.getComponentHeight());
    }

    private int getBarHeight() {
        int barHeight = (height * height) / this.getContentHeight();

        if (barHeight < 32) barHeight = 32;

        if (barHeight > height - border * 2)
            barHeight = height - border * 2;

        return barHeight;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isEnabled())
            if (this.scrolling) {
                int maxScroll = height - getBarHeight();
                double moved = deltaY / maxScroll;
                this.scrollDistance += getMaxScroll() * moved;
                applyScrollLimits();
                return true;
            }
        return false;
    }

    /**
     * @return Resource Location of Scroll Panel Background, defaults to {@link net.minecraft.client.gui.screens.Screen#BACKGROUND_LOCATION}
     */
    protected ResourceLocation getBackground() {
        return BACKGROUND_LOCATION;
    }

    @Override
    public final void mouseClick(double mouseX, double mouseY, int mouseButton) {
        if (isEnabled()) {
            this.scrolling = mouseButton == 0 && mouseX >= barLeft && mouseX < barLeft + barWidth;
            if (this.scrolling) {
                return;
            }
            if (mouseX >= left && mouseX <= right) {
                for (GuiComponent comp : components) {
                    if (comp.isVisible() && (isMouseInComponent(mouseX, mouseY, comp)) || comp instanceof TextField)
                        comp.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder worldr = tess.getBuilder();
        double scale = mc.getWindow().getGuiScale();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (left * scale), (int) (mc.getWindow().getHeight() - (bottom * scale)),
                (int) (width * scale), (int) (height * scale));
        if (this.mc.level != null) {
            this.drawGradientRect(poseStack, this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
        } else // Draw dark dirt background
        {
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, getBackground());
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            final float texScale = 32.0F;
            worldr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            worldr.vertex(this.left, this.bottom, 0.0D).uv(this.left / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.vertex(this.right, this.bottom, 0.0D).uv(this.right / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.vertex(this.right, this.top, 0.0D).uv(this.right / texScale, (this.top + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.vertex(this.left, this.top, 0.0D).uv(this.left / texScale, (this.top + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            tess.end();
        }

        this.drawPanel(poseStack, mouseX, mouseY, partialTicks);

        RenderSystem.disableDepthTest();

        int extraHeight = (this.getContentHeight() + border) - height;
        if (extraHeight > 0) {
            int barHeight = getBarHeight();

            int barTop = (int) this.scrollDistance * (height - barHeight) / extraHeight + this.top;
            if (barTop < this.top) {
                barTop = this.top;
            }
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            worldr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            worldr.vertex(barLeft, this.bottom, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            worldr.vertex(barLeft + barWidth, this.bottom, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            worldr.vertex(barLeft + barWidth, this.top, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            worldr.vertex(barLeft, this.top, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            worldr.vertex(barLeft, barTop + barHeight, 0.0D).uv(0.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            worldr.vertex(barLeft + barWidth, barTop + barHeight, 0.0D).uv(1.0F, 1.0F).color(128, 128, 128, 255).endVertex();
            worldr.vertex(barLeft + barWidth, barTop, 0.0D).uv(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            worldr.vertex(barLeft, barTop, 0.0D).uv(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            worldr.vertex(barLeft, barTop + barHeight - 1, 0.0D).uv(0.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            worldr.vertex(barLeft + barWidth - 1, barTop + barHeight - 1, 0.0D).uv(1.0F, 1.0F).color(192, 192, 192, 255).endVertex();
            worldr.vertex(barLeft + barWidth - 1, barTop, 0.0D).uv(1.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            worldr.vertex(barLeft, barTop, 0.0D).uv(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            tess.end();
        }

        RenderSystem.enableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        for (final GuiComponent comp : Lists.reverse(components)) { //Reversing to call front component
            tooltips.clear();
            if (!comp.isVisible()) continue;
            if (comp.canHaveTooltip() && isMouseInComponent(mouseX, mouseY, comp)) {

                if (comp.getTooltips() != null) {
                    tooltips.addAll(Arrays.asList(comp.getTooltips()));
                    break;
                }
            }
        }

    }

    @Override
    public String[] getTooltips() {
        return tooltips.toArray(new String[0]);
    }

    protected void drawGradientRect(PoseStack poseStack, int left, int top, int right, int bottom, int color1, int color2) {
        GuiUtils.drawGradientRect(poseStack.last().pose(), 0, left, top, right, bottom, color1, color2);
    }
}