package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashFenceKnotEntityMixin {
    @Unique
    private final LeashFenceKnotEntity vMinus$self = (LeashFenceKnotEntity) (Object) this;

    @Inject(method = "survives", at = @At("RETURN"), cancellable = true)
    private void vMinus$survives(@NotNull CallbackInfoReturnable<Boolean> cir) {
       cir.setReturnValue(vMinus$self.level().getBlockState(vMinus$self.blockPosition()).is(VMinusTags.Blocks.LEASHABLE));
    }
}
