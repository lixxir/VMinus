package net.lixir.vminus.datagen;

import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class VMinusRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public VMinusRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        for (BlockSet blockSet : BlockSet.BLOCK_SETS) {
            ItemLike baseBlock = blockSet.getBaseBlock();

            if (baseBlock != null && blockSet.hasSlab()) {
                ItemLike slabBlock = blockSet.getSlabBlock();
                if (slabBlock != null) {
                    slabRecipe(pWriter, baseBlock, slabBlock);
                }
            }

            if (baseBlock != null && blockSet.hasStairs()) {
                ItemLike stairsBlock = blockSet.getStairsBlock();
                if (stairsBlock != null) {
                    stairsRecipe(pWriter, baseBlock, stairsBlock);
                }
            }

            if (baseBlock != null && blockSet.hasWall()) {
                ItemLike wallBlock = blockSet.getWallBlock();
                if (wallBlock != null) {
                    wallRecipe(pWriter, baseBlock, wallBlock);
                }
            }

            if (baseBlock != null && blockSet.hasCracked()) {
                ItemLike crackedBlock = blockSet.getCrackedBlock();
                if (crackedBlock != null) {
                    crackedRecipe(baseBlock, crackedBlock);
                }
            }
        }
    }

    private void slabRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike slabBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabBlock, 4)
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabBlock, 6)
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        }
    }

    private void stairsRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike stairsBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsBlock, 3)
                    .pattern("B  ")
                    .pattern("BB ")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsBlock, 4)
                    .pattern("B  ")
                    .pattern("BB ")
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        }
    }

    private void wallRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike wallBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 2)
                    .pattern("B")
                    .pattern("B")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 6)
                    .pattern("BBB")
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        }
    }

    private void crackedRecipe(ItemLike baseBlock, ItemLike crackedBlock) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(baseBlock), RecipeCategory.BUILDING_BLOCKS, crackedBlock, 0.1f, 200)
                .unlockedBy(getHasName(baseBlock), has(baseBlock));
    }
}
