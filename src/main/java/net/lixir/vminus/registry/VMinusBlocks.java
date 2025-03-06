package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.util.setup.SetupRegistries;
import net.lixir.vminus.util.setup.SetupTint;
import net.lixir.vminus.util.setup.block.BlockSetup;
import net.lixir.vminus.util.setup.block.BlockSetupModel;
import net.lixir.vminus.util.setup.block.BlockSetupType;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.lixir.vminus.registry.VMinusItems.ITEMS;

public class VMinusBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VMinus.ID);
    /*

    public static final BlockItemRegistryPair TEST_BLOCK2 = BlockItemRegistryPair.create(BLOCKS, ITEMS, "test_block2",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    static {
        SetupRegistries.BLOCKS.register(VMinus.ID, new BlockSetup.Builder(TEST_BLOCK2, BlockSetupType.WALL).baseBlock(Blocks.STONE).build());
    }

    */

    //public static final RegistryObject<Block> ALGAE = BLOCKS.register("algae", () -> new Block(BlockBehaviour.Properties.of()));

    // Datagen registry
    static {
        //setupBlock(new BlockSetup.Builder(ALGAE).model(BlockSetupModel.FLAT_LAYER).tint(SetupTint.GRASS).renderType(RenderType.cutout()).build());
    }

    private static void setupBlock(BlockSetup BlockSetup) {
        SetupRegistries.BLOCKS.register(VMinus.ID, BlockSetup);
    }

}
