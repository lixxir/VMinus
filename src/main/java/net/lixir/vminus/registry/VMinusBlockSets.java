package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static net.lixir.vminus.registry.VMinusBlocks.BLOCKS;
import static net.lixir.vminus.registry.VMinusItems.ITEMS;

public class VMinusBlockSets {
    public static void initialize() {}


    /*
    public static final BlockSet TEST = new BlockSet.Builder(VMinus.ID, "test_block", BlockBehaviour.Properties.copy(Blocks.STONE), BLOCKS, ITEMS)
                .wall()
                //.texture(new ResourceLocation("minecraft", "block/magma"))
                .creativeTab(Items.OAK_BUTTON, BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.BUILDING_BLOCKS))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .build();

     */



}
