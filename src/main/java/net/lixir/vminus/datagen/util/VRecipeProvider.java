package net.lixir.vminus.datagen.util;

import net.lixir.vminus.datagen.util.simple.*;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class VRecipeProvider extends RecipeProvider implements IConditionBuilder {
    final private String modId;
    protected Consumer<FinishedRecipe> pWriter = null;

    public VRecipeProvider(PackOutput pOutput, String modId) {
        super(pOutput);
        this.modId = modId;
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        this.pWriter = pWriter;
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(this::blockSets);
        simpleDatagen();
    }

    private void simpleDatagen() {
        for (DatagenObject simpleDatagen : DatagenRegistry.getValuesFromModId(modId)) {
            if (!simpleDatagen.hasRecipes())
                continue;
            if (simpleDatagen instanceof BlockItemDatagen blockItemSimpleDatagen) {
                BlockItemRegistryPair blockItemPair = blockItemSimpleDatagen.getBlockItemRegistryPair();
                Block block = blockItemPair.block();
                Item item = blockItemPair.item();
                switch (simpleDatagen.getType()) {
                    case ORE -> {
                        OreDatagen oreDatagen = (OreDatagen) blockItemSimpleDatagen;
                        RegistryObject<Item> oreDropRegistryObject = oreDatagen.getOreDrop();
                        Item oreDrop = oreDropRegistryObject.get();
                        List<ItemLike> inputs = List.of(item);
                        String oreDropPath = oreDropRegistryObject.getId().getPath();
                        oreSmelting(pWriter, inputs, RecipeCategory.MISC, oreDrop, 0.7f, 200, oreDropPath);
                        oreBlasting(pWriter, inputs, RecipeCategory.MISC, oreDrop, 0.7f, 100, oreDropPath);
                    }
                    case FLOWER -> {
                        FlowerDatagen flowerDatagen = (FlowerDatagen) blockItemSimpleDatagen;
                        Item dyeItem = flowerDatagen.getDyeItem();
                        dyeRecipe(block, dyeItem, 1);
                    }
                    case LARGE_FLOWER -> {
                        FlowerDatagen flowerDatagen = (FlowerDatagen) blockItemSimpleDatagen;
                        Item dyeItem = flowerDatagen.getDyeItem();
                        dyeRecipe(block, dyeItem, 2);
                    }
                }
            }
        }
    }

    private void blockSets(BlockSet blockSet) {
        ItemLike baseBlock = blockSet.getBaseBlock();
        if (baseBlock == null)
            return;
        if (blockSet.getStairs() != null) {
            ItemLike slabBlock = blockSet.getStairs().item();
            stairsRecipe(baseBlock, slabBlock);
        }
        if (blockSet.getSlab() != null) {
            ItemLike stairsBlock = blockSet.getSlab().item();
            slabRecipe(baseBlock, stairsBlock);
        }
        if (blockSet.getWall() != null) {
            ItemLike wallBlock = blockSet.getWall().item();
            wallRecipe(baseBlock, wallBlock);
        }
        if (blockSet.getFence() != null) {
            ItemLike fenceBlock = blockSet.getFence().item();
            fenceRecipe(baseBlock, fenceBlock);
        }
        if (blockSet.getDoor() != null) {
            ItemLike doorBlock = blockSet.getDoor().item();
            doorRecipe(baseBlock, doorBlock);
        }
        if (blockSet.getTrapdoor() != null) {
            ItemLike trapdoorBlock = blockSet.getTrapdoor().item();
            doorRecipe(baseBlock, trapdoorBlock);
        }
        if (blockSet.getPressurePlate() != null) {
            ItemLike pressurePlate = blockSet.getPressurePlate().item();
            ItemLike slab = blockSet.getSlab().item();
            pressurePlateRecipe(baseBlock, slab, pressurePlate);
        }
        if (blockSet.getButton() != null) {
            ItemLike buttonBlock = blockSet.getButton().item();
            buttonRecipe(baseBlock, buttonBlock);
        }
        if (blockSet.getSign() != null) {
            ItemLike signItem = blockSet.getSign().item();
            signRecipe(baseBlock, signItem);
        }
        if (blockSet.getHangingSign() != null) {
            ItemLike hangingSignItem = blockSet.getHangingSign().item();
            hangingSignRecipe(baseBlock, blockSet.getStrippedLog().block(), hangingSignItem);
        }
        if (blockSet.getLog() != null) {
            planksFromLog(pWriter, baseBlock, blockSet.getLogsTag().getSecond(), 4);
        }
    }

    protected void chestplateRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("B B")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', material)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }

    protected void leggingsRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("BBB")
                .pattern("B B")
                .pattern("B B")
                .define('B', material)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }

    protected void bootsRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("B B")
                .pattern("B B")
                .define('B', material)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }

    protected void helmetRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern("BBB")
                .pattern("B B")
                .define('B', material)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }

    protected void pickaxeRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }


    protected void axeRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("BB")
                .pattern("BS")
                .pattern(" S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }

    protected void shovelRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("B")
                .pattern("S")
                .pattern("S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }

    protected void swordRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("B")
                .pattern("B")
                .pattern("S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }


    protected void hoeRecipe(Item material, Item output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, output, 1)
                .pattern("BB")
                .pattern(" S")
                .pattern(" S")
                .define('B', material)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material))
                .save(pWriter);
    }

    protected void slabRecipe(ItemLike baseBlock, ItemLike slabBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabBlock, 4)
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabBlock, 6)
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }

    protected void dyeRecipe(ItemLike baseBlock, ItemLike dyeItem, int output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, dyeItem, output)
                .requires(baseBlock, 1)
                .unlockedBy(getHasName(baseBlock), has(baseBlock))
                .save(pWriter);

    }

    protected void buttonRecipe(ItemLike baseBlock, ItemLike buttonBlock) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, buttonBlock, 1)
                    .requires(baseBlock, 1)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);

    }

    protected void signRecipe(ItemLike baseBlock, ItemLike signItem) {
        if (ModList.get().isLoaded("detour") ) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 1)
                    .pattern("B")
                    .pattern("S")
                    .define('S', Items.STICK)
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 3)
                    .pattern("BBB")
                    .pattern("BBB")
                    .pattern(" S ")
                    .define('S', Items.STICK)
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }

    protected void hangingSignRecipe(ItemLike baseBlock, @Nullable ItemLike strippedLogBlock, ItemLike signItem) {
        if (ModList.get().isLoaded("detour") ) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 1)
                    .pattern("C")
                    .pattern("B")
                    .define('C', Items.CHAIN)
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        } else if (strippedLogBlock != null){
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, signItem, 6)
                    .pattern("C C")
                    .pattern("BBB")
                    .pattern("BBB")
                    .define('C', Items.CHAIN)
                    .define('B', strippedLogBlock)
                    .unlockedBy(getHasName(strippedLogBlock), has(strippedLogBlock))
                    .save(pWriter);
        }
    }

    protected void pressurePlateRecipe(ItemLike baseBlock, @Nullable ItemLike slabBlock, ItemLike pressurePlate) {
        if (ModList.get().isLoaded("detour") && slabBlock != null) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pressurePlate, 1)
                    .requires(slabBlock, 1)
                    .unlockedBy(getHasName(slabBlock), has(slabBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pressurePlate, 1)
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }

    protected void trapdoorRecipe(ItemLike baseBlock, @Nullable ItemLike slabBlock, ItemLike trapdoorBlock) {
        if (ModList.get().isLoaded("detour")  && slabBlock != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, trapdoorBlock, 1)
                    .pattern("BB")
                    .define('B', slabBlock)
                    .unlockedBy(getHasName(slabBlock), has(slabBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, trapdoorBlock, 2)
                    .pattern("BBB")
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }

    protected void doorRecipe(ItemLike baseBlock, ItemLike doorBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, doorBlock, 2)
                    .pattern("BB")
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, doorBlock, 3)
                    .pattern("BB")
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }

    protected void stairsRecipe(ItemLike baseBlock, ItemLike stairsBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsBlock, 4)
                    .pattern("B  ")
                    .pattern("BB ")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsBlock, 4)
                    .pattern("B  ")
                    .pattern("BB ")
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }

    protected void woodRecipe(ItemLike logBlock, ItemLike woodBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodBlock, 4)
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', logBlock)
                    .unlockedBy(getHasName(logBlock), has(logBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodBlock, 3)
                    .pattern("BB")
                    .pattern("BB")
                    .define('B', logBlock)
                    .unlockedBy(getHasName(logBlock), has(logBlock))
                    .save(pWriter);
        }
    }

    protected void fenceRecipe(ItemLike baseBlock, ItemLike wallBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 4)
                    .pattern("BS")
                    .pattern("BS")
                    .define('B', baseBlock)
                    .define('S', Items.STICK)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 3)
                    .pattern("BSB")
                    .pattern("BSB")
                    .define('B', baseBlock)
                    .define('S', Items.STICK)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }

    protected void wallRecipe(ItemLike baseBlock, ItemLike wallBlock) {
        if (ModList.get().isLoaded("detour")) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 2)
                    .pattern("B")
                    .pattern("B")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wallBlock, 6)
                    .pattern("BBB")
                    .pattern("BBB")
                    .define('B', baseBlock)
                    .unlockedBy(getHasName(baseBlock), has(baseBlock))
                    .save(pWriter);
        }
    }


    protected void crackedRecipe(ItemLike baseBlock, ItemLike crackedBlock) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(baseBlock), RecipeCategory.BUILDING_BLOCKS, crackedBlock, 0.1f, 200)
                .unlockedBy(getHasName(baseBlock), has(baseBlock));
    }
}
