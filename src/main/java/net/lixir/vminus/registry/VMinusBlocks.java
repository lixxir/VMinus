package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.lixir.vminus.registry.VMinusItems.ITEMS;

public class VMinusBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VMinus.ID);
}
