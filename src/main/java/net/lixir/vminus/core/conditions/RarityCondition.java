package net.lixir.vminus.core.conditions;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RarityCondition implements IVisionCondition {
    @Override
    public boolean test(VisionConditionArguments visionConditionArguments, String value) {
        if (visionConditionArguments.hasItem()) {
            Item item = visionConditionArguments.getItem();
            return item.getRarity(item.getDefaultInstance()).toString().equalsIgnoreCase(value);
        }
        return true;
    }
}
