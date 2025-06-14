package net.lixir.vminus.registry;

import net.lixir.vminus.datagen.util.VItemModelProvider;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ItemModel {
    private final BiConsumer<Data, VItemModelProvider> consumer;

    public ItemModel(BiConsumer<Data, VItemModelProvider> consumer) {
        this.consumer = consumer;
    }

    public void apply(Data data, VItemModelProvider provider) {
        consumer.accept(data, provider);
    }

    public static final ItemModel UNSET = new ItemModel((data, provider) -> {
    });
    public static final ItemModel NULL = new ItemModel((data, provider) -> {
    });
    public static final ItemModel BASIC = new ItemModel((data, provider) -> provider.basic(data.item(), data.itemEntry()));
    public static final ItemModel HANDHELD = new ItemModel((data, provider) -> provider.handheld(data.item()));
    public static final ItemModel PANE = new ItemModel((data, provider) -> provider.pane(data.item()));
    public static final ItemModel DOUBLE_PANE = new ItemModel((data, provider) -> provider.doublePane(data.item()));
    public static final ItemModel FROM_BLOCK_PARENT = new ItemModel((data, provider) -> provider.fromBlockParent(data.item()));

    public record Data(@NotNull Item item, @NotNull ItemEntry itemEntry) {
    }
}
