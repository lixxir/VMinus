package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.sight.resource.SightManager;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.resource.managers.*;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class AddReloadListenerEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void AddReloadListenerEvent(@NotNull AddReloadListenerEvent event) {
        ICondition.IContext context = event.getConditionContext();

        event.addListener(new SightManager());
        event.addListener(new ItemVisionManager(context));
        event.addListener(new BlockVisionManager(context));


    }

}