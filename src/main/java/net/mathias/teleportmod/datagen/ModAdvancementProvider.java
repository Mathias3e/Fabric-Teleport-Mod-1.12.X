package net.mathias.teleportmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.mathias.teleportmod.TeleportMod;
import net.mathias.teleportmod.item.ModItems;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {

        AdvancementEntry rootAdvancement = Advancement.Builder.create()
                .display(
                        ModItems.TELEPORT_CRYSTAL_TIER_1,
                        Text.translatable("teleportmod.advancement.root.title"),
                        Text.translatable("teleportmod.advancement.root.description"),
                        Identifier.of("minecraft", "textures/block/netherite_block.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(ModItems.TELEPORT_CRYSTAL_TIER_1))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/root");

        AdvancementEntry teleportCrystalTier2 = Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(
                        ModItems.TELEPORT_CRYSTAL_TIER_2,
                        Text.translatable("teleportmod.advancement.teleport_crystal_tier_2.title"),
                        Text.translatable("teleportmod.advancement.teleport_crystal_tier_2.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(ModItems.TELEPORT_CRYSTAL_TIER_2))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/teleport_crystal_tier_2");

        AdvancementEntry teleportCrystalTier3 = Advancement.Builder.create()
                .parent(teleportCrystalTier2)
                .display(
                        ModItems.TELEPORT_CRYSTAL_TIER_3,
                        Text.translatable("teleportmod.advancement.teleport_crystal_tier_3.title"),
                        Text.translatable("teleportmod.advancement.teleport_crystal_tier_3.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(ModItems.TELEPORT_CRYSTAL_TIER_3))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/teleport_crystal_tier_3");

        AdvancementEntry teleportCrystalTier4 = Advancement.Builder.create()
                .parent(teleportCrystalTier3)
                .display(
                        ModItems.TELEPORT_CRYSTAL_TIER_4,
                        Text.translatable("teleportmod.advancement.teleport_crystal_tier_4.title"),
                        Text.translatable("teleportmod.advancement.teleport_crystal_tier_4.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(ModItems.TELEPORT_CRYSTAL_TIER_4))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/teleport_crystal_tier_4");

        AdvancementEntry teleporter = Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(
                        ModItems.TELEPORTER,
                        Text.translatable("teleportmod.advancement.teleporter.title"),
                        Text.translatable("teleportmod.advancement.teleporter.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(ModItems.TELEPORTER))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/teleporter");

        AdvancementEntry firstTeleport = Advancement.Builder.create()
                .parent(teleporter)
                .display(
                        ModItems.TELEPORTER,
                        Text.translatable("teleportmod.advancement.first_teleport.title"),
                        Text.translatable("teleportmod.advancement.first_teleport.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("first_teleport", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/first_teleport");

        AdvancementEntry materializedInside = Advancement.Builder.create()
                .parent(teleporter)
                .display(
                        ModItems.TELEPORTER,
                        Text.translatable("teleportmod.advancement.materialized_inside.title"),
                        Text.translatable("teleportmod.advancement.materialized_inside.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("materialized_inside", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/materialized_inside");
    }
}
