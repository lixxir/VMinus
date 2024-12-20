
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.registry;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.block.DefaultBlockBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VMinusBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, VMinusMod.MODID);
    public static final RegistryObject<Block> DEFAULT_BLOCK = REGISTRY.register("default_block", () -> new DefaultBlockBlock());
    // Start of user code block custom blocks
    // End of user code block custom blocks
}
