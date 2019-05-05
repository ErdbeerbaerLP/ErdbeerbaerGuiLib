package de.erdbeerbaerlp.guilib.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;

public interface IGuiComponent {
	Minecraft mc = Minecraft.getMinecraft();
	
	
	void setID(int id);
	default boolean canHaveTooltip() {
		return true;
	}
	default void setTooltips(String... strings) {
		
	}
	default String[] getTooltips() {
		return new String[0];
	}
	
	boolean isVisible();
	boolean isEnabled();
	void setVisible(boolean visible);
	void setEnabled(boolean enable);
	
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	
	
	void draw(int mouseX, int mouseY, float partial);
	void mouseClick(int mouseX, int mouseY, int mouseButton) throws IOException;
	void mouseReleased(int mouseX, int mouseY, int state);
	void keyTyped(char typedChar, int keyCode) throws IOException;
	void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);
	default void updateComponent() {
		
	}
	default void handleMouseInput() {
		
	}
	default void handleKeyboardInput() {
		
	}
	
	
	default void disable() {
		setEnabled(false);
	}
	default void enable() {
		setEnabled(true);
	}
	
	default void hide() {
		setVisible(false);
	}
	default void show() {
		setVisible(true);
	}
	void assignToPage(int page);
	int getAssignedPage();
	
	
}
