package net.lixir.vminus.mixins;

import net.lixir.vminus.registry.UnifiedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap", at = @At("HEAD"))
    private static void vminus$bootStrap(CallbackInfo ci) {
        for (UnifiedRegistry registry : UnifiedRegistry.getRegistries()) {
            registry.init();
        }
    }
}
