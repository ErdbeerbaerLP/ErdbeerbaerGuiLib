package de.erdbeerbaerlp.guilib;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class GuiLibMixinConnector implements IMixinConnector {
    /**
     * Connect to Mixin
     */
    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.eguilib.json");
    }
}