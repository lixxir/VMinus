package net.lixir.vminus.mixins.core.registries;

import net.lixir.vminus.VMinus;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BuiltInRegistries.class, priority = 10000)
public abstract class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap", at = @At("HEAD"))
    private static void vMinus$bootStrap(CallbackInfo ci) {
        VMinus.init();
    }
}
