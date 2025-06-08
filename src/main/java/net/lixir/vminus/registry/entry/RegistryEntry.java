package net.lixir.vminus.registry.entry;

import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.minecraft.world.item.Item;
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
    public E setDefault(T t) {
        RegistryEntryDefaults<E, T> accessor = (RegistryEntryDefaults<E, T>) t;
        E entry = accessor.vminus$getDefault();
        merge((E) this, entry);
        return entry;
    }

    public boolean isDefaulted() {
        return isDefaulted;
    }

    abstract void merge(E self, E other);
}
