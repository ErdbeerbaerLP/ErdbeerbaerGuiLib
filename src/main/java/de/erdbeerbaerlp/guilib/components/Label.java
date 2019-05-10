package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
//GuiLabel
public class Label extends GuiComponent{
	private final List<String> labels;
    private boolean centered;
    public boolean visible = true;
    private boolean labelBgEnabled;
    private final int textColor;
    private int backColor;
    private int ulColor;
    private int brColor;
    private int border;
	
	public Label(int x, int y, int width, int height, int color) {
		super(x, y, width, height);

        this.labels = Lists.<String>newArrayList();
		this.textColor = color;
		this.setX(x);
		this.setY(y);
		this.visible = true;
	}
	public Label(int x, int y) {
		this(x, y, 150, 20, -1);
	}
	public Label(String text, int x, int y) {
		this(x, y);
		this.addLine(text);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partial) {
		if(!visible) return;
		this.drawLabel(Minecraft.getMinecraft(), mouseX, mouseY);
	}
	 public void addLine(String p_175202_1_)
	    {
	        this.labels.add(I18n.format(p_175202_1_));
	    }

	    /**
	     * Sets the Label to be centered
	     */
	    public Label setCentered()
	    {
	        this.centered = true;
	        return this;
	    }

	    public void drawLabel(Minecraft mc, int mouseX, int mouseY)
	    {
	        if (this.visible)
	        {
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            this.drawLabelBackground(mc, mouseX, mouseY);
	            int i = this.getY() + this.height / 2 + this.border / 2;
	            int j = i - this.labels.size() * 10 / 2;

	            for (int k = 0; k < this.labels.size(); ++k)
	            {
	                if (this.centered)
	                {
	                    this.drawCenteredString(this.fontRenderer, this.labels.get(k), this.getX() + this.width / 2, j + k * 10, this.textColor);
	                }
	                else
	                {
	                    this.drawString(this.fontRenderer, this.labels.get(k), this.getX(), j + k * 10, this.textColor);
	                }
	            }
	        }
	    }

	    protected void drawLabelBackground(Minecraft mcIn, int p_146160_2_, int p_146160_3_)
	    {
	        if (this.labelBgEnabled)
	        {
	            int i = this.width + this.border * 2;
	            int j = this.height + this.border * 2;
	            int k = this.getX() - this.border;
	            int l = this.getY() - this.border;
	            drawRect(k, l, k + i, l + j, this.backColor);
	            this.drawHorizontalLine(k, k + i, l, this.ulColor);
	            this.drawHorizontalLine(k, k + i, l + j, this.brColor);
	            this.drawVerticalLine(k, l, l + j, this.ulColor);
	            this.drawVerticalLine(k + i, l, l + j, this.brColor);
	        }
	    }
	@Override
	public void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException {
		
	}
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		
		
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		
		
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		
		
	}
}
