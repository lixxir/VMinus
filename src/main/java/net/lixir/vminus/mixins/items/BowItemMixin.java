package net.lixir.vminus.mixins.items;

import net.lixir.vminus.world.item.IMaxDurationGetter;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends Item implements IMaxDurationGetter {
    public BowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public int vminus$getMaxDuration() {
        Integer value = VisionUtils.getOverrideValue((VisionDuck) this, VisionProperties.Items.MAX_USE_TICKS, new VisionContext(this));
        if (value != null)
            return value;
        return 20;
    }
}
