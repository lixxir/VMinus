package net.lixir.vminus.api.block.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility methods for working with {@link BlockState} instances,
 * such as cycling properties or copying values between states.
 */
public class BlockStateUtils {
    /**
     * Cycles the value of the given property backwards (in reverse order)
     * for the provided {@link BlockState}.
     * <p>
     * For example, if a property has possible values {@code [NORTH, EAST, SOUTH, WEST]}
     * and the current value is {@code EAST}, then calling this method will
     * return a state with {@code NORTH}.
     *
     * @param state    the block state to modify
     * @param property the property to reverse-cycle
     * @param <T>      the type of the property
     * @return a new {@link BlockState} with the property cycled backwards
     */
    public static <T extends Comparable<T>> @NotNull BlockState reverseCycle(
            @NotNull BlockState state,
            @NotNull Property<T> property
    ) {
        Collection<T> values = property.getPossibleValues();
        List<T> list = values instanceof List ? (List<T>) values : new ArrayList<>(values);
        T current = state.getValue(property);
        int index = list.indexOf(current);
        int prevIndex = (index - 1 + list.size()) % list.size();
        return state.setValue(property, list.get(prevIndex));
    }

    /**
     * Copies all property values from one {@link BlockState} to another,
     * where the target state supports the same property.
     * <p>
     * Any properties in {@code from} that are not present in {@code to}
     * will be skipped.
     *
     * @param from the source state to copy properties from
     * @param to   the target state to apply values to
     * @return a new {@link BlockState} with values copied where applicable
     */
    public static BlockState copyProperties(@NotNull BlockState from, BlockState to) {
        for (Property<?> property : from.getProperties()) {
            if (to.hasProperty(property)) {
                to = copyProperty(from, to, property);
            }
        }
        return to;
    }

    /**
     * Copies a single property value from one {@link BlockState} to another.
     * <p>
     * This method assumes that both states share the same property definition.
     *
     * @param from     the source state containing the property value
     * @param to       the target state to apply the property value to
     * @param property the property to copy
     * @param <T>      the type of the property
     * @return a new {@link BlockState} with the property copied
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> @NotNull BlockState copyProperty(
            @NotNull BlockState from,
            @NotNull BlockState to,
            Property<?> property
    ) {
        return to.setValue((Property<T>) property, from.getValue((Property<T>) property));
    }
}
