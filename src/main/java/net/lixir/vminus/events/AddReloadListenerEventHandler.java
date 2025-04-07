package net.lixir.vminus.events;

import net.lixir.vminus.visions.resources.managers.BlockVisionManager;
import net.lixir.vminus.visions.resources.managers.CreativeTabManager;
import net.lixir.vminus.visions.resources.managers.EntityVisionManager;
import net.lixir.vminus.visions.resources.managers.ItemVisionManager;
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
       event.addListener(new ItemVisionManager(context));
       event.addListener(new BlockVisionManager(context));
       event.addListener(new EntityVisionManager(context));
       event.addListener(new CreativeTabManager(context));
    }
}