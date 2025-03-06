package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.EntityVision;
import net.lixir.vminus.core.visions.accessors.IEntityVisionAccessor;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class EntityTypeMixin implements IEntityVisionAccessor {
    @Unique
    private final EntityType<?> vminus$entityType = (EntityType<?>) (Object) this;

    @Unique
    private EntityVision vminus$entityVision = new EntityVision();

    @Override
    public EntityVision vminus$getVision() {
        return this.vminus$entityVision;
    }

    @Override
    public void vminus$setVision(EntityVision vision) {
        this.vminus$entityVision = vision;
    }
    @Inject(method = "canSummon", at = @At("RETURN"), cancellable = true)
    public void canSummon(CallbackInfoReturnable<Boolean> cir) {
        Boolean banned = vminus$entityVision.ban.value(new VisionConditionArguments(vminus$entityType));
        if (banned != null && banned) cir.setReturnValue(false);
    }
}
