package net.lixir.vminus.datagen;

import net.lixir.vminus.datagen.util.VItemModelProvider;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ItemModel {
    private final String name;
    private final BiConsumer<Data, VItemModelProvider> consumer;

    public ItemModel(String name, BiConsumer<Data, VItemModelProvider> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    public void apply(Data data, VItemModelProvider provider) {
        consumer.accept(data, provider);
    }

    public static final ItemModel UNSET = new ItemModel("unset", (data, provider) -> {
    });
    public static final ItemModel NONE = new ItemModel("none", (data, provider) -> {
    });
    public static final ItemModel BASIC_NOT_BLOCK = new ItemModel("basic_not_block", (data, provider) -> provider.basicNotBlock(data.item()));
    public static final ItemModel BASIC = new ItemModel("basic", (data, provider) -> provider.basic(data.item(), data.itemEntry()));
    public static final ItemModel HANDHELD = new ItemModel("handheld", (data, provider) -> provider.handheld(data.item()));
    public static final ItemModel PANE = new ItemModel("pane", (data, provider) -> provider.pane(data.item()));
    public static final ItemModel DOUBLE_PANE = new ItemModel("double_pane", (data, provider) -> provider.doublePane(data.item()));
    public static final ItemModel DOUBLE_PLANT= new ItemModel("double_plant", (data, provider) -> provider.doublePane(data.item()));
    public static final ItemModel FROM_BLOCK_PARENT = new ItemModel("from_block_parent", (data, provider) -> provider.fromBlockParent(data.item()));

    public record Data(@NotNull Item item, @NotNull ItemEntry itemEntry) {
    }

    @Override
    public String toString() {
        return "ItemModel[" + name + "]";
    }
}
