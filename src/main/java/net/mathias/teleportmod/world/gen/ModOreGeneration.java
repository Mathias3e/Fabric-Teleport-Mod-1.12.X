package net.mathias.teleportmod.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.mathias.teleportmod.world.ModPlacedFeatures;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class ModOreGeneration {
    public static void generateOres() {
        //BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.CRIMSON_FOREST, BiomeKeys.NETHER_WASTES),
        //GenerationStep.Feature.UNDERGROUND_ORES,
        //        ModPlacedFeatures.NETHER_ENERGY_ORE_PLACED_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_ENERGY_ORE_PLACED_KEY);
    }
}