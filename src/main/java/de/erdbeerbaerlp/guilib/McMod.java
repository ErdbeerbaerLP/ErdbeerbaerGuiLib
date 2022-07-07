package de.erdbeerbaerlp.guilib;

import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(McMod.MODID)
public class McMod {
    public static final String MODID = "eguilib";
    public static final String NAME = "Erdbeerbaer´s GuiLib";
    public static final String VERSION = "2.0.0";
    public static final Logger LOGGER = LogManager.getLogger();

    public McMod() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory((mc, screen) -> new ExampleGUI(screen)));

    }

}
