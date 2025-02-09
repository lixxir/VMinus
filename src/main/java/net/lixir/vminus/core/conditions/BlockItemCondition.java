package net.lixir.vminus.core.conditions;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class BlockItemCondition implements IVisionCondition {
    @Override
    public boolean test(VisionConditionArguments visionConditionArguments, String value) {
        return  true;
    }
}
