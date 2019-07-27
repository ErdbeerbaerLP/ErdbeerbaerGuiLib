package de.erdbeerbaerlp.guilib;

import de.erdbeerbaerlp.guilib.components.*;
import de.erdbeerbaerlp.guilib.components.Button.DefaultButtonIcons;
import de.erdbeerbaerlp.guilib.gui.BetterGuiScreen;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("unused")
public class ExampleGUI extends BetterGuiScreen {
    private Button exampleButton;
    private CheckBox exampleCheckbox;
    private TextField exampleTextField;
    private Label exampleLabel1;
    private Button exitButton;
    private Slider exampleSlider1;
    private EnumSlider exampleSlider2;
    private ToggleButton exampleToggleButton;
    private EnumSlider drawTypeSlider;
    private Image googleImage;
    private Image apple;

    @Override
    public void buildGui() {

        //Initialize variables
        exampleButton = new Button(50, 50, "Button", DefaultButtonIcons.SAVE);
        exampleCheckbox = new CheckBox(50, 70, "Checkbox", false);
        exampleTextField = new TextField(50, 100, 150);
        exampleLabel1 = new Label("Example GUI", width / 2, 10);
        exitButton = new Button(0, 0, DefaultButtonIcons.DELETE);
        exampleSlider1 = new Slider(50, 130, "Slider: ", 0, 100, 50, () -> System.out.println(exampleSlider1.getValue()));
        exampleSlider2 = new <ExampleEnum>EnumSlider(200, 130, "Enum Slider: ", ExampleEnum.class, ExampleEnum.EXAMPLE, () -> {
            System.out.println("Enum changed to \"" + ((ExampleEnum) exampleSlider2.getEnum()).getName() + "\"");
            System.out.println("Index: " + exampleSlider2.getValueInt());
            System.out.println("Other Value: " + ((ExampleEnum) exampleSlider2.getEnum()).getOtherValue());
        });
        exampleToggleButton = new ToggleButton(50, 170, "Toggle Button: ");
        drawTypeSlider = new <ToggleButton.DrawType>EnumSlider(156, 170, "Draw type: ", ToggleButton.DrawType.class, ToggleButton.DrawType.COLORED_LINE, () -> this.exampleToggleButton.setDrawType((ToggleButton.DrawType) drawTypeSlider.getEnum()));
        googleImage = new Image(200, 40, 136, 46, "https://www.google.de/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        apple = new Image(0, 0, 16, 16, new ResourceLocation("minecraft", "textures/items/apple.png"));

        //Register listeners
        exampleButton.setClickListener(() -> System.out.println("I have been clicked!"));
        exampleCheckbox.setChangeListener(() -> System.out.println(exampleCheckbox.isChecked() ? "I just got Checked" : "I just have been unchecked :/"));
        exitButton.setClickListener(() -> openGui(null));
        exampleTextField.setReturnAction(() -> exampleButton.setText(exampleTextField.getText()));
        exampleToggleButton.setClickListener(() -> {
            System.out.println("New Value: " + exampleToggleButton.getValue());
            this.exampleButton.setEnabled(exampleToggleButton.getValue());
        });
        apple.setCallback(() -> {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_PLAYER_BURP, 1));
            apple.setVisible(false);
            apple.disable();
        });


        //Set tooltips
        exampleButton.setTooltips("Example Tooltip", "This is a Button");
        exampleCheckbox.setTooltips("Another Tooltip", "This is a Checkbox", "");
        exampleTextField.setTooltips("A simple Textbox", "This one does support colors too!", "Simply use a \u00A7", "Press return to run a callback");
        exitButton.setTooltips("Closes this GUI");
        exampleSlider1.setTooltips("A simple double/integer slider");
        exampleSlider2.setTooltips("This slider works using Enums", "It will change through all enum values");
        exampleToggleButton.setTooltips("This button can be toggled");
        drawTypeSlider.setTooltips("Change how the toggle button will be rendered");
        googleImage.setTooltips("This is an example image", "It was loaded from an URL");
        apple.setTooltips("Oh, look at this apple!", "", "Click to eat");

        //Set some values
        exampleTextField.setAcceptsColors(true);
        exampleTextField.setText("Text Field");
        exampleLabel1.setCentered();
        exampleToggleButton.setValue(true);

        //Add components
        this.addComponent(exampleLabel1);
        this.addComponent(exampleButton);
        this.addComponent(exitButton);
        this.addComponent(exampleCheckbox);
        this.addComponent(exampleTextField);
        this.addComponent(exampleSlider1);
        this.addComponent(exampleSlider2);
        this.addComponent(exampleToggleButton);
        this.addComponent(drawTypeSlider);
        this.addAllComponents(googleImage, apple);
    }

    @Override
    public void updateGui() {
        //Update positions
        exampleLabel1.setX(width / 2); //always centered!

        exitButton.setX(width - exitButton.getWidth() - 6);
        exitButton.setY(6);
        apple.setPosition(exitButton.getX() - 40, exitButton.getY() + 30);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public boolean doesEscCloseGui() {
        return false;
    }

    public enum ExampleEnum {
        THIS("This", 1.0f),
        IS("is", 100.45f),
        AN("an", -90f),
        EXAMPLE("example", 1337f),
        SLIDER("slider", -2387f),
        WITH("with", 0.000000001f),
        ENUM("enum", 3.14159265f),
        VALUES("values.", 98234.5f),
        YAAAY("Yaay!", -0f);

        private String name;
        private float otherValue;

        ExampleEnum(String name, float someOtherValue) {
            this.name = name;
            this.otherValue = someOtherValue;
        }

        //This method will be called using reflection from the slider
        public String getName() {
            return name;
        }

        public float getOtherValue() {
            return otherValue;
        }
    }

}
