package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.api.registry.definition.EntityDefinition;
import net.lixir.vminus.api.registry.definition.duck.EntityDefinitionDuck;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.VisionUtils;
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
public class EntityTypeMixin implements VisionDuck, EntityDefinitionDuck {
    @Unique
    private final EntityType<?> vMinus$self = (EntityType<?>) (Object) this;

    @Unique
    private EntityDefinition vMinus$entityDefinition = null;

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @Inject(method = "canSummon", at = @At("RETURN"), cancellable = true)
    private void vMinus$canSummon(CallbackInfoReturnable<Boolean> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Entities.BAN, new VisionContext(vMinus$self));
    }

    @Override
    public void vMinus$setDefinition(@javax.annotation.Nullable EntityDefinition entry) {
        this.vMinus$entityDefinition = entry;
    }

    @javax.annotation.Nullable
    @Override
    public @Nullable EntityDefinition vMinus$getDefinition() {
        return vMinus$entityDefinition;
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
