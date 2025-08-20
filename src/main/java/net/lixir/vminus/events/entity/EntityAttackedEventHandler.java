package net.lixir.vminus.events.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityAttackedEventHandler {
    @SubscribeEvent
    public static void onEntityAttacked(final LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            LivingEntity entity = event.getEntity();
            Entity sourceentity = event.getSource().getEntity();
            Entity immediatesourceentity = event.getSource().getDirectEntity();

            if (immediatesourceentity == null || sourceentity == null)
                return;
            if (!entity.isAttackable())
                return;
            // Damage horse armor when attacked if it has durability
            // TODO: Make this a HorseMixin instead
            if (entity instanceof Horse) {
                ItemStack horseArmor = entity.getItemBySlot(EquipmentSlot.CHEST);
                if (!(horseArmor.getItem() == ItemStack.EMPTY.getItem())) {
                    if (horseArmor.isDamageableItem()) {
                        if (horseArmor.hurt(1, entity.getRandom(), null)) {
                            horseArmor.shrink(1);
                            horseArmor.setDamageValue(0);
                        }
                    }
                }
            }
        }
    }
}
