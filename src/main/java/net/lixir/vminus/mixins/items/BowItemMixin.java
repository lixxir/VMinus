package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.MaxDurationGetter;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BowItem.class)
public abstract class BowItemMixin implements MaxDurationGetter {
    @Unique
    private final Item vminus$item = (Item) (Object) this;

    /*
    @Override
    public int vminus$getMaxDuration() {
        Integer value = ItemVision.of(vminus$item).max_duration.value(new VisionContext(vminus$item));
        if (value != null)
            return value;
        return 20;
    }

     */
}
