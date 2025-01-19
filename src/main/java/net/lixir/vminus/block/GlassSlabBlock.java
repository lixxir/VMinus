package net.lixir.vminus.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlassSlabBlock extends SlabBlock {
    public GlassSlabBlock(Properties p_56359_) {
        super(p_56359_);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState currentState, BlockState neighborState,
                                 Direction direction) {
        Block neighborBlock = neighborState.getBlock();

        if (neighborBlock == Blocks.GLASS) {
            return true;
        }

        if (neighborBlock == this && isInvisibleToGlassSlab(currentState, neighborState, direction)) {
            return true;
        }

        if (neighborBlock instanceof GlassStairsBlock && isInvisibleToGlassStairs(currentState, neighborState, direction)) {
            return true;
        }

        return super.skipRendering(currentState, neighborState, direction);
    }

    private boolean isInvisibleToGlassSlab(BlockState currentState, BlockState neighborState, Direction direction) {
        SlabType currentSlabType = currentState.getValue(SlabBlock.TYPE);
        SlabType neighborSlabType = neighborState.getValue(SlabBlock.TYPE);

        switch (direction) {
            case UP:
                return (neighborSlabType != SlabType.TOP && currentSlabType != SlabType.BOTTOM);
            case DOWN:
                return (neighborSlabType != SlabType.BOTTOM && currentSlabType != SlabType.TOP);
            case NORTH:
            case EAST:
            case SOUTH:
            case WEST:
                return (currentSlabType == neighborSlabType || neighborSlabType == SlabType.DOUBLE);
            default:
                return false;
        }
    }

    private boolean isInvisibleToGlassStairs(BlockState currentState, BlockState neighborState, Direction direction) {
        SlabType currentSlabType = currentState.getValue(SlabBlock.TYPE);
        Half neighborHalf = neighborState.getValue(StairBlock.HALF);
        Direction neighborFacing = neighborState.getValue(StairBlock.FACING);

        if (direction == Direction.UP && neighborHalf == Half.BOTTOM) {
            return true;
        }

        if (direction == Direction.DOWN && neighborHalf == Half.TOP) {
            return true;
        }

        if (neighborFacing == direction.getOpposite()) {
            return true;
        }

        if (direction.get2DDataValue() != -1) {
            if ((currentSlabType == SlabType.BOTTOM && neighborHalf == Half.BOTTOM) ||
                    (currentSlabType == SlabType.TOP && neighborHalf == Half.TOP)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }
}
