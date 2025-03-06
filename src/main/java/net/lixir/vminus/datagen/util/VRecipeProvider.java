package net.lixir.vminus.datagen.util;

import net.lixir.vminus.util.setup.SetupRecipe;
import net.lixir.vminus.util.setup.SetupRegistries;
import net.lixir.vminus.util.setup.block.BlockSetup;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class VRecipeProvider extends RecipeProvider implements IConditionBuilder {
    final private String modId;

    public VRecipeProvider(PackOutput pOutput, String modId) {
        super(pOutput);
        this.modId = modId;
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(blockSet -> registerBlockSetRecipes(pWriter, blockSet));

        for (BlockSetup blockSetup : SetupRegistries.BLOCKS.getValues(modId)) {
            BlockItemRegistryPair blockItemPair = blockSetup.getBlockItemPair();
            Block block = blockItemPair.block();
            Block baseBlock = blockSetup.getBaseBlock();
            SetupRecipe blockSetupRecipe = blockSetup.getSetupRecipe();
            if (baseBlock != null && !blockSetupRecipe.equals(SetupRecipe.NONE)) {
                switch (blockSetupRecipe) {
                    case WALL -> wallRecipe(pWriter, baseBlock, block);
                }
            }

        }


    }

    private void registerBlockSetRecipes(@NotNull Consumer<FinishedRecipe> pWriter, BlockSet blockSet) {
        ItemLike baseBlock = blockSet.getBaseBlock();
        if (baseBlock == null)
            return;
        if (blockSet.getStairs() != null) {
            ItemLike slabBlock = blockSet.getStairs().item();
            stairsRecipe(pWriter, baseBlock, slabBlock);
        }
        if (blockSet.getSlab() != null) {
            ItemLike stairsBlock = blockSet.getSlab().item();
            slabRecipe(pWriter, baseBlock, stairsBlock);
        }
        if (blockSet.getWall() != null) {
            ItemLike wallBlock = blockSet.getWall().item();
            wallRecipe(pWriter, baseBlock, wallBlock);
        }
        if (blockSet.getFence() != null) {
            ItemLike fenceBlock = blockSet.getFence().item();
            fenceRecipe(pWriter, baseBlock, fenceBlock);
        }
        if (blockSet.getDoor() != null) {
            ItemLike doorBlock = blockSet.getDoor().item();
            doorRecipe(pWriter, baseBlock, doorBlock);
        }
        if (blockSet.getTrapdoor() != null) {
            ItemLike trapdoorBlock = blockSet.getTrapdoor().item();
            doorRecipe(pWriter, baseBlock, trapdoorBlock);
        }
        if (blockSet.getPressurePlate() != null) {
            ItemLike pressurePlate = blockSet.getPressurePlate().item();
            ItemLike slab = blockSet.getSlab().item();
            pressurePlateRecipe(pWriter, baseBlock, slab, pressurePlate);
        }
        if (blockSet.getButton() != null) {
            ItemLike buttonBlock = blockSet.getButton().item();
            buttonRecipe(pWriter, baseBlock, buttonBlock);
        }
        if (blockSet.getSign() != null) {
            ItemLike signItem = blockSet.getSign().item();
            signRecipe(pWriter, baseBlock, signItem);
        }
        if (blockSet.getHangingSign() != null) {
            ItemLike hangingSignItem = blockSet.getHangingSign().item();
            hangingSignRecipe(pWriter, baseBlock, blockSet.getStrippedLog().block(), hangingSignItem);
        }
        if (blockSet.getLog() != null) {
            planksFromLog(pWriter, baseBlock, blockSet.getLogsTag().getSecond(), 4);
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

    protected void buttonRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike buttonBlock) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, buttonBlock, 1)
                    .requires(baseBlock, 1)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);

    }

    protected void signRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike signItem) {
        if (ModList.get().isLoaded("detour") ) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 1)
                    .pattern("B")
                    .pattern("S")
                    .define('S', Items.STICK)
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 3)
                    .pattern("BBB")
                    .pattern("BBB")
                    .pattern(" S ")
                    .define('S', Items.STICK)
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        }
    }

    protected void hangingSignRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, @Nullable ItemLike strippedLogBlock, ItemLike signItem) {
        if (ModList.get().isLoaded("detour") ) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 1)
                    .pattern("C")
                    .pattern("B")
                    .define('C', Items.CHAIN)
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        } else if (strippedLogBlock != null){
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 6)
                    .pattern("C C")
                    .pattern("BBB")
                    .pattern("BBB")
                    .define('C', Items.CHAIN)
                    .define('B', strippedLogBlock)
                    .unlockedBy(getHasName(strippedLogBlock), has(strippedLogBlock))
                    .save(writer);
        }
    }

    protected void pressurePlateRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, @Nullable ItemLike slabBlock, ItemLike pressurePlate) {
        if (ModList.get().isLoaded("detour") && slabBlock != null) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pressurePlate, 1)
                    .requires(slabBlock, 1)
                    .unlockedBy(getHasName(slabBlock), has(slabBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pressurePlate, 1)
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        }
    }

    protected void trapdoorRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, @Nullable ItemLike slabBlock, ItemLike trapdoorBlock) {
        if (ModList.get().isLoaded("detour")  && slabBlock != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, trapdoorBlock, 1)
                    .pattern("BB")
                    .define('B', slabBlock)
                    .unlockedBy(getHasName(slabBlock), has(slabBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, trapdoorBlock, 2)
                    .pattern("BBB")
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        }
    }

    protected void doorRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike doorBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, doorBlock, 2)
                    .pattern("BB")
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, doorBlock, 3)
                    .pattern("BB")
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        }
    }

    protected void stairsRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike stairsBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsBlock, 4)
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

    protected void woodRecipe(Consumer<FinishedRecipe> writer, ItemLike logBlock, ItemLike woodBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodBlock, 4)
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', logBlock)
                    .unlockedBy(getHasName(logBlock), has(logBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodBlock, 3)
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', logBlock)
                    .unlockedBy(getHasName(logBlock), has(logBlock))
                    .save(writer);
        }
    }

    protected void fenceRecipe(Consumer<FinishedRecipe> writer, ItemLike baseBlock, ItemLike wallBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 4)
                    .pattern("BS")
                    .pattern("BS")
                    .define('B', baseBlock)
                    .define('S', Items.STICK)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(writer);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 3)
                    .pattern("BSB")
                    .pattern("BSB")
                    .define('B', baseBlock)
                    .define('S', Items.STICK)
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
