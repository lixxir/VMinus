package net.lixir.vminus.events.entity.living;


import net.lixir.vminus.world.item.IEquipmentItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class LivingJumpEventHandler {
    @SubscribeEvent
    public static void onEntityJump(LivingEvent.@NotNull LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;
        if (!(entity instanceof Player player) || player.getAbilities().flying)
            return;
        if (entity.isSpectator())
            return;
        Level level = entity.level();
        ItemStack boots = entity.getSlot(EquipmentSlot.FEET.getIndex()).get();
        if (boots.getItem() instanceof IEquipmentItem IEquipmentItem) {
            IEquipmentItem.onEntityJump(level, entity, boots, true);
        }
    }
}