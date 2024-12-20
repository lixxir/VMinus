package net.lixir.vminus.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FarmlandTrampledEventHandler {
    @SubscribeEvent
    public static void onFarmlandTrampled(BlockEvent.FarmlandTrampleEvent event) {
        // Prevents the trampling of crops if the boots have the "lightfooted" nbt tag
        Entity entity = event.getEntity();
        ItemStack boots = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY);
        CompoundTag tag = boots.getTag();
        if (tag != null) {
            if (tag.getBoolean("lightfooted")) {
                if (event != null && event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event != null && event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
