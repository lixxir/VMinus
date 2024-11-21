package net.lixir.vminus.procedures;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class HorseArmorDamageProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity());
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        ItemStack horseArmor = ItemStack.EMPTY;
        if (entity.isAttackable()) {
            if (entity instanceof Horse) {
                horseArmor = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY);
                if (!(horseArmor.getItem() == ItemStack.EMPTY.getItem())) {
                    if (horseArmor.isDamageableItem()) {
                        {
                            ItemStack _ist = horseArmor;
                            if (_ist.hurt(1, RandomSource.create(), null)) {
                                _ist.shrink(1);
                                _ist.setDamageValue(0);
                            }
                        }
                    }
                }
            }
        }
    }
}
