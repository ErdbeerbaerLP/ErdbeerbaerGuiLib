package de.erdbeerbaerlp.guilib;

import de.erdbeerbaerlp.guilib.components.*;
import de.erdbeerbaerlp.guilib.gui.ExtendedScreen;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;


@SuppressWarnings("unused")
public class ExampleGUI extends ExtendedScreen {
    private Button exampleButton;
    private TextField exampleTextField;
    private Label exampleLabel1;
    private Button exitButton;
    private CheckBox exampleCheckbox;
    private Slider exampleSlider1;
    private EnumSlider exampleSlider2;
    private ToggleButton exampleToggleButton;
    private EnumSlider drawTypeSlider;
    private Image beeGif;
    private Image apple;
    private Image dynamicImage;
    private TextField urlField;
    private Button nextPageButton;
    private Button prevPageButton;
    private Label pageIndicator;

    public ExampleGUI(Screen parent) {
        super(parent);
    }

    @Override
    public void buildGui() {

        //Initialize variables
        exampleButton = new Button(50, 50, "Button", Button.DefaultButtonIcons.SAVE);
        exampleTextField = new TextField(50, 100, 150);
        exampleLabel1 = new Label("Example GUI", width / 2, 10);
        exitButton = new Button(0, 0, Button.DefaultButtonIcons.DELETE);
        exampleCheckbox = new CheckBox(50, 70, "Checkbox", false);
        exampleSlider1 = new Slider(50, 130, "Slider: ", 0, 100, 50, () -> System.out.println(exampleSlider1.getValue()));
        exampleSlider2 = new <ExampleEnum>EnumSlider(200, 130, "Enum Slider: ", ExampleEnum.class, ExampleEnum.EXAMPLE, () -> {
            System.out.println("Enum changed to \"" + ((ExampleEnum) exampleSlider2.getEnum()).getName() + "\"");
            System.out.println("Index: " + exampleSlider2.getValueInt());
            System.out.println("Other Value: " + ((ExampleEnum) exampleSlider2.getEnum()).getOtherValue());
        });
        exampleToggleButton = new ToggleButton(50, 170, "Toggle Button: ");
        drawTypeSlider = new <ToggleButton.DrawType>EnumSlider(156, 170, "Draw type: ", ToggleButton.DrawType.class, ToggleButton.DrawType.COLORED_LINE, () -> this.exampleToggleButton.setDrawType((ToggleButton.DrawType) drawTypeSlider.getEnum()));
        beeGif = new Image(250, 40, 64, 64, "https://gamepedia.cursecdn.com/minecraft_gamepedia/thumb/5/58/Bee.gif/120px-Bee.gif");
        apple = new Image(0, 0, 16, 16, new ResourceLocation("minecraft", "textures/item/apple.png"));

        dynamicImage = new Image(0, 0, 300, 180, "https://www.minecraft.net/etc.clientlibs/minecraft/clientlibs/main/resources/img/header/logo.png");
        urlField = new TextField(0, 0, 240);
        nextPageButton = new Button(0, 0, 40, ">");
        prevPageButton = new Button(0, 0, 40, "<");
        pageIndicator = new Label(0, 0);

        //Register listeners
        exampleButton.setClickListener(() -> System.out.println("I have been clicked!"));
        exampleCheckbox.setChangeListener(() -> System.out.println(exampleCheckbox.isChecked() ? "I just got Checked" : "I just have been unchecked :/"));
        exitButton.setClickListener(this::close);
        exampleTextField.setReturnAction(() -> exampleButton.setText(exampleTextField.getText()));
        exampleToggleButton.setClickListener(() -> {
            System.out.println("New Value: " + exampleToggleButton.getValue());
            this.exampleButton.setEnabled(exampleToggleButton.getValue());
        });
        apple.setCallback(() -> {
            minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_PLAYER_BURP, 1));
            apple.setVisible(false);
            apple.disable();
        });
        urlField.setText(dynamicImage.getImageURL());
        urlField.setReturnAction(() -> {
            dynamicImage.setImageURL(urlField.getText());
        });
        nextPageButton.setClickListener(this::nextPage);
        prevPageButton.setClickListener(this::prevPage);

        //Set tooltips
        exampleButton.setTooltips("Example Tooltip", "This is a Button");
        exampleCheckbox.setTooltips("Another Tooltip", "This is a Checkbox", "");
        exampleTextField.setTooltips("A simple Textbox", "This one does support colors too!", "Simply use a \u00A7", "Press return to run a callback");
        exitButton.setTooltips("Closes this GUI");
        exampleSlider1.setTooltips("A simple double/integer slider");
        exampleSlider2.setTooltips("This slider works using Enums", "It will change through all enum values");
        exampleToggleButton.setTooltips("This button can be toggled");
        drawTypeSlider.setTooltips("Change how the toggle button will be rendered");
        beeGif.setTooltips("This is an example image", "It was loaded from an URL");
        apple.setTooltips("Oh, look at this apple!", "", "Click to eat");

        //Set some values
        exampleTextField.setAcceptsColors(true);
        exampleTextField.setText("Text Field");
        exampleTextField.setLabel("Text Field Label");
        exampleLabel1.setCentered();
        pageIndicator.setCentered();
        exampleToggleButton.setValue(true);
        urlField.setLabel("Image URL");
        urlField.setMaxStringLength(1024);

        //Add components
        this.addAllComponents(exampleLabel1, exitButton, prevPageButton, nextPageButton, pageIndicator);
        this.addComponent(exampleButton, 0);
        this.addComponent(exampleCheckbox, 0);
        this.addComponent(exampleTextField, 0);
        this.addComponent(exampleSlider1, 0);
        this.addComponent(exampleSlider2, 0);
        this.addComponent(exampleToggleButton, 0);
        this.addComponent(drawTypeSlider, 0);
        this.addComponent(beeGif, 0);
        this.addComponent(apple, 0);
        this.addComponent(dynamicImage, 1);
        this.addComponent(urlField, 1);
    }

    @Override
    public void updateGui() {
        //Update positions
        exampleLabel1.setX(width / 2); //always centered!

        nextPageButton.setPosition(width - nextPageButton.getWidth() - 6, height - nextPageButton.getHeight() - 15);
        prevPageButton.setPosition(6, nextPageButton.getY());
        pageIndicator.setPosition(width / 2, height - 13);
        pageIndicator.setText("Page " + getCurrentPage());

        exitButton.setX(width - exitButton.getWidth() - 6);
        exitButton.setY(6);

        apple.setPosition(exitButton.getX() - 40, exitButton.getY() + 30);

        dynamicImage.setPosition(width / 2 - 150, 50);
        urlField.setPosition(dynamicImage.getX(), dynamicImage.getY() - 30);
        urlField.setWidth(dynamicImage.getWidth());
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public boolean doesEscCloseGui() {
        return true;
    }

    /**
     * An example enum
     */
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
