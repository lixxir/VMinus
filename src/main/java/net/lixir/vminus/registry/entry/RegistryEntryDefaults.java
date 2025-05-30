package net.lixir.vminus.registry.entry;

import javax.annotation.Nullable;

public interface RegistryEntryDefaults<E extends RegistryEntry<E>> {
    default @Nullable E vminus$getDefault() {
        return null;
    }
}
