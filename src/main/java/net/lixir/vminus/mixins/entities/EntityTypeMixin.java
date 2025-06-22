package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.registry.entry.EntityEntry;
import net.lixir.vminus.registry.entry.EntityEntryAccessor;
import net.lixir.vminus.vision.VisionDuck;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin implements VisionDuck, EntityEntryAccessor {
    @Unique
    private final EntityType<?> vminus$entityType = (EntityType<?>) (Object) this;

    @Unique
    private EntityEntry vminus$entityEntry = null;

    @Unique
    private int vMinus$visionIndex = 0;

    /*
    @Inject(method = "canSummon", at = @At("RETURN"), cancellable = true)
    public void canSummon(CallbackInfoReturnable<Boolean> cir) {
        Boolean banned = vminus$entityVision.ban.value(new VisionContext(vminus$entityType));
        if (banned != null && banned) cir.setReturnValue(false);
    }

     */

    @Override
    public void vminus$setEntry(EntityEntry entry) {
        this.vminus$entityEntry = entry;
    }

    @Override
    public @Nullable EntityEntry vminus$getEntry() {
        return vminus$entityEntry;
    }

    @Override
    public void vMinus$setVisionIndex(int index) {
        vMinus$visionIndex = index;
    }

    @Override
    public int vMinus$getVisionIndex() {
        return vMinus$visionIndex;
    }
}
