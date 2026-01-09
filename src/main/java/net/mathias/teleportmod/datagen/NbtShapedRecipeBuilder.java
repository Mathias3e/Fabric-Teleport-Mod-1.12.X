package net.mathias.teleportmod.datagen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NbtShapedRecipeBuilder implements CraftingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final ItemStack output;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    private boolean showNotification = true;

    private NbtShapedRecipeBuilder(RecipeCategory category, ItemStack output) {
        this.category = category;
        this.output = output;
    }

    public static NbtShapedRecipeBuilder create(RecipeCategory category, ItemStack output) {
        return new NbtShapedRecipeBuilder(category, output);
    }

    public static NbtShapedRecipeBuilder create(RecipeCategory category, ItemConvertible item, int count) {
        return new NbtShapedRecipeBuilder(category, new ItemStack(item, count));
    }

    // --- Fluent NBT / Component Methods ---

    /**
     * Generic method to add any Data Component.
     */
    public <T> NbtShapedRecipeBuilder component(ComponentType<T> type, T value) {
        this.output.set(type, value);
        return this;
    }

    public NbtShapedRecipeBuilder input(Character c, TagKey<Item> tag) {
        return this.input(c, Ingredient.fromTag(tag));
    }

    public NbtShapedRecipeBuilder input(Character c, ItemConvertible itemProvider) {
        return this.input(c, Ingredient.ofItems(itemProvider));
    }

    public NbtShapedRecipeBuilder input(Character c, Ingredient ingredient) {
        if (this.inputs.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        } else if (c == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.inputs.put(c, ingredient);
            return this;
        }
    }

    public NbtShapedRecipeBuilder pattern(String patternStr) {
        if (!this.pattern.isEmpty() && patternStr.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(patternStr);
            return this;
        }
    }

    public NbtShapedRecipeBuilder criterion(String string, AdvancementCriterion<?> advancementCriterion) {
        this.criteria.put(string, advancementCriterion);
        return this;
    }

    public NbtShapedRecipeBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public NbtShapedRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output.getItem();
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        RawShapedRecipe rawShapedRecipe = this.validate(recipeId);
        Advancement.Builder builder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        this.criteria.forEach(builder::criterion);

        ShapedRecipe shapedRecipe = new ShapedRecipe(
                Objects.requireNonNullElse(this.group, ""),
                CraftingRecipeJsonBuilder.toCraftingCategory(this.category),
                rawShapedRecipe,
                this.output,
                this.showNotification
        );

        exporter.accept(recipeId, shapedRecipe, builder.build(recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private RawShapedRecipe validate(Identifier recipeId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        } else {
            return RawShapedRecipe.create(this.inputs, this.pattern);
        }
    }
}