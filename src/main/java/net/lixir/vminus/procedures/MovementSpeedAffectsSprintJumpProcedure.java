package net.lixir.vminus.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class MovementSpeedAffectsSprintJumpProcedure {
    @SubscribeEvent
    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        execute(event, event.getEntity());
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        double movementSpeed = 0;
        if (entity instanceof Player) {
            if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D && entity.isSprinting()) {
                movementSpeed = ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).getValue()
                        - ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).getBaseValue();
                movementSpeed = movementSpeed * 5.5;
                movementSpeed = movementSpeed - 0.2;
                entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + entity.getLookAngle().x * movementSpeed), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() + entity.getLookAngle().z * movementSpeed)));
            }
        }
    }
}
