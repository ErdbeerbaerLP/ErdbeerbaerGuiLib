package de.erdbeerbaerlp.guilib.components;

public interface IGuiComponent {
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
	
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	
}
