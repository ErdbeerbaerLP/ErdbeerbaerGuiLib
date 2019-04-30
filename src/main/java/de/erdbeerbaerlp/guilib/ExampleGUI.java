package de.erdbeerbaerlp.guilib;

import de.erdbeerbaerlp.guilib.components.Button;
import de.erdbeerbaerlp.guilib.components.Button.ButtonIcon;
import de.erdbeerbaerlp.guilib.components.CheckBox;
import de.erdbeerbaerlp.guilib.components.EnumSlider;
import de.erdbeerbaerlp.guilib.components.Label;
import de.erdbeerbaerlp.guilib.components.Slider;
import de.erdbeerbaerlp.guilib.components.TextField;
import de.erdbeerbaerlp.guilib.gui.BetterGuiScreen;

public class ExampleGUI extends BetterGuiScreen {
	public enum ExampleEnum {
		THIS,IS,AN,EXAMPLE,SLIDER,WITH,ENUM,VALUES,YAAAY;
	}

	private Button exampleButton;
	private CheckBox exampleCheckbox;
	private TextField exampleTextField;
	private Label exampleLabel1;
	private Button exitButton;
	private Slider exampleSlider1;
	private EnumSlider exampleSlider2;
	@Override
	public void buildGui() {
		//Initialize variables
		exampleButton = new Button(50, 50, "Button", ButtonIcon.SAVE);
		exampleCheckbox = new CheckBox(50, 70, "Checkbox", false);
		exampleTextField = new TextField(50, 100, 150);
		exampleLabel1 = new Label(width/2, 10);
		exitButton = new Button(0, 0, ButtonIcon.DELETE); //Set initial position to 0 for complete handling in update()
		exampleSlider1 = new Slider(50, 130, "Slider: ", 0, 100, 50, ()->{
			System.out.println(exampleSlider1.getValue());
		});
		exampleSlider2 = new <ExampleEnum> EnumSlider(200, 130, "Enum Slider: ", ExampleEnum.class, ExampleEnum.EXAMPLE, ()-> {
			System.out.println("Enum changed to \""+exampleSlider2.getEnum().name()+"\"");
			System.out.println("Index: "+exampleSlider2.getValueInt());
		});

		//Register listeners
		exampleButton.setClickListener(() ->{
			System.out.println("I have been clicked!");
		});
		exampleCheckbox.setChangeListener(()->{
			System.out.println(exampleCheckbox.isChecked() ? "I just got Checked":"I just have been unchecked :/");
		});
		exitButton.setClickListener(()->{
			openGui(null);
		});
		exampleTextField.setReturnAction(()->{
			exampleButton.setText(exampleTextField.getText());
		});
		//Set tooltips
		exampleButton.setTooltips("Example Tooltip", "This is a Button");
		exampleCheckbox.setTooltips("Another Tooltip", "This is a Checkbox", "");
		exampleTextField.setTooltips("A simple Textbox", "This one does support colors too!", "Simply use a \u00A7", "Press return to run a callback");
		exitButton.setTooltips("Closes this GUI");
		exampleSlider1.setTooltips("A simple double/integer slider");
		exampleSlider2.setTooltips("This slider works using Enums", "It will change through all enum values");
		//Set some values
		exampleTextField.setAcceptsColors(true);
		exampleTextField.setText("Text Field");
		exampleLabel1.setCentered();

		//Add components
		this.addComponent(exampleLabel1);
		this.addComponent(exampleButton);
		this.addComponent(exitButton);
		this.addComponent(exampleCheckbox);
		this.addComponent(exampleTextField);
		this.addComponent(exampleSlider1);
		this.addComponent(exampleSlider2);

	}
	@Override
	public void updateGui() {
		//		System.out.println("update");
		//Update positions
		exampleLabel1.x = width/2; //always centered!

		exitButton.x = width-exitButton.width-6;
		exitButton.y = 6;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	@Override
	public boolean doesEscCloseGui() {
		return false;
	}

}
