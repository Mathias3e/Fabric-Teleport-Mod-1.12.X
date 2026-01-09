package net.mathias.teleportmod;

import net.fabricmc.api.ModInitializer;

import net.mathias.teleportmod.block.ModBlocks;
import net.mathias.teleportmod.item.ModItemGroups;
import net.mathias.teleportmod.item.ModItems;
import net.mathias.teleportmod.networking.ModMessages;
import net.mathias.teleportmod.screen.ModScreenHandlers;
import net.mathias.teleportmod.world.gen.ModWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeleportMod implements ModInitializer {
	public static final String MOD_ID = "teleportmod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItemGroups.registerItemGroups();

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModScreenHandlers.registerScreenHandlers();

        ModMessages.registerNetworking();

        ModWorldGeneration.generateModWorldGen();
	}
}