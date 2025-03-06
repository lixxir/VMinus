package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.Traits;
import net.lixir.vminus.world.Trait;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LivingJumpEventHandler {
    @SubscribeEvent
    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;
        double movementSpeed = 0;
        if (entity instanceof Player) {
            if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D && entity.isSprinting()) {
                movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue() - entity.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
                movementSpeed = movementSpeed * 3.5;
                movementSpeed = movementSpeed - 0.2;
                entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + entity.getLookAngle().x * movementSpeed), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() + entity.getLookAngle().z * movementSpeed)));
            }
        }
        LevelAccessor world = event.getEntity().level();
        for (ItemStack armorStack : entity.getArmorSlots()) {
            for (Trait trait : Traits.getTraits(armorStack)) {
                if (trait.onJump(armorStack, entity, world))
                    if (event.isCancelable())
                        event.setCanceled(true);
            }
        }
    }
}