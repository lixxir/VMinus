package net.lixir.vminus.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record NumberRange<T extends Number & Comparable<T>>(T min, T max) {
    public NumberRange {
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException("Min cannot be greater than max");
    }

    public boolean contains(@NotNull T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Range[" + min + ", " + max + "]";
    }
}
