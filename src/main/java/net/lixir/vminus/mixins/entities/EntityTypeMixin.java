package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.registry.entry.EntityEntry;
import net.lixir.vminus.registry.entry.EntityEntryAccessor;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class EntityTypeMixin implements VisionDuck, EntityEntryAccessor {
    @Unique
    private final EntityType<?> vMinus$self = (EntityType<?>) (Object) this;

    @Unique
    private EntityEntry vMinus$entityEntry = null;

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @Inject(method = "canSummon", at = @At("RETURN"), cancellable = true)
    private void vMinus$canSummon(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Entities.BAN, new VisionContext(vMinus$self));
    }

    @Override
    public void vminus$setEntry(EntityEntry entry) {
        this.vMinus$entityEntry = entry;
    }

    @Override
    public @Nullable EntityEntry vminus$getEntry() {
        return vMinus$entityEntry;
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.ENTITY;
    }

    @Override
    public void vMinus$setVisionId(@Nullable ResourceLocation id) {
        vMinus$visionId = id;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return vMinus$visionId;
    }
}
