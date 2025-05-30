package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.MaxDurationGetter;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BowItem.class)
public abstract class BowItemMixin implements MaxDurationGetter {
    @Unique
    private final Item vminus$item = (Item) (Object) this;

    @Override
    public int vminus$getMaxDuration() {
        Integer value = ItemVision.of(vminus$item).max_duration.value(new VisionConditionArguments(vminus$item));
        if (value != null)
            return value;
        return 20;
    }
}
