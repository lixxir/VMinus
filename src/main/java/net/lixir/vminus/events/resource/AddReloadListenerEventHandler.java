package net.lixir.vminus.events.resource;

import net.lixir.vminus.resources.data.RoleManager;
import net.lixir.vminus.resources.data.sight.SightManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class AddReloadListenerEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onAddReloadListenerEvent(@NotNull AddReloadListenerEvent event) {
        ICondition.IContext context = event.getConditionContext();
        // event.addListener(BannedRecipeManager.INSTANCE);
        event.addListener(RoleManager.INSTANCE);
        event.addListener(new SightManager());
    }


}