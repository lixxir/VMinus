package net.lixir.vminus.registry.entry;

import net.lixir.vminus.registry.VRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.loading.DatagenModLoader;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class RegistryEntry<E extends RegistryEntry<E,T>, T> {
    public static final ResourceLocation UNSET_RESOURCE_LOCATION = new ResourceLocation("minecraft", "unset");
    protected String lang = "default";
    protected boolean isDefaulted;

    public @Nullable String getLang() {
        return lang;
    }

    public abstract E lang(String langValue);

    @SuppressWarnings("unchecked")
    public E setDefault(@NotNull T t) {
        E entry = (E) VRegistry.getRegistryEntry(t);
        merge(entry);
        return entry;
    }

    public boolean isDefaulted() {
        return isDefaulted;
    }

    public void setDefaulted(boolean isDefaulted) {
        this.isDefaulted = isDefaulted;
    }

    public static boolean isDatagen() {
        return DatagenModLoader.isRunningDataGen();
    }

    public abstract @NotNull E merge(E other);
}
