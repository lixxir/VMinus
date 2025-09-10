package net.lixir.vminus.api.registry.definition.duck;

import net.lixir.vminus.api.registry.definition.RegistryDefinition;

import javax.annotation.Nullable;

/**
 * A generic duck interface for attaching a {@link RegistryDefinition} to any registry-backed object.
 * <p>
 * This enables VMinus to associate metadata, datagen, or other definition data with objects
 * (e.g., blocks, items, entities) without modifying their original classes.
 *
 * @param <T> the type of definition being attached (e.g., BlockDefinition, ItemDefinition)
 * @param <E> the type of the object that the definition is associated with
 */
public interface RegistryDefinitionDuck<T extends RegistryDefinition<T, E>, E> {
    void vMinus$setDefinition(@Nullable T definition);
    @Nullable T vMinus$getDefinition();
}
