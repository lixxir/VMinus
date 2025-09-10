package net.lixir.vminus.api.registry.definition.duck;

import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

/**
 * A duck interface for attaching an {@link ItemDefinition} to an {@link net.minecraft.world.item.Item}.
 * <p>
 * This allows VMinus to store metadata or definition information on items without
 * modifying the original {@link net.minecraft.world.item.Item} class.
 */
public interface ItemDefinitionDuck extends RegistryDefinitionDuck<ItemDefinition, Item> {
    @Override
    void vMinus$setDefinition(@Nullable ItemDefinition itemEntry);

    @Nullable
    @Override
    ItemDefinition vMinus$getDefinition();

    static ItemDefinitionDuck of(Item item) {
        return (ItemDefinitionDuck) item;
    }
}