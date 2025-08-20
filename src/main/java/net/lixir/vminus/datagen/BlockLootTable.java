package net.lixir.vminus.datagen;

import net.lixir.vminus.datagen.util.loottable.VBlockLootProvider;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class BlockLootTable {
    public static final BlockLootTable UNSET = new BlockLootTable("unset", (data, provider) -> {});
    public static final BlockLootTable NONE = new BlockLootTable("none", (data, provider) -> {});
    public static final BlockLootTable SHEARS = new BlockLootTable("shears", (data, provider) -> provider.shears(data.block()));
    public static final BlockLootTable DOUBLE_PLANT_SHEARS = new BlockLootTable("double_plant_shears", (data, provider) -> provider.doublePlantShears(data.block()));
    public static final BlockLootTable SELF = new BlockLootTable("self", (data, provider) -> provider.self(data.block()));
    public static final BlockLootTable DOUBLE_FLOWER = new BlockLootTable("double_flower", (data, provider) -> provider.doubleFlower((DoublePlantBlock) data.block()));
    public static final BlockLootTable PINK_PETALS = new BlockLootTable("pink_petals", (data, provider) -> provider.pinkPetals((PinkPetalsBlock) data.block()));
    public static final BlockLootTable PINK_PETALS_SHEARS = new BlockLootTable("pink_petals_shears", (data, provider) -> provider.pinkPetalsShears((PinkPetalsBlock) data.block()));

    private final BiConsumer<Data, VBlockLootProvider> consumer;
    private final String name;

    public BlockLootTable(String name, BiConsumer<Data, VBlockLootProvider> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    public void apply(Block block, @NotNull BlockEntry blockEntry, VBlockLootProvider provider) {
        Data data = new Data(block, blockEntry);
        consumer.accept(data, provider);
    }

    @Override
    public String toString() {
        return "BlockLootTable[name=" + name + ", consumer=" + consumer + "]";
    }

    public record Data(@NotNull Block block, BlockEntry blockEntry) {
    }

    public boolean isEmpty() {
        return this == NONE || this == UNSET;
    }
}
