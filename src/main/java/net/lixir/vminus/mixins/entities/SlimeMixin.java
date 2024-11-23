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
    private final Slime slime = (Slime) (Object) this;

    protected SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void remove(Entity.RemovalReason removalReason, CallbackInfo ci) {
        int size = slime.getSize();
        if (!slime.level().isClientSide && size > 1 && slime.isDeadOrDying()) {
            Component customName = slime.getCustomName();
            boolean noAi = slime.isNoAi();
            float offset = (float) size / 4.0F;
            int newSize = size - 1;
            int splitCount = 2 + slime.getRandom().nextInt(3);
            String variant = "";
            if (slime.getPersistentData().contains("variant"))
                variant = slime.getPersistentData().getString("variant");
            for (int i = 0; i < splitCount; ++i) {
                float offsetX = ((float) (i % 2) - 0.5F) * offset;
                float offsetZ = ((float) (i / 2) - 0.5F) * offset;
                Slime newSlime = slime.getType().create(slime.level());
                if (newSlime != null) {
                    if (slime.isPersistenceRequired()) {
                        newSlime.setPersistenceRequired();
                    }
                    newSlime.setCustomName(customName);
                    newSlime.setNoAi(noAi);
                    newSlime.setInvulnerable(slime.isInvulnerable());
                    newSlime.setSize(newSize, true);
                    newSlime.moveTo(slime.getX() + (double) offsetX, slime.getY() + 0.5D, slime.getZ() + (double) offsetZ, slime.getRandom().nextFloat() * 360.0F, 0.0F);
                    newSlime.getPersistentData().putString("variant", variant);
                    //newSlime.load(newSlimeNbt);
                    slime.level().addFreshEntity(newSlime);
                }
            }
        }
        super.remove(removalReason);
        ci.cancel();
    }

}
