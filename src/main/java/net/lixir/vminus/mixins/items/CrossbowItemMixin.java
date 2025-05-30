package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.MaxDurationGetter;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin implements MaxDurationGetter {
    @Shadow public abstract int getUseDuration(ItemStack p_40938_);

    @Unique
    private final Item vminus$item = (Item) (Object) this;

    @Override
    public int vminus$getMaxDuration() {
        Integer value = ItemVision.of(vminus$item).max_duration.value(new VisionConditionArguments(vminus$item));
        if (value != null)
            return value;
        return 25;
    }
}
