package net.lixir.vminus.vision.conditions;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RarityCondition implements IVisionCondition {
    @Override
    public boolean test(Object object, String value) {
        if (object instanceof Item item) {
            return item.getRarity(item.getDefaultInstance()).toString().equalsIgnoreCase(value);
        } else if (object instanceof ItemStack itemStack) {
            return itemStack.getRarity().toString().equalsIgnoreCase(value);
        }
        return false;
    }


}
