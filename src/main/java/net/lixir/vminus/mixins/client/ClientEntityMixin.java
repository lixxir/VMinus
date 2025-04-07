package net.lixir.vminus.mixins.client;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.network.mobvariants.RequestVariantTexturePacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class ClientEntityMixin {
    @Unique
    private final Entity vminus$entity = (Entity) (Object) this;

    @Inject(method = "baseTick", at = @At(value = "TAIL"))
    public void baseTick(CallbackInfo ci) {
        // vminus$entity.refreshDimensions();

    }
}
