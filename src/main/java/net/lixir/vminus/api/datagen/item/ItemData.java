package net.lixir.vminus.api.datagen.item;

import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.lixir.vminus.api.tint.TintType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper containing a {@link Item} and its
 * corresponding {@link ItemDefinition} for data generation processes.
 *
 * @param item           the item being processed
 * @param itemDefinition the definition associated with the item
 */
public record ItemData(@NotNull Item item, ItemDefinition itemDefinition) {

    /**
     * Creates a new {@link ItemData} instance from an item,
     * automatically resolving its {@link ItemDefinition}.
     *
     * @param item the {@link Item} to wrap
     * @return a new {@link ItemData} instance containing the item and its definition
     */
    public static @NotNull ItemData of(Item item) {
        return new ItemData(item, ItemDefinition.of(item));
    }
}