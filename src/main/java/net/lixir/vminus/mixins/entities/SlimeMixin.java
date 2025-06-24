package net.lixir.vminus.mixins.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.class)
public abstract class SlimeMixin {
    @Unique
    private final Slime vMinus$self = (Slime) (Object) this;
    
    @Inject(method = "remove", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
    ))
    private void vMinus$splitVariant(Entity.RemovalReason reason, CallbackInfo ci) {
        if (vMinus$self.getSize() > 1 && vMinus$self.isDeadOrDying()) {
            String variant = vMinus$self.getPersistentData().getString("variant");
            if (!variant.isEmpty())
                vMinus$self.getPersistentData().putString("variant", variant);
        }
    }
}
