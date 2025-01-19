
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.registry;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.block.entity.ModHangingSignBlockEntity;
import net.lixir.vminus.block.entity.ModSignBlockEntity;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class VMinusBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, VMinusMod.MODID);
	public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> MOD_SIGN =
			BLOCK_ENTITIES.register("mod_sign", () -> {
				List<Block> signBlocks = BlockSet.BLOCK_SETS.stream()
						.filter(set -> set.hasSign())
						.flatMap(set -> Stream.of(set.getStandingSignBlock(), set.getWallSignBlock()))
						.filter(Objects::nonNull)
						.toList();

				return BlockEntityType.Builder.of(ModSignBlockEntity::new, signBlocks.toArray(new Block[0])).build(null);
			});


	public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> MOD_HANGING_SIGN =
			BLOCK_ENTITIES.register("hanging_mod_sign", () -> {
				List<Block> hangingSignBlocks = BlockSet.BLOCK_SETS.stream()
						.flatMap(set -> Stream.of(set.getHangingSignBlock(), set.getWallHangingSignBlock()))
						.filter(Objects::nonNull)
						.toList();
				return BlockEntityType.Builder.of(ModHangingSignBlockEntity::new, hangingSignBlocks.toArray(new Block[0])).build(null);
			});

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return BLOCK_ENTITIES.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
