package net.mathias.teleportmod.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.mathias.teleportmod.item.ModItems;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifiers {
    private static final Identifier END_CITY_TREASURE_ID =
            Identifier.of("minecraft", "chests/end_city_treasure");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (END_CITY_TREASURE_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1f, 1f))
                        .with(ItemEntry.builder(ModItems.TELEPORT_CRYSTAL_TIER_2)
                                .weight(5)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)))
                        );
                tableBuilder.pool(poolBuilder);
            }
        });
    }
}