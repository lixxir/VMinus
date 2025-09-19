
package net.lixir.vminus.api.datagen.block.loottable;

import net.lixir.vminus.api.datagen.block.BlockData;
import net.lixir.vminus.api.datagen.block.loottable.provider.VBlockLootProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;

import java.util.function.BiConsumer;

/**
 * Default VMinus-provided block loot table types.
 */
public enum BuiltInBlockLootTableTypes implements BlockLootTableType {
    UNSET("unset", (data, provider) -> {}),
    NONE("none", (data, provider) -> {}),
    SHEARS("shears", (data, provider) -> provider.shears(data.block())),
    DOUBLE_PLANT_SHEARS("double_plant_shears", (data, provider) -> provider.doublePlantShears(data.block())),
    SELF("self", (data, provider) -> provider.self(data.block())),
    DOUBLE_FLOWER("double_flower", (data, provider) -> provider.doubleFlower((DoublePlantBlock) data.block())),
    PINK_PETALS("pink_petals", (data, provider) -> provider.pinkPetals((PinkPetalsBlock) data.block())),
    PINK_PETALS_SHEARS("pink_petals_shears", (data, provider) -> provider.pinkPetalsShears((PinkPetalsBlock) data.block()));

    private final String name;
    private final BiConsumer<BlockData, VBlockLootProvider> consumer;

    BuiltInBlockLootTableTypes(String name, BiConsumer<BlockData, VBlockLootProvider> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    @Override
    public void apply(BlockData blockData, VBlockLootProvider provider) {
        consumer.accept(blockData, provider);
    }

    @Override
    public BiConsumer<BlockData, VBlockLootProvider> getConsumer() {
        return consumer;
    }

    @Override
    public String getName() {
        return name;
    }
}
