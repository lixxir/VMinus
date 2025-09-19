package net.lixir.vminus.api.datagen.item.model;

import net.lixir.vminus.api.datagen.item.ItemData;
import net.lixir.vminus.api.datagen.item.model.provider.VItemModelProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Built-in item model types provided by VMinus.
 */
public enum BuiltInItemModelTypes implements ItemModelType {
    UNSET("unset", (data, provider) -> {}),
    NONE("none", (data, provider) -> {}),
    BASIC_NOT_BLOCK("basic_not_block", (data, provider) -> provider.basicNotBlock(data.item())),
    BASIC("basic", (data, provider) -> provider.basic(data.item(), data.itemDefinition())),
    HANDHELD("handheld", (data, provider) -> provider.handheld(data.item())),
    PANE("pane", (data, provider) -> provider.pane(data.item())),
    DOUBLE_PANE("double_pane", (data, provider) -> provider.doublePane(data.item())),
    DOUBLE_PLANT("double_plant", (data, provider) -> provider.doublePane(data.item())),
    FROM_BLOCK_PARENT("from_block_parent", (data, provider) -> provider.fromBlockParent(data.item()));

    private final String name;
    private final BiConsumer<ItemData, VItemModelProvider> consumer;

    BuiltInItemModelTypes(String name, BiConsumer<ItemData, VItemModelProvider> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    @Override
    public void apply(ItemData itemData, VItemModelProvider provider) {
        consumer.accept(itemData, provider);
    }

    @Override
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "ItemModel{" + name + "}";
    }
}
