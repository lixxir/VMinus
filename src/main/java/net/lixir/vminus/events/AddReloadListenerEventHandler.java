package net.lixir.vminus.events;

import net.lixir.vminus.visions.resources.VisionManager;
import net.lixir.vminus.visions.util.VisionType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AddReloadListenerEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void AddReloadListenerEvent(AddReloadListenerEvent event) {
       ICondition.IContext context = event.getConditionContext();
       event.addListener(new VisionManager(VisionType.ITEM, context));
       event.addListener(new VisionManager(VisionType.BLOCK, context));
       event.addListener(new VisionManager(VisionType.ENTITY, context));
       event.addListener(new VisionManager(VisionType.EFFECT, context));
       event.addListener(new VisionManager(VisionType.ENCHANTMENT, context));

    }
}