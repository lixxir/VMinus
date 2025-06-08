package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.EntityEntry;
import net.lixir.vminus.registry.entry.EntityEntryAccessor;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.EntityVision;
import net.lixir.vminus.visions.accessors.EntityVisionAccessor;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class EntityTypeMixin implements EntityVisionAccessor, EntityEntryAccessor {
    @Unique
    private final EntityType<?> vminus$entityType = (EntityType<?>) (Object) this;

    @Unique
    private EntityEntry vminus$entityEntry = null;

    @Unique
    private EntityVision vminus$entityVision = new EntityVision();

    @Override
    public @NotNull EntityVision vminus$getVision() {
        if (vminus$entityVision == null)
            return EntityVision.EMPTY;
        return this.vminus$entityVision;
    }

    @Override
    public void vminus$clearVision() {
        if (vminus$entityVision != null)
            this.vminus$entityVision = new EntityVision();
    }

    @Override
    public void vminus$mergeVision(EntityVision vision) {
        if (this.vminus$entityVision == null)
            this.vminus$entityVision = vision;
        else
            this.vminus$entityVision.merge(vision);
    }

    @Override
    public void vminus$freezeVision() {
        if (vminus$entityVision != null)
            this.vminus$entityVision.freeze();
    }

    @Inject(method = "canSummon", at = @At("RETURN"), cancellable = true)
    public void canSummon(CallbackInfoReturnable<Boolean> cir) {
        Boolean banned = vminus$entityVision.ban.value(new VisionConditionArguments(vminus$entityType));
        if (banned != null && banned) cir.setReturnValue(false);
    }

    @Override
    public void vminus$setEntry(EntityEntry entry) {
        this.vminus$entityEntry = entry;
    }

    @Override
    public @Nullable EntityEntry vminus$getEntry() {
        return vminus$entityEntry;
    }
}
