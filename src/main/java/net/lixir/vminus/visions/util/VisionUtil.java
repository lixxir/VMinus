package net.lixir.vminus.visions.util;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.values.VisionProperty;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class VisionUtil {
    public static <T> void tryOverride(CallbackInfoReturnable<T> cir, VisionProperty<T> visionProperty, VisionConditionArguments visionConditionArguments) {
        T value = visionProperty.value(visionConditionArguments);
        if (value != null)
            cir.setReturnValue(value);
    }
}

