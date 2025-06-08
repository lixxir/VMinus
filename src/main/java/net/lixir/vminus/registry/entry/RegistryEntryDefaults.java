package net.lixir.vminus.registry.entry;

import javax.annotation.Nullable;

public interface RegistryEntryDefaults<E extends RegistryEntry<E, T>, T> {
    default @Nullable E vminus$getDefault() {
        return null;
    }
}
