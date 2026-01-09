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
                        ModItems.TELEPORTER,
                        Text.translatable("teleportmod.advancement.root.title"),
                        Text.translatable("teleportmod.advancement.root.description"),
                        Identifier.of("minecraft", "textures/block/netherite_block.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("has_item", InventoryChangedCriterion.Conditions.items(ModItems.TELEPORTER))
                .build(consumer, TeleportMod.MOD_ID + ":teleport/root");

        AdvancementEntry firstTeleport = Advancement.Builder.create()
                .parent(rootAdvancement)
                .display(
                        ModItems.TELEPORT_CRYSTAL,
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
                .parent(rootAdvancement)
                .display(
                        ModItems.TELEPORT_CRYSTAL,
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
