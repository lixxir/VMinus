package net.lixir.vminus.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockStateUtils {
    public static <T extends Comparable<T>> @NotNull BlockState reverseCycle(@NotNull BlockState state, @NotNull Property<T> property) {
        Collection<T> values = property.getPossibleValues();
        List<T> list = values instanceof List ? (List<T>) values : new ArrayList<>(values);
        T current = state.getValue(property);
        int index = list.indexOf(current);
        int prevIndex = (index - 1 + list.size()) % list.size();
        return state.setValue(property, list.get(prevIndex));
    }

    public static BlockState copyProperties(@NotNull BlockState from, BlockState to) {
        for (Property<?> property : from.getProperties()) {
            if (to.hasProperty(property)) {
                to = copyProperty(from, to, property);
            }
        }
        return to;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> @NotNull BlockState copyProperty(@NotNull BlockState from, @NotNull BlockState to, Property<?> property) {
        return to.setValue((Property<T>) property, from.getValue((Property<T>) property));
    }
}
