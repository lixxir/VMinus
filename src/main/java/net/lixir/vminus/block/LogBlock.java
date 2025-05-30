package net.lixir.vminus.block;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.RegistryEntryDefaults;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class LogBlock extends RotatedPillarBlock implements RegistryEntryDefaults<BlockEntry> {
    public LogBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public BlockEntry vminus$getDefault() {
        BlockEntry blockEntry = BlockEntry.of();
        blockEntry.model(BlockEntry.Model.AXIS);
        blockEntry.tags(List.of(BlockTags.LOGS));
        return blockEntry;
    }
}