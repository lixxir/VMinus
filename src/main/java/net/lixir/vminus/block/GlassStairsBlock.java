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
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class GlassStairsBlock extends StairBlock {

    public GlassStairsBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        Block blockFrom = stateFrom.getBlock();

        if (blockFrom == Blocks.GLASS) {
            return true;
        }

        if (blockFrom instanceof GlassSlabBlock) {
            return isInvisibleToGlassSlab(state, stateFrom, direction);
        }

        if (blockFrom == this) {
            return isInvisibleToGlassStairs(state, stateFrom, direction);
        }

        return super.skipRendering(state, stateFrom, direction);
    }

    private boolean isInvisibleToGlassSlab(BlockState state, BlockState stateFrom, Direction direction) {
        SlabType typeFrom = stateFrom.getValue(SlabBlock.TYPE);
        Half half = state.getValue(StairBlock.HALF);
        Direction facing = state.getValue(StairBlock.FACING);
        StairsShape shape = state.getValue(StairBlock.SHAPE);

        if (typeFrom == SlabType.DOUBLE) {
            return true;
        }

        switch (direction) {
            case UP:
                return typeFrom != SlabType.TOP;
            case DOWN:
                return typeFrom != SlabType.BOTTOM;
            case NORTH:
            case EAST:
            case SOUTH:
            case WEST:
                if (direction == facing.getOpposite()) {
                    return checkFront(typeFrom, half);
                }
                if (direction == facing.getClockWise() && shape == StairsShape.OUTER_LEFT) {
                    return checkRight(typeFrom, half);
                }
                if (direction == facing.getCounterClockWise() && shape == StairsShape.OUTER_RIGHT) {
                    return checkLeft(typeFrom, half);
                }
                break;
        }

        return false;
    }

    private boolean checkFront(SlabType typeFrom, Half half) {
        return (typeFrom == SlabType.BOTTOM && half == Half.BOTTOM) ||
                (typeFrom == SlabType.TOP && half == Half.TOP);
    }

    private boolean checkRight(SlabType typeFrom, Half half) {
        return (typeFrom == SlabType.BOTTOM && half == Half.BOTTOM) ||
                (typeFrom == SlabType.TOP && half == Half.TOP);
    }

    private boolean checkLeft(SlabType typeFrom, Half half) {
        return (typeFrom == SlabType.BOTTOM && half == Half.BOTTOM) ||
                (typeFrom == SlabType.TOP && half == Half.TOP);
    }

    private boolean isInvisibleToGlassStairs(BlockState state, BlockState stateFrom, Direction direction) {
        Half half = state.getValue(StairBlock.HALF);
        Half halfFrom = stateFrom.getValue(StairBlock.HALF);
        Direction facing = state.getValue(StairBlock.FACING);
        Direction facingFrom = stateFrom.getValue(StairBlock.FACING);
        StairsShape shape = state.getValue(StairBlock.SHAPE);
        StairsShape shapeFrom = stateFrom.getValue(StairBlock.SHAPE);

        if (direction == Direction.UP) {
            return checkUp(half, halfFrom, facing, facingFrom, shape, shapeFrom);
        }
        if (direction == Direction.DOWN) {
            return checkDown(half, halfFrom, facing, facingFrom, shape, shapeFrom);
        }

        if (facing == facingFrom) {
            if (direction == facing.getCounterClockWise() || direction == facing.getClockWise()) {
                return shape == shapeFrom || (shape == StairsShape.STRAIGHT && shapeFrom == StairsShape.STRAIGHT);
            }
        }

        if (facingFrom == direction.getOpposite()) {
            return shape != StairsShape.OUTER_LEFT && shape != StairsShape.OUTER_RIGHT;
        }

        return checkCurvedSides(facingFrom, direction, shapeFrom);
    }

    private boolean checkUp(Half half, Half halfFrom, Direction facing, Direction facingFrom, StairsShape shape, StairsShape shapeFrom) {
        if (halfFrom == Half.BOTTOM) {
            return true;
        }

        if (half != halfFrom) {
            if (facing == facingFrom && shape == shapeFrom) {
                return true;
            }
            return checkShapeCompatibility(shape, shapeFrom, facing, facingFrom);
        }

        return false;
    }

    private boolean checkDown(Half half, Half halfFrom, Direction facing, Direction facingFrom, StairsShape shape, StairsShape shapeFrom) {
        if (halfFrom == Half.TOP) {
            return true;
        }

        return checkShapeCompatibility(shape, shapeFrom, facing, facingFrom);
    }

    private boolean checkShapeCompatibility(StairsShape shape, StairsShape shapeFrom, Direction facing, Direction facingFrom) {
        switch (shape) {
            case STRAIGHT:
                return isStraightCompatible(shapeFrom, facing, facingFrom);
            case INNER_LEFT:
                return shapeFrom == StairsShape.INNER_RIGHT && facingFrom == facing.getCounterClockWise();
            case INNER_RIGHT:
                return shapeFrom == StairsShape.INNER_LEFT && facingFrom == facing.getClockWise();
            case OUTER_LEFT:
                return isOuterLeftCompatible(shapeFrom, facing, facingFrom);
            case OUTER_RIGHT:
                return isOuterRightCompatible(shapeFrom, facing, facingFrom);
            default:
                return false;
        }
    }

    private boolean isStraightCompatible(StairsShape shapeFrom, Direction facing, Direction facingFrom) {
        return (shapeFrom == StairsShape.INNER_LEFT && (facingFrom == facing || facingFrom == facing.getClockWise())) ||
                (shapeFrom == StairsShape.INNER_RIGHT && (facingFrom == facing || facingFrom == facing.getCounterClockWise()));
    }

    private boolean isOuterLeftCompatible(StairsShape shapeFrom, Direction facing, Direction facingFrom) {
        if (shapeFrom == StairsShape.OUTER_RIGHT && facingFrom == facing.getCounterClockWise()) {
            return true;
        }
        if (shapeFrom == StairsShape.STRAIGHT || facingFrom == facing) {
            return true;
        }
        return (shapeFrom == StairsShape.INNER_RIGHT && facingFrom == facing.getCounterClockWise());
    }
    private boolean isOuterRightCompatible(StairsShape shapeFrom, Direction facing, Direction facingFrom) {
        if (shapeFrom == StairsShape.OUTER_LEFT && facingFrom == facing.getClockWise()) {
            return true;
        }
        if (shapeFrom == StairsShape.STRAIGHT || facingFrom == facing) {
            return true;
        }
        return (shapeFrom == StairsShape.INNER_LEFT && facingFrom == facing.getClockWise());
    }

    private boolean checkCurvedSides(Direction facingFrom, Direction direction, StairsShape shapeFrom) {
        if (shapeFrom == StairsShape.INNER_RIGHT && facingFrom.getCounterClockWise() == direction) {
            return true;
        }
        if (shapeFrom == StairsShape.INNER_LEFT && facingFrom.getClockWise() == direction) {
            return true;
        }
        if (shapeFrom == StairsShape.OUTER_LEFT && facingFrom.getClockWise() == direction) {
            return true;
        }
        return shapeFrom == StairsShape.OUTER_RIGHT && facingFrom.getCounterClockWise() == direction;
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
