package net.mathias.teleportmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.mathias.teleportmod.item.ModItems;
import net.minecraft.registry.RegistryWrapper;
import net.mathias.teleportmod.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Items.ENERGY_SCOURS_ITEMS)
                .add(ModItems.ENERGY_ORB);

        getOrCreateTagBuilder(ModTags.Items.TELEPORT_CRYSTAL_ITEMS)
                .add(ModItems.TELEPORT_CRYSTAL_TIER_1)
                .add(ModItems.TELEPORT_CRYSTAL_TIER_2)
                .add(ModItems.TELEPORT_CRYSTAL_TIER_3)
                .add(ModItems.TELEPORT_CRYSTAL_TIER_4);
    }
}
