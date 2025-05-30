package net.lixir.vminus.events;

import net.lixir.vminus.item.trait.ItemTraits;
import net.lixir.vminus.item.trait.ItemTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
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

        LevelAccessor world = event.getEntity().level();
        for (ItemStack armorStack : entity.getArmorSlots()) {
            for (ItemTrait itemTrait : ItemTraits.getTraits(armorStack)) {
                if (itemTrait.onJump(armorStack, entity, world))
                    if (event.isCancelable())
                        event.setCanceled(true);
            }
        }
    }
}