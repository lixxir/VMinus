package net.lixir.vminus.registry.entry;

import net.lixir.vminus.registry.UnifiedRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class RegistryEntry<E extends RegistryEntry<E,T>, T> {
    protected String langValue = "default";
    protected boolean isDefaulted;

    public @Nullable String getLangValue() {
        return langValue;
    }

    public abstract E lang(String langValue);

    @SuppressWarnings("unchecked")
    public E setDefault(@NotNull T t) {
        E entry = (E) UnifiedRegistry.getRegistryEntry(t.getClass());
        merge((E) this, entry);
        return entry;
    }

    public boolean isDefaulted() {
        return isDefaulted;
    }

    abstract void merge(E self, E other);
}
