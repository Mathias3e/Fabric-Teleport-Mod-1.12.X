package net.mathias.teleportmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.mathias.teleportmod.TeleportMod;
import net.mathias.teleportmod.block.ModBlocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup TELEPORT_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(TeleportMod.MOD_ID,
        "teleport_items_group"),
        FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModItems.TELEPORTER))
                .displayName(Text.translatable("itemgroup.teleportmod.teleport_items"))
                .entries((displayContext, entries) -> {
                    entries.add(ModItems.TELEPORT_CRYSTAL_TIER_1);
                    entries.add(ModItems.TELEPORT_CRYSTAL_TIER_2);
                    entries.add(ModItems.TELEPORT_CRYSTAL_TIER_3);
                    entries.add(ModItems.TELEPORT_CRYSTAL_TIER_4);
                    entries.add(ModItems.ENERGY_ORB);
                    entries.add(ModBlocks.ENERGY_ORE);

                    ItemStack teleporterStack = new ItemStack(ModItems.TELEPORTER);
                    NbtCompound nbt = new NbtCompound();
                    nbt.putFloat("block_amount_per_one_energy_orb", 30.0f);
                    teleporterStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

                    entries.add(teleporterStack);
                }).build());

    public static void registerItemGroups() {
        TeleportMod.LOGGER.info("Registering Item Groups for" + TeleportMod.MOD_ID);
    }
}
