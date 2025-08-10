package net.lixir.vminus.registry.entry.group;

import net.lixir.vminus.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RegistryEntryGroup<T> {
    private @NotNull RegistryEntry<?, T> entry;

    private RegistryEntryGroup(@NotNull RegistryEntry<?, T> entry) {
        this.entry = entry;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends RegistryEntry<E, T>, T> @NotNull RegistryEntryGroup<T> create(@NotNull RegistryEntry<E, T> entry) {
        if (entry.isDefaulted())
            throw new IllegalArgumentException("Registry Entry Groups can not be defaulted!");
        return new RegistryEntryGroup<>(entry);
    }

    public @NotNull RegistryEntry<?, T> getEntry() {
        return entry;
    }

    public RegistryEntryGroup<T> extend(@NotNull RegistryEntry<?, T> newEntry) {
        if (newEntry.isDefaulted())
            throw new IllegalArgumentException("Registry Entry Groups can not be defaulted!");
        this.entry = newEntry;
        return this;
    }


    @Override
    public String toString() {
        return "RegistryEntryGroup{" + "entry=" + (entry) +'}';
    }
}
