package net.mathias.teleportmod;

import net.fabricmc.api.ClientModInitializer;
import net.mathias.teleportmod.screen.ModScreenHandlers;
import net.mathias.teleportmod.screen.custom.TeleporterScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class TeleportModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Verbindet den Logik-Handler mit dem grafischen Screen
        HandledScreens.register(ModScreenHandlers.TELEPORTER_SCREEN_HANDLER, TeleporterScreen::new);
    }
}
