package de.erdbeerbaerlp.guilib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = McMod.MODID, name = McMod.NAME, version = McMod.VERSION)
public class McMod {
	public static final String MODID = "eguilib";
	public static final String NAME = "Erdbeerbaer´s GuiLib";
	public static final String VERSION = "1.0.0";
	public McMod() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	
}
