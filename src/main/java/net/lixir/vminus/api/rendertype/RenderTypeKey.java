package net.lixir.vminus.api.rendertype;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a wrapper for block render type identifiers.
 * <p>
 * This class provides utility methods for working with render type strings,
 * including common states such as {@code unset} or {@code none}.
 * It also defines some built-in constants for vanilla Minecraft render type IDs,
 * making comparisons and validation more convenient.
 */
public record RenderTypeKey(@NotNull String key) {
    public static final RenderTypeKey UNSET = new RenderTypeKey("unset");
    public static final RenderTypeKey SOLID = new RenderTypeKey("solid");
    public static final RenderTypeKey CUTOUT = new RenderTypeKey("cutout");
    public static final RenderTypeKey CUTOUT_MIPPED = new RenderTypeKey("cutout_mipped");
    public static final RenderTypeKey TRANSLUCENT = new RenderTypeKey("translucent");

    /**
     * @return true if this render type is {@link #UNSET}
     */
    public boolean isUnset() {
        return this == UNSET;
    }

    /**
     * @return the raw string value
     */
    public @NotNull String key() {
        return key;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RenderTypeKey that))
            return false;
        return key.equals(that.key);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "RenderTypeKey{" + key + "}";
    }

    /**
     * Factory method to handle defaulting unset or empty values
     */
    private static @NotNull RenderTypeKey of(@Nullable String name) {
        if (name == null || name.isEmpty() || "unset".equalsIgnoreCase(name))
            return UNSET;
        return new RenderTypeKey(name);
    }
}
