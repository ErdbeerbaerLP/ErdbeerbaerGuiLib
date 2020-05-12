package de.erdbeerbaerlp.guilib.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

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

    protected void drawPanel(int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY, float partialTicks) {
        for (GuiComponent c : components) {
            c.scrollOffsetY = getY() - (int) scrollDistance;
            c.scrollOffsetX = getX();
            c.render(mouseX, mouseY, partialTicks);
        }
    }

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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = button == 0 && mouseX >= barLeft && mouseX < barLeft + barWidth;
        if (this.scrolling) {
            return true;
        }
        if (mouseX >= left && mouseX <= right) {
            for (GuiComponent comp : components) {
                if (comp.isVisible() && (isMouseInComponent(mouseX, mouseY, comp)) || comp instanceof TextField)
                    comp.mouseClicked(mouseX, mouseY, button);
            }
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        /*if (super.mouseReleased(mouseX, mouseY, state))
            return true;*/
        boolean ret = this.scrolling;
        this.scrolling = false;
        if (mouseX >= left && mouseX <= right) {
            for (GuiComponent comp : components) {
                if (comp.isVisible() && (isMouseInComponent(mouseX, mouseY, comp)) || comp instanceof TextField)
                    comp.mouseReleased(mouseX, mouseY, state);
            }
        }
        return ret;
    }

    @Override
    public void mouseRelease(double mouseX, double mouseY, int state) {
        //unused

    }

    @Override
    public final void mouseClick(double mouseX, double mouseY, int mouseButton) {
        //Unused
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
        for (GuiComponent comp : components) {
            comp.charTyped(typedChar, keyCode);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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
        return (mouseX >= comp.getX() - getX() && mouseY >= comp.getY() - getY() && mouseX < comp.getX() - getX() + comp.getWidth() && mouseY < comp.getY() - getY() + comp.getHeight());
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
     * @return Resource Location of Scroll Panel Background, defaults to {@link AbstractGui#BACKGROUND_LOCATION}
     */
    protected ResourceLocation getBackground() {
        return BACKGROUND_LOCATION;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldr = tess.getBuffer();
        double scale = mc.getMainWindow().getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (left * scale), (int) (mc.getMainWindow().getFramebufferHeight() - (bottom * scale)),
                (int) (width * scale), (int) (height * scale));
        if (this.mc.world != null) {
            this.drawGradientRect(this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
        } else // Draw dark dirt background
        {
            RenderSystem.disableLighting();
            RenderSystem.disableFog();
            this.mc.getTextureManager().bindTexture(getBackground());
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            final float texScale = 32.0F;
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(this.left, this.bottom, 0.0D).tex(this.left / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.right, this.bottom, 0.0D).tex(this.right / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.right, this.top, 0.0D).tex(this.right / texScale, (this.top + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.left, this.top, 0.0D).tex(this.left / texScale, (this.top + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            tess.draw();
        }

        int baseY = this.top + border - (int) this.scrollDistance;
        this.drawPanel(right, baseY, tess, mouseX, mouseY, partialTicks);

        RenderSystem.disableDepthTest();

        int extraHeight = (this.getContentHeight() + border) - height;
        if (extraHeight > 0) {
            int barHeight = getBarHeight();

            int barTop = (int) this.scrollDistance * (height - barHeight) / extraHeight + this.top;
            if (barTop < this.top) {
                barTop = this.top;
            }

            RenderSystem.disableTexture();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(barLeft, this.bottom, 0.0D).tex(0.0F, 1.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(barLeft + barWidth, this.bottom, 0.0D).tex(1.0F, 1.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(barLeft + barWidth, this.top, 0.0D).tex(1.0F, 0.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(barLeft, this.top, 0.0D).tex(0.0F, 0.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(barLeft, barTop + barHeight, 0.0D).tex(0.0F, 1.0F).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(barLeft + barWidth, barTop + barHeight, 0.0D).tex(1.0F, 1.0F).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(barLeft + barWidth, barTop, 0.0D).tex(1.0F, 0.0F).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(barLeft, barTop, 0.0D).tex(0.0F, 0.0F).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(barLeft, barTop + barHeight - 1, 0.0D).tex(0.0F, 1.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(barLeft + barWidth - 1, barTop + barHeight - 1, 0.0D).tex(1.0F, 1.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(barLeft + barWidth - 1, barTop, 0.0D).tex(1.0F, 0.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(barLeft, barTop, 0.0D).tex(0.0F, 0.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            tess.draw();
        }

        RenderSystem.enableTexture();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        GuiUtils.drawGradientRect(0, left, top, right, bottom, color1, color2);
    }
}