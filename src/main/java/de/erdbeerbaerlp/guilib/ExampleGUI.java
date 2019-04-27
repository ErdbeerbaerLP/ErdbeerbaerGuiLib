package de.erdbeerbaerlp.guilib;

import de.erdbeerbaerlp.guilib.components.Button;
import de.erdbeerbaerlp.guilib.components.Button.ButtonIcon;
import de.erdbeerbaerlp.guilib.components.CheckBox;
import de.erdbeerbaerlp.guilib.gui.BetterGuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;

public class ExampleGUI extends BetterGuiScreen {
	private Button exampleButton;
	private CheckBox exampleCheckbox;
	@Override
	public void initGui() {
		exampleButton = new Button(50, 50, "Button", ButtonIcon.SAVE);
		exampleCheckbox = new CheckBox(50, 70, "Tick Box", false);
		
		exampleButton.addClickListener(() ->{
			System.out.println("I have been clicked!");
		});
		exampleButton.setTooltips("Example Tooltip", "This is a Button");
		this.addComponent(exampleButton);
		exampleCheckbox.setTooltips("Another Tooltip", "This is a Checkbox", "");
		this.addComponent(exampleCheckbox);
		
	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doesGuiPauseGame() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
