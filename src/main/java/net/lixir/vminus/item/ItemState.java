package net.lixir.vminus.item;

import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public record ItemState(Item item, ResourceLocation location, ItemPropertyFunction function) {
    @Contract("_, _, _ -> new")
    public static @NotNull ItemState of(Item item, ResourceLocation location, ItemPropertyFunction function) {
        return new ItemState(item, location, function);
    }
}
