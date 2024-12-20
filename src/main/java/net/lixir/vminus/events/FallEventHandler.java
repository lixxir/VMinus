package net.lixir.vminus.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FallEventHandler {
    @SubscribeEvent
    public static void onEntityFall(LivingFallEvent event) {
        if (event == null) return;

        Entity entity = event.getEntity();
        if (entity == null) return;

        double distance = event.getDistance();
        double cancelDistance = 0;

        if (distance >= 2.5) {
            for (int index0 = 0; index0 < 4; index0++) {
                ItemStack bootsItemstack = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, index0)) : ItemStack.EMPTY);
                CompoundTag tag = bootsItemstack.getTag();
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
