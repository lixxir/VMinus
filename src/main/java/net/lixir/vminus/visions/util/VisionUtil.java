package net.lixir.vminus.visions.util;

import net.lixir.vminus.visions.Vision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.values.VisionProperty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

public class VisionUtil {
    public static <T> void visionOverride(CallbackInfoReturnable<T> cir, T t) {
        if (t != null) {
            cir.setReturnValue(t);
        }
    }

    public static <T>  void visionOverride(CallbackInfoReturnable<T> cir, VisionProperty<T> visionProperty, MobEffect mobEffect) {
        T value = visionProperty.value(new VisionConditionArguments(mobEffect));
        if (value != null)
            cir.setReturnValue(value);
    }

    public static <T>  void visionOverride(CallbackInfoReturnable<T> cir, VisionProperty<T> visionProperty, Item item) {
        T value = visionProperty.value(new VisionConditionArguments(item));
        if (value != null)
            cir.setReturnValue(value);
    }
}

