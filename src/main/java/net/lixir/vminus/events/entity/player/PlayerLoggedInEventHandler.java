package net.lixir.vminus.events.entity.player;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class PlayerLoggedInEventHandler {
    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.@NotNull PlayerLoggedInEvent event) {

    }
}
