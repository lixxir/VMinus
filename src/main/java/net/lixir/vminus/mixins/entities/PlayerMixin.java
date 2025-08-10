package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.roles.Role;
import net.lixir.vminus.roles.RoleManager;
import net.lixir.vminus.roles.RoleSavedData;
import net.lixir.vminus.sight.resource.SightManager;
import net.lixir.vminus.util.SizeAttributeUtil;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin implements VisionDuck {
    @Unique
    private final Player vMinus$self = (Player) (Object) this;

    @ModifyArg(
            method = "eat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
            ),
            index = 4
    )
    private SoundEvent vMinus$replaceBurpSound(SoundEvent originalSound) {
        VisionFoodProperties visionFoodProperties = VisionUtils.getOverrideValue(this, VisionProperties.Items.FOOD, new VisionContext(vMinus$self));
        if (visionFoodProperties != null) {
            SoundEvent burpSound = visionFoodProperties.burpSound();
            if (burpSound != null) {
                return burpSound;
            }
        }
        return originalSound;
    }

    @Inject(method = "canUseGameMasterBlocks", at = @At("RETURN"), cancellable = true)
    private void vminus$canUseGameMasterBlocks(CallbackInfoReturnable<Boolean> cir) {
        if (!(vMinus$self instanceof ServerPlayer))
            return;
        RoleSavedData data = RoleSavedData.get(vMinus$self.level());
        String name = data.getRole(vMinus$self.getUUID());
        Role role = RoleManager.INSTANCE.getRole(name);
        if (role == null)
            return;
        boolean canUseCommandBlocks = role.canUseGameMasterBlocks();
        cir.setReturnValue(canUseCommandBlocks && vMinus$self.getAbilities().instabuild);
    }


    @Inject(method = "getDimensions", at = @At(value = "RETURN"), cancellable = true)
    private void vminus$getDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (!SightManager.get("size_attributes"))
            return;
        float defaultHeight = cir.getReturnValue().height;
        float defaultWidth = cir.getReturnValue().width;
        float width = defaultWidth * SizeAttributeUtil.getWidth(vMinus$self);
        float height = defaultHeight * SizeAttributeUtil.getHeight(vMinus$self);
        if (width != defaultWidth && height != defaultHeight)
            cir.setReturnValue(EntityDimensions.scalable(width, height));
    }
}
