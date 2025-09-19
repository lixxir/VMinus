package net.lixir.vminus.api.registry.definition.duck;

import net.lixir.vminus.api.registry.definition.EntityDefinition;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;

/**
 * A duck interface for attaching an {@link EntityDefinition} to an {@link EntityType}.
 * <p>
 * This allows VMinus to store metadata or definition information on entity types
 * without modifying the original {@link EntityType} class.
 */
public interface EntityDefinitionDuck extends RegistryDefinitionDuck<EntityDefinition, EntityType<?>> {
    @Override
    void vMinus$setDefinition(@Nullable EntityDefinition entry);

    @Nullable
    @Override
    EntityDefinition vMinus$getDefinition();

    static EntityDefinitionDuck of(EntityType<?> entityType) {
        return (EntityDefinitionDuck) entityType;
    }
}