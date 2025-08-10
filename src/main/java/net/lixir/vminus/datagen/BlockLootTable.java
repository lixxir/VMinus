package net.lixir.vminus.datagen;

import net.lixir.vminus.datagen.util.loottable.VBlockLootProvider;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class BlockLootTable {
    public static final BlockLootTable UNSET = new BlockLootTable("unset", (data, provider) -> {
    });
    public static final BlockLootTable NONE = new BlockLootTable("none", (data, provider) -> {});
    public static final BlockLootTable SELF = new BlockLootTable("self", (data, provider) -> provider.self(data.block()));

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
}
