package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements VisionDuck {
    @Unique
    private final ItemEntity vMinus$self = (ItemEntity) (Object) this;

    public ItemEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow
    public abstract ItemStack getItem();

    @Shadow @Final private static EntityDataAccessor<ItemStack> DATA_ITEM;

    @Inject(at = @At("RETURN"), method = "fireImmune", cancellable = true)
    private void vMinus$fireImmune(CallbackInfoReturnable<Boolean> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.FIRE_RESISTANT, new VisionContext(vMinus$self));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"), method = "setItem", cancellable = true)
    private void vMinus$setItem(@NotNull ItemStack itemStack, CallbackInfo ci) {
        if (ItemReplacement.tryReplace(itemStack, replaced ->
                entityData.set(DATA_ITEM, replaced))) {
            ci.cancel();
        }
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return ((VisionDuck) getItem().getItem()).vMinus$getVisionId();
    }
}
