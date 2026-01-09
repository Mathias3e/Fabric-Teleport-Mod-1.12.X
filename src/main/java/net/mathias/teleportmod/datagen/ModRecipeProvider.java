package net.mathias.teleportmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.mathias.teleportmod.item.ModItems;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TELEPORT_CRYSTAL)
                .pattern("SES")
                .input('S', Items.AMETHYST_SHARD)
                .input('E', Items.ENDER_PEARL)
                .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TELEPORTER)
                .pattern("  N")
                .pattern("CKC")
                .pattern("CRC")
                .input('N', Items.NETHERITE_INGOT)
                .input('C', Items.COPPER_INGOT)
                .input('K', ModItems.TELEPORT_CRYSTAL)
                .input('R', Items.REDSTONE)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL), conditionsFromItem(ModItems.TELEPORT_CRYSTAL))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(recipeExporter);
    }
}
