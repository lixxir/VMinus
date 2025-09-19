package net.lixir.vminus.api.registry.definition;

import net.lixir.vminus.api.datagen.lang.LangKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A definition that stores metadata, tags, and datagen customization
 * for {@link EntityType} objects.
 * <p>
 * This acts as a registry-side container for information associated with
 * an entity, such as its {@link TagKey tags}. It is primarily used for
 * data generation and flexible entity classification.
 */
public class EntityDefinition extends RegistryDefinition<EntityDefinition, EntityType<?>> {
    public static final EntityDefinition EMPTY = of();
    protected final Set<TagKey<EntityType<?>>> tags = new HashSet<>();

    /**
     * Creates a new, empty {@link EntityDefinition}.
     * Use {@link #of()} instead of direct construction.
     */
    protected EntityDefinition() {}

    /**
     * Creates a fresh, empty {@link EntityDefinition}.
     *
     * @return a new definition
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull EntityDefinition of() {
        return new EntityDefinition();
    }

    /**
     * Adds one or more {@link TagKey tags} to this entity definition.
     *
     * @param tags the tags to add
     * @return this definition (for chaining)
     */
    @SafeVarargs
    public final EntityDefinition tag(TagKey<EntityType<?>>... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    /**
     * @return an unmodifiable view of all tags applied to this entity
     */
    public @NotNull Set<TagKey<EntityType<?>>> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Merges this definition with another.
     * <p>
     * By default, {@link EntityDefinition} does not merge any state
     * and simply returns {@code this}. Override this method if
     * custom merge behavior is needed.
     *
     * @param other the other definition
     * @return this definition (unchanged)
     */
    @Override
    public @NotNull EntityDefinition merge(@Nullable EntityDefinition other) {
        if (other == null)
            return this;
        tags.addAll(other.tags);
        return super.merge(other);
    }

    /**
     * @return true if this definition is the sentinel {@link #EMPTY}
     */
    @Override
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EntityDefinition other))
            return false;

        return isDefaulted == other.isDefaulted &&
                tags.equals(other.tags) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + tags.hashCode();
        result = 31 * result + Boolean.hashCode(isDefaulted);
        return result;
    }
}
