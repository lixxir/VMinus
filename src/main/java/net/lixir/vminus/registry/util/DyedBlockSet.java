package net.lixir.vminus.registry.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class DyedBlockSet {
    private final Map<DyeColor, BlockSet> blockSets = new EnumMap<>(DyeColor.class);


    public DyedBlockSet(Function<DyeColor, BlockSet.Builder> builderFunction, String targetItemId, CreativeModeTab creativeModeTab, BlockBehaviour.Properties properties) {
        registerDyes(builderFunction, targetItemId, creativeModeTab, properties);
    }

    public DyedBlockSet(Function<DyeColor, BlockSet.Builder> builderFunction, CreativeModeTab creativeModeTab) {
        registerDyes(builderFunction, "", creativeModeTab, null);
    }

    private void registerDyes(Function<DyeColor, BlockSet.Builder> builderFunction, String targetItemId, CreativeModeTab creativeModeTab,  BlockBehaviour.Properties properties) {
        for (DyeColor color : DyeColor.values()) {
            BlockSet.Builder builder = builderFunction.apply(color);
            if (builder != null) {
                String namespace = builder.getBaseBlockNamespaceId();

                String blockId = builder.getBlockId();
                Block block;
                if (blockId != null && namespace != null) {
                    block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(namespace, blockId));
                } else {
                    block = null;
                }

                if (block != null) {
                    BlockSetCreativeOrder blockSetCreativeOrder = new BlockSetCreativeOrder(creativeModeTab, block.asItem(), false);
                    builder.creativeTab(blockSetCreativeOrder);
                    builder.baseBlock(block);
                } else {
                    builder.properties(properties);
                    if (creativeModeTab != null) {

                        block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft", color.getName() + "_" + targetItemId));
                        if (block != null) {
                            BlockSetCreativeOrder blockSetCreativeOrder = new BlockSetCreativeOrder(creativeModeTab, block.asItem(), false);
                            builder.creativeTab(blockSetCreativeOrder);
                        }
                    }
                }



                blockSets.put(color, builder.build());
            }
        }
    }


    public BlockSet getBlockSet(DyeColor color) {
        return blockSets.get(color);
    }

    public Map<DyeColor, BlockSet> getAllBlockSets() {
        return blockSets;
    }
}
