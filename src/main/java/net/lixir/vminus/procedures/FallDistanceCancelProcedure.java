package net.lixir.vminus.procedures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class FallDistanceCancelProcedure {
    @SubscribeEvent
    public static void onEntityFall(LivingFallEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity(), event.getDistance());
        }
    }

    public static void execute(Entity entity, double distance) {
        execute(null, entity, distance);
    }

    private static void execute(@Nullable Event event, Entity entity, double distance) {
        if (entity == null)
            return;
        ItemStack armor = ItemStack.EMPTY;
        double cancelDistance = 0;
        if (distance >= 2.5) {
            for (int index0 = 0; index0 < 4; index0++) {
                armor = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, index0)) : ItemStack.EMPTY);
                CompoundTag tag = armor.getTag();
                if (tag != null) {
                    cancelDistance = cancelDistance + tag.getDouble("fall_distance");
                }
            }
            if (2.5 + cancelDistance > distance) {
                if (event != null && event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event != null && event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
