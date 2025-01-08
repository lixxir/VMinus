package net.lixir.vminus.mixins.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.class)
public abstract class SlimeMixin extends Mob implements Enemy {

    @Unique
    private final Slime vminus$slime = (Slime) (Object) this;

    protected SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void remove(Entity.RemovalReason removalReason, CallbackInfo ci) {
        int size = vminus$slime.getSize();
        if (!vminus$slime.level.isClientSide && size > 1 && vminus$slime.isDeadOrDying()) {
            Component customName = vminus$slime.getCustomName();
            boolean noAi = vminus$slime.isNoAi();
            float offset = (float) size / 4.0F;
            int newSize = size - 1;
            int splitCount = 2 + vminus$slime.getRandom().nextInt(3);
            String variant = "";
            if (vminus$slime.getPersistentData().contains("variant"))
                variant = vminus$slime.getPersistentData().getString("variant");
            for (int i = 0; i < splitCount; ++i) {
                float offsetX = ((float) (i % 2) - 0.5F) * offset;
                float offsetZ = ((float) (i / 2) - 0.5F) * offset;
                Slime newSlime = vminus$slime.getType().create(vminus$slime.level);
                if (newSlime != null) {
                    if (vminus$slime.isPersistenceRequired()) {
                        newSlime.setPersistenceRequired();
                    }
                    newSlime.setCustomName(customName);
                    newSlime.setNoAi(noAi);
                    newSlime.setInvulnerable(vminus$slime.isInvulnerable());
                    ((SlimeAccessor) newSlime).invokeSetSize(newSize, true);
                    newSlime.moveTo(vminus$slime.getX() + (double) offsetX, vminus$slime.getY() + 0.5D, vminus$slime.getZ() + (double) offsetZ, vminus$slime.getRandom().nextFloat() * 360.0F, 0.0F);
                    newSlime.getPersistentData().putString("variant", variant);
                    //newSlime.load(newSlimeNbt);
                    vminus$slime.level.addFreshEntity(newSlime);
                }
            }
        }
        super.remove(removalReason);
        ci.cancel();
    }

}
