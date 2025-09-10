package net.lixir.vminus.api.registry.definition.group;

import net.lixir.vminus.api.registry.definition.RegistryDefinition;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a group that wraps a single {@link RegistryDefinition}.
 * <p>
 * Provides a way to manage or replace a definition while enforcing that
 * defaulted definitions cannot be added to a group. Useful for organizing
 * definitions in collections or chains.
 *
 * @param <T> the type of the underlying object (e.g., Block, Item)
 */
public class DefinitionGroup<T> {
    protected static final String DEFAULTED_EXCEPTION_MESSAGE = "Definition groups cannot contain defaulted definitions!";
    private @NotNull RegistryDefinition<?, T> definition;

    /** Private constructor; use {@link #of(RegistryDefinition)} to instantiate. */
    private DefinitionGroup(@NotNull RegistryDefinition<?, T> definition) {
        this.definition = definition;
    }

    /**
     * Creates a new {@link DefinitionGroup} for a given definition.
     * <p>
     * Throws an exception if the definition is marked as defaulted.
     *
     * @param definition the definition to wrap
     * @param <E> the specific type of definition
     * @param <T> the type of the underlying object
     * @return a new DefinitionGroup wrapping the given definition
     * @throws IllegalArgumentException if the definition is defaulted
     */
    @Contract(value = "_ -> new", pure = true)
    public static <E extends RegistryDefinition<E, T>, T> @NotNull DefinitionGroup<T> of(@NotNull RegistryDefinition<E, T> definition) {
        if (definition.isDefaulted()) // Should not default because it is going to try and pull from default values and that gets messy.
            throw new IllegalArgumentException(DEFAULTED_EXCEPTION_MESSAGE);
        return new DefinitionGroup<>(definition);
    }

    /**
     * Returns the current definition contained in this group.
     *
     * @return the wrapped definition
     */
    public @NotNull RegistryDefinition<?, T> getDefinition() {
        return definition;
    }

    /**
     * Replaces the current definition with a new one.
     * <p>
     * Throws an exception if the new definition is marked as defaulted.
     *
     * @param definition the new definition to wrap
     * @param <E> the specific type of definition
     * @return this DefinitionGroup after updating the definition
     * @throws IllegalArgumentException if the new definition is defaulted
     */
    public <E extends RegistryDefinition<E, T>> DefinitionGroup<T> replace(@NotNull RegistryDefinition<E, T> definition) {
        if (definition.isDefaulted()) {
            throw new IllegalArgumentException("Definition Groups cannot contain defaulted definitions!");
        }
        this.definition = DefinitionGroup.of(definition).definition;
        return this;
    }

    @Override
    public String toString() {
        return "DefinitionGroup{" + "entry=" + definition + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DefinitionGroup<?> that = (DefinitionGroup<?>) o;
        return definition.equals(that.definition);
    }

    @Override
    public int hashCode() {
        return definition.hashCode();
    }
}
