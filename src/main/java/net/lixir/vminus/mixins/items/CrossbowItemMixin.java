package net.lixir.vminus.mixins.items;

import com.llamalad7.mixinextras.sugar.Local;
import net.lixir.vminus.world.item.IMaxDurationGetter;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends Item implements IMaxDurationGetter {

    public CrossbowItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int vminus$getMaxDuration() {
        Integer value = VisionUtils.getOverrideValue((VisionDuck) this, VisionProperties.Items.MAX_USE_TICKS, new VisionContext(this));
        if (value != null)
            return value;
        return 25;
    }

    @ModifyConstant(
            method = "getChargeDuration",
            constant = @Constant(intValue = 25)
    )
    private static int vMinus$getChargeDuration(int constant, @Local(argsOnly = true) @NotNull ItemStack stack) {
        if (stack.getItem() instanceof IMaxDurationGetter maxGetter) {
            return maxGetter.vminus$getMaxDuration();
        }
        return constant;
    }

}
