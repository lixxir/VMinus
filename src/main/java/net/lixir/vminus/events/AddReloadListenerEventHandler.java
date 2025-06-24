package net.lixir.vminus.events;

import net.lixir.vminus.sight.resource.SightManager;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.resource.manager.VisionManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class AddReloadListenerEventHandler {

    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onAddReloadListenerEvent(@NotNull AddReloadListenerEvent event) {
        ICondition.IContext context = event.getConditionContext();

        event.addListener(new SightManager());
        event.addListener(new VisionManager<>(VisionTypes.ITEM, BuiltInRegistries.ITEM, context));
        event.addListener(new VisionManager<>(VisionTypes.BLOCK, BuiltInRegistries.BLOCK, context));
        event.addListener(new VisionManager<>(VisionTypes.ENTITY, BuiltInRegistries.ENTITY_TYPE, context));
        event.addListener(new VisionManager<>(VisionTypes.TAB, BuiltInRegistries.CREATIVE_MODE_TAB, context));
        event.addListener(new VisionManager<>(VisionTypes.ENCHANTMENT, BuiltInRegistries.ENCHANTMENT, context));
        event.addListener(new VisionManager<>(VisionTypes.EFFECT, BuiltInRegistries.MOB_EFFECT, context));

    }

}