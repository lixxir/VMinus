package net.lixir.vminus.api.registry.definition;

import net.lixir.vminus.api.datagen.lang.LangKey;
import net.lixir.vminus.api.registry.VRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.loading.DatagenModLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Base class for defining additional metadata, datagen information, or
 * configuration for registry objects such as blocks, items, or entities.
 * <p>
 * This class is generic to allow type-safe chaining and merging of definitions.
 *
 * @param <E> the concrete subclass type (used for fluent chaining)
 * @param <T> the type of the underlying object being defined (e.g., Block, Item)
 */
public abstract class RegistryDefinition<E extends RegistryDefinition<E, T>, T> {
    public static final ResourceLocation UNSET_RESOURCE_LOCATION = new ResourceLocation("minecraft", "unset");
    protected LangKey langKey = LangKey.DEFAULT;
    protected boolean isDefaulted;

    /**
     * Returns whether datagen is currently running.
     *
     * @return true if datagen is running, false otherwise
     */
    public static boolean isDatagen() {
        return DatagenModLoader.isRunningDataGen();
    }

    public @NotNull LangKey getLangKey() {
        return langKey;
    }

    /**
     * Sets the language key for this definition, but only when data generation is running.
     * <p>
     * During normal runtime, this method does not modify the existing language key.
     *
     * @param langKey the new {@link LangKey} to assign
     * @return this definition for fluent chaining
     */
    @SuppressWarnings("unchecked")
    public E langKey(@NotNull LangKey langKey) {
        if (isDatagen()) {
            this.langKey = langKey;
        }
        return (E) this;
    }

    public E langKey(@NotNull String lang) {
       return langKey(LangKey.of(lang));
    }

    public boolean isDefaulted() {
        return isDefaulted;
    }

    /**
     * Applies default values from the global registry for the given object.
     * <p>
     * Retrieves the default definition from {@link VRegistry} and merges it
     * into this definition.
     *
     * @param t the underlying object to default
     * @return the merged definition
     */
    @SuppressWarnings("unchecked")
    public E setDefault(@NotNull T t) {
        E entry = (E) VRegistry.getDefaultDefinition(t);
        merge(entry);
        return entry;
    }

    /**
     * Marks this BlockDefinition as defaulted.
     * <p>
     * This is protected because it should only be used internally by RegistryDefinition logic
     * and not modified externally.
     */
    protected void setDefaulted() {
        this.isDefaulted = true;
    }

    /**
     * Merges another definition into this one.
     * <p>
     * Implementations should combine metadata, models, loot tables, etc.
     *
     * @param other the other definition to merge
     * @return this definition after merging
     */
    public abstract @NotNull E merge(E other);

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        RegistryDefinition<?, ?> other = (RegistryDefinition<?, ?>) obj;
        return isDefaulted == other.isDefaulted &&
                Objects.equals(langKey, other.langKey);
    }

    public abstract boolean isEmpty();

    @Override
    public int hashCode() {
        return Objects.hash(langKey, isDefaulted);
    }
}
