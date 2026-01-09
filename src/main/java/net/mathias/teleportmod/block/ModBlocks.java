package net.mathias.teleportmod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mathias.teleportmod.TeleportMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class  ModBlocks {
    public static final Block ENERGY_ORE = registerBlock("energy_ore", new ExperienceDroppingBlock(UniformIntProvider.create(1, 1),
            AbstractBlock.Settings.create()
                //.luminance(state -> 4)
                .mapColor(MapColor.DARK_RED)
                .requiresTool()
                .strength(4.5F, 3.0F)
                .sounds(BlockSoundGroup.NETHER_ORE)
    ));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(TeleportMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(TeleportMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        TeleportMod.LOGGER.info("Registering Mod Blocks for " + TeleportMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(ModBlocks.ENERGY_ORE);
        });
    }
}