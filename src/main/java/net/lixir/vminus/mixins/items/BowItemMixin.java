package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.MaxDurationGetter;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends Item  implements MaxDurationGetter {
    public BowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public int vminus$getMaxDuration() {
        Integer value = VisionUtil.getOverrideValue((VisionDuck) this, VisionPropertyTypes.Items.MAX_USE_TICKS, new VisionContext(this));
        if (value != null)
            return value;
        return 20;
    }
}
