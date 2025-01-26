package net.lixir.vminus.mixins.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor("rarity")
    Rarity getRarity();

    @Accessor("foodProperties")
    void setFoodProperties(FoodProperties foodProperties);
}