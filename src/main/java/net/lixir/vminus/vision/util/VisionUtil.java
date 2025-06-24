package net.lixir.vminus.vision.util;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyType;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class VisionUtil {
    public static <T> @Nullable T getOverrideValue(VisionDuck visionDuck, @NotNull VisionPropertyType<T> visionPropertyType, @Nullable VisionContext visionContext) {
        Vision vision = Vision.getVision(visionDuck);
        return vision.getValue(visionPropertyType.getId(), visionContext);
    }

    public static <T> T tryOverride(CallbackInfoReturnable<T> cir, VisionDuck visionDuck, VisionPropertyType<T> visionPropertyType, @Nullable VisionContext visionContext) {
        T value = getOverrideValue(visionDuck, visionPropertyType, visionContext);
        if (value != null) {
            cir.setReturnValue(value);
        }
        return value;
    }

    public static <T> T tryCancel(CallbackInfo ci, VisionDuck visionDuck, VisionPropertyType<T> visionPropertyType, @Nullable VisionContext visionContext) {
        T value = getOverrideValue(visionDuck, visionPropertyType, visionContext);
        if (value != null) {
            ci.cancel();
        }
        return value;
    }
}

