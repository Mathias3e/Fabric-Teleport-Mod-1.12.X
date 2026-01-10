package net.mathias.teleportmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.mathias.teleportmod.TeleportMod;
import net.mathias.teleportmod.component.ModDataComponentTypes;
import net.mathias.teleportmod.item.ModItems;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ItemStack teleporterItem = new ItemStack(ModItems.TELEPORTER);

        SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.ofItems(ModItems.TELEPORT_CRYSTAL_TIER_1),
                        Ingredient.ofItems(Items.IRON_INGOT),
                        RecipeCategory.MISC,
                        ModItems.TELEPORT_CRYSTAL_TIER_2
                )
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL_TIER_1), conditionsFromItem(ModItems.TELEPORT_CRYSTAL_TIER_1))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleport_crystal_tier_2_smithing"));

        SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.ofItems(ModItems.TELEPORT_CRYSTAL_TIER_2),
                        Ingredient.ofItems(Items.DIAMOND),
                        RecipeCategory.MISC,
                        ModItems.TELEPORT_CRYSTAL_TIER_3
                )
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL_TIER_2), conditionsFromItem(ModItems.TELEPORT_CRYSTAL_TIER_2))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleport_crystal_tier_3_smithing"));

        SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.ofItems(ModItems.TELEPORT_CRYSTAL_TIER_3),
                        Ingredient.ofItems(Items.NETHERITE_INGOT),
                        RecipeCategory.MISC,
                        ModItems.TELEPORT_CRYSTAL_TIER_4
                )
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL_TIER_3), conditionsFromItem(ModItems.TELEPORT_CRYSTAL_TIER_3))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleport_crystal_tier_4_smithing"));

        // ----------------------------------------------------------------------------------------------- //

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TELEPORT_CRYSTAL_TIER_1)
                .pattern("SES")
                .input('S', Items.AMETHYST_SHARD)
                .input('E', Items.ENDER_PEARL)
                .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleport_crystal_tier_1_crafting"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TELEPORT_CRYSTAL_TIER_2)
                .pattern("UUU")
                .pattern("SES")
                .pattern("UUU")
                .input('S', Items.AMETHYST_SHARD)
                .input('E', Items.ENDER_PEARL)
                .input('U', Items.IRON_INGOT)
                .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleport_crystal_tier_2_crafting"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TELEPORT_CRYSTAL_TIER_3)
                .pattern("UUU")
                .pattern("SES")
                .pattern("UUU")
                .input('S', Items.AMETHYST_SHARD)
                .input('E', Items.ENDER_PEARL)
                .input('U', Items.DIAMOND)
                .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleport_crystal_tier_3_crafting"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TELEPORT_CRYSTAL_TIER_4)
                .pattern("UUU")
                .pattern("SES")
                .pattern("UUU")
                .input('S', Items.AMETHYST_SHARD)
                .input('E', Items.ENDER_PEARL)
                .input('U', Items.NETHERITE_INGOT)
                .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleport_crystal_tier_4_crafting"));

        // ----------------------------------------------------------------------------------------------- //

        NbtShapedRecipeBuilder.create(RecipeCategory.MISC, teleporterItem)
                .component(ModDataComponentTypes.BLOCK_AMOUNT_PER_ONE_ENERGY_ORB, 30.0f)
                .pattern("  N")
                .pattern("CKC")
                .pattern("CRC")
                .input('N', Items.NETHERITE_INGOT)
                .input('C', Items.COPPER_INGOT)
                .input('K', ModItems.TELEPORT_CRYSTAL_TIER_1)
                .input('R', Items.REDSTONE)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL_TIER_1), conditionsFromItem(ModItems.TELEPORT_CRYSTAL_TIER_1))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleporter_30"));

        NbtShapedRecipeBuilder.create(RecipeCategory.MISC, teleporterItem)
                .component(ModDataComponentTypes.BLOCK_AMOUNT_PER_ONE_ENERGY_ORB, 60.0f)
                .pattern("  N")
                .pattern("CKC")
                .pattern("CRC")
                .input('N', Items.NETHERITE_INGOT)
                .input('C', Items.COPPER_INGOT)
                .input('K', ModItems.TELEPORT_CRYSTAL_TIER_2)
                .input('R', Items.REDSTONE)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL_TIER_2), conditionsFromItem(ModItems.TELEPORT_CRYSTAL_TIER_2))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleporter_60"));

        NbtShapedRecipeBuilder.create(RecipeCategory.MISC, teleporterItem)
                .component(ModDataComponentTypes.BLOCK_AMOUNT_PER_ONE_ENERGY_ORB, 120.0f)
                .pattern("  N")
                .pattern("CKC")
                .pattern("CRC")
                .input('N', Items.NETHERITE_INGOT)
                .input('C', Items.COPPER_INGOT)
                .input('K', ModItems.TELEPORT_CRYSTAL_TIER_3)
                .input('R', Items.REDSTONE)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL_TIER_3), conditionsFromItem(ModItems.TELEPORT_CRYSTAL_TIER_3))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleporter_120"));

        NbtShapedRecipeBuilder.create(RecipeCategory.MISC, teleporterItem)
                .component(ModDataComponentTypes.BLOCK_AMOUNT_PER_ONE_ENERGY_ORB, 240.0f)
                .pattern("  N")
                .pattern("CKC")
                .pattern("CRC")
                .input('N', Items.NETHERITE_INGOT)
                .input('C', Items.COPPER_INGOT)
                .input('K', ModItems.TELEPORT_CRYSTAL_TIER_4)
                .input('R', Items.REDSTONE)
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(ModItems.TELEPORT_CRYSTAL_TIER_4), conditionsFromItem(ModItems.TELEPORT_CRYSTAL_TIER_4))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(recipeExporter, Identifier.of("teleportmod", "teleporter_240"));
    }
}
