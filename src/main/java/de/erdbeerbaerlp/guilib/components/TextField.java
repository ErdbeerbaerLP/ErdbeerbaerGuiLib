package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatAllowedCharacters;

public class TextField extends GuiTextField implements IGuiComponent{
	private String[] tooltip = new String[0];
	private boolean acceptsColors = false;
	private Runnable callback;
	public TextField(int x, int y, int width, int height) {
		super(-1, Minecraft.getMinecraft().fontRenderer, x, y, width, height);
		setMaxStringLength(100);
	}
	public TextField(int x, int y, int width) {
		this(x, y, width, 20);
	}
	
	final char colorCodePlaceholder = '\u0378';
	private boolean enabled;
	@Override
	public void writeText(String textToWrite) {
        String s = "";
        if(acceptsColors) {
        	textToWrite = textToWrite.replace('\u00A7', colorCodePlaceholder);
        }
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        if(acceptsColors) {
        	s1 = s1.replace(colorCodePlaceholder,'\u00A7');
        }
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.text.length() - (i - j);

        if (!this.text.isEmpty())
        {
            s = s + this.text.substring(0, i);
        }

        int l;

        if (k < s1.length())
        {
            s = s + s1.substring(0, k);
            l = k;
        }
        else
        {
            s = s + s1;
            l = s1.length();
        }

        if (!this.text.isEmpty() && j < this.text.length())
        {
            s = s + this.text.substring(j);
        }

        if (this.validator.apply(s))
        {
            this.text = s;
            this.moveCursorBy(i - this.selectionEnd + l);
            this.setResponderEntryValue(this.id, this.text);
        }
	}
    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    public final boolean textboxKeyTyped(char typedChar, int keyCode)
    {
        if (!this.isFocused)
        {
            return false;
        }
        else if (GuiScreen.isKeyComboCtrlA(keyCode))
        {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        }
        else if (GuiScreen.isKeyComboCtrlC(keyCode))
        {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        }
        else if (GuiScreen.isKeyComboCtrlV(keyCode))
        {
            if (this.isEnabled)
            {
                this.writeText(GuiScreen.getClipboardString());
            }

            return true;
        }
        else if (GuiScreen.isKeyComboCtrlX(keyCode))
        {
            GuiScreen.setClipboardString(this.getSelectedText());

            if (this.isEnabled)
            {
                this.writeText("");
            }

            return true;
        }
        else
        {
            switch (keyCode)
            {
                case 14:

                    if (GuiScreen.isCtrlKeyDown())
                    {
                        if (this.isEnabled)
                        {
                            this.deleteWords(-1);
                        }
                    }
                    else if (this.isEnabled)
                    {
                        this.deleteFromCursor(-1);
                    }

                    return true;
                case 199:

                    if (GuiScreen.isShiftKeyDown())
                    {
                        this.setSelectionPos(0);
                    }
                    else
                    {
                        this.setCursorPositionZero();
                    }

                    return true;
                case 203:

                    if (GuiScreen.isShiftKeyDown())
                    {
                        if (GuiScreen.isCtrlKeyDown())
                        {
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                        }
                        else
                        {
                            this.setSelectionPos(this.getSelectionEnd() - 1);
                        }
                    }
                    else if (GuiScreen.isCtrlKeyDown())
                    {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    }
                    else
                    {
                        this.moveCursorBy(-1);
                    }

                    return true;
                case 205:

                    if (GuiScreen.isShiftKeyDown())
                    {
                        if (GuiScreen.isCtrlKeyDown())
                        {
                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        }
                        else
                        {
                            this.setSelectionPos(this.getSelectionEnd() + 1);
                        }
                    }
                    else if (GuiScreen.isCtrlKeyDown())
                    {
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    }
                    else
                    {
                        this.moveCursorBy(1);
                    }

                    return true;
                case 207:

                    if (GuiScreen.isShiftKeyDown())
                    {
                        this.setSelectionPos(this.text.length());
                    }
                    else
                    {
                        this.setCursorPositionEnd();
                    }

                    return true;
                case 211:

                    if (GuiScreen.isCtrlKeyDown())
                    {
                        if (this.isEnabled)
                        {
                            this.deleteWords(1);
                        }
                    }
                    else if (this.isEnabled)
                    {
                        this.deleteFromCursor(1);
                    }

                    return true;
                case 28:
                	this.onReturn();
                	return true;
                default:
                	if(this.acceptsColors && typedChar == '\u00A7') {
                		typedChar = colorCodePlaceholder;
                	}
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
                    {
                    	if(this.acceptsColors && typedChar == colorCodePlaceholder) typedChar = '\u00A7';
                        if (this.isEnabled)
                        {
                            this.writeText(Character.toString(typedChar));
                        }

                        return true;
                    }
                    else
                    {
                        return false;
                    }
            }
        }
    }
    @Override
    public void draw(int mouseX, int mouseY, float partial) {
    	if(!visible) return;
    	this.drawTextBox();
    }
	@Override
	public final void drawTextBox() {
		super.drawTextBox();
	}
	public void setTooltips(String... strings) {
		this.tooltip = strings;
	}
	public void setAcceptsColors(boolean acceptsColors) {
		this.acceptsColors = acceptsColors;
	}
	public void setWidth(int width) {
		
		this.width = width;
	}
	public void onReturn() {
		if(this.callback != null) this.callback.run();
	}
	public final void setReturnAction(Runnable r) {
		this.callback = r;
	}
	@Override
	public void setID(int id) {
		this.id = id;
	}
	@Override
	public String[] getTooltips() {
		return this.tooltip;
	}
	@Override
	public boolean isVisible() {
		
		return this.getVisible();
	}
	@Override
	public int getX() {
		
		return x;
	}

	@Override
	public int getY() {
		
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		
		return height;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		
		
	}
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		
		textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		
		
	}
	@Override
	public void mouseClick(int mouseX, int mouseY, int mouseButton) {
		
		mouseClicked(mouseX, mouseY, mouseButton);
		
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		this.visible = visible;
	}

	@Override
	public void setEnabled(boolean enable) {
		// TODO Auto-generated method stub
		this.enabled = enable;
	}
	private int page = -1;
	@Override
	public void assignToPage(int page) {
		this.page = page;
	}

	@Override
	public int getAssignedPage() {
		return this.page;
	}
}
