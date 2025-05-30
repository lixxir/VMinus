package net.lixir.vminus.registry.entry;

import javax.annotation.Nullable;

public abstract class RegistryEntry<E> {
    protected String langValue = "default";

    public @Nullable String getLangValue() {
        return langValue;
    }

    public abstract E lang(String langValue);
}
