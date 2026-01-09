package net.mathias.teleportmod.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.mathias.teleportmod.TeleportMod;
import net.mathias.teleportmod.screen.custom.TeleporterScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<TeleporterScreenHandler> TELEPORTER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    Identifier.of(TeleportMod.MOD_ID, "teleporter_screen_handler"),
                    new ExtendedScreenHandlerType<>(TeleporterScreenHandler::new, ItemStack.PACKET_CODEC));

    public static void registerScreenHandlers() {
        TeleportMod.LOGGER.info("Registering Screen Handlers for " + TeleportMod.MOD_ID);
    }
}