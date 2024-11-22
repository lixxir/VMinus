package net.lixir.vminus.helpers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class DirectionHelper {
    public static Direction getDirectionFromString(String directionString) {
        switch (directionString.toLowerCase()) {
            case "up":
                return Direction.UP;
            case "down":
                return Direction.DOWN;
            case "north":
                return Direction.NORTH;
            case "south":
                return Direction.SOUTH;
            case "west":
                return Direction.WEST;
            case "east":
                return Direction.EAST;
            default:
                return null;
        }
    }

    public static BlockState applyDirectionToBlockState(BlockState state, Direction direction) {
        Property<?> property = state.getBlock().getStateDefinition().getProperty("facing");
        if (property instanceof DirectionProperty directionProperty && directionProperty.getPossibleValues().contains(direction)) {
            return state.setValue(directionProperty, direction);
        } else {
            property = state.getBlock().getStateDefinition().getProperty("axis");
            if (property instanceof EnumProperty<?> enumProperty && enumProperty.getPossibleValues().contains(direction.getAxis())) {
                @SuppressWarnings("unchecked")
                EnumProperty<Direction.Axis> axisProperty = (EnumProperty<Direction.Axis>) enumProperty;
                return state.setValue(axisProperty, direction.getAxis());
            }
        }
        return null;
    }
}
