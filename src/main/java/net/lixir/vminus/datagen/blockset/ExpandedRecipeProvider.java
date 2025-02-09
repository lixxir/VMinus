package net.lixir.vminus.datagen.blockset;

import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ExpandedRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ExpandedRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        for (BlockSet blockSet : BlockSet.BLOCK_SETS) {
            ItemLike baseBlock = blockSet.getBaseBlock();

            if (baseBlock != null) {
                if (blockSet.hasSlab()) {
                    ItemLike slabBlock = blockSet.getSlabBlock();
                    if (slabBlock != null) {
                        slabRecipe(pWriter, baseBlock, slabBlock);
                    }
                }

                if (blockSet.hasStairs()) {
                    ItemLike stairsBlock = blockSet.getStairsBlock();
                    if (stairsBlock != null) {
                        stairsRecipe(pWriter, baseBlock, stairsBlock);
                    }
                }

                if (blockSet.hasWall()) {
                    ItemLike wallBlock = blockSet.getWallBlock();
                    if (wallBlock != null) {
                        wallRecipe(pWriter, baseBlock, wallBlock);
                    }
                }

                if (blockSet.hasCracked()) {
                    ItemLike crackedBlock = blockSet.getCrackedBlock();
                    if (crackedBlock != null) {
                        crackedRecipe(baseBlock, crackedBlock);
                    }
                }

                if (blockSet.hasLog()) {
                    ItemLike logBlock = blockSet.getLogBlock();
                    if (logBlock != null) {
                        crackedRecipe(baseBlock, logBlock);
                    }
                }
            }
        }
    }

    protected void chestplateRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                    .pattern("B B")
                    .pattern("BBB")
                    .pattern("BBB")
                    .define('B', material)
                    .unlockedBy(getHasName(material), has(material))
                    .save(writer);
    }

    protected void leggingsRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("BBB")
                .pattern("B B")
                .pattern("B B")
                .define('B', material)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }

    protected void bootsRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("B B")
                .pattern("B B")
                .define('B', material)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }

    protected void helmetRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("BBB")
                .pattern("B B")
                .define('B', material)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }

    protected void pickaxeRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }


    protected void axeRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("BB")
                .pattern("BS")
                .pattern(" S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }

    protected void shovelRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("B")
                .pattern("S")
                .pattern("S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }

    protected void swordRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("B")
                .pattern("B")
                .pattern("S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }


    protected void hoeRecipe(Consumer<FinishedRecipe> writer, Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("BB")
                .pattern(" S")
                .pattern(" S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
    }



    protected void slabRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike slabBlock) {
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

    protected void stairsRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike stairsBlock) {
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

    protected void wallRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike wallBlock) {
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

    protected void crackedRecipe(ItemLike baseBlock, ItemLike crackedBlock) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(baseBlock), RecipeCategory.BUILDING_BLOCKS, crackedBlock, 0.1f, 200)
                .unlockedBy(getHasName(baseBlock), has(baseBlock));
    }
}
