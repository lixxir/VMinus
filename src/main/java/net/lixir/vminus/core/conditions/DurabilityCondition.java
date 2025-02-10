package net.lixir.vminus.core.conditions;

import net.minecraft.world.item.ItemStack;

public class DurabilityCondition extends NumericCondition {
    @Override
    protected Number getTargetValue(VisionConditionArguments args) {
        if (args.hasItem()) {
            assert args.getItem() != null;
            ItemStack stack = args.getItem().getDefaultInstance();
            return stack.getMaxDamage() - stack.getDamageValue();
        }
        return null;
    }
}
