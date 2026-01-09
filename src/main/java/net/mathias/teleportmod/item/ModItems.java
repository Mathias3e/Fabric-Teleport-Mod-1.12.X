package net.mathias.teleportmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.mathias.teleportmod.TeleportMod;
import net.mathias.teleportmod.item.custom.TeleporterItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item ENERGY_ORB = registerItem("energy_orb", new Item(new Item.Settings()));
    public static final Item TELEPORT_CRYSTAL_TIER_1 = registerItem("teleport_crystal_tier_1", new Item(new Item.Settings()));
    public static final Item TELEPORT_CRYSTAL_TIER_2 = registerItem("teleport_crystal_tier_2", new Item(new Item.Settings()));
    public static final Item TELEPORT_CRYSTAL_TIER_3 = registerItem("teleport_crystal_tier_3", new Item(new Item.Settings()));
    public static final Item TELEPORT_CRYSTAL_TIER_4 = registerItem("teleport_crystal_tier_4", new Item(new Item.Settings()));

    public static final Item TELEPORTER = registerItem("teleporter", new TeleporterItem(new Item.Settings()
        .maxCount(1)
    ));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(TeleportMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        TeleportMod.LOGGER.info("Registering Mod Items for " + TeleportMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(TELEPORT_CRYSTAL_TIER_1);
            entries.add(TELEPORT_CRYSTAL_TIER_2);
            entries.add(TELEPORT_CRYSTAL_TIER_3);
            entries.add(TELEPORT_CRYSTAL_TIER_4);
            // Weitere Itrems
        });
    }
}