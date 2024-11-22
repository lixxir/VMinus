
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.init;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.capes.OpenCapesMenuMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class VminusModKeyMappings {
    public static final KeyMapping OPEN_CAPES_MENU = new KeyMapping("key.vminus.open_capes_menu", GLFW.GLFW_KEY_V, "key.categories.misc") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                VMinusMod.PACKET_HANDLER.sendToServer(new OpenCapesMenuMessage(0, 0));
                OpenCapesMenuMessage.pressAction(Minecraft.getInstance().player, 0, 0);
            }
            isDownOld = isDown;
        }
    };

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_CAPES_MENU);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                OPEN_CAPES_MENU.consumeClick();
            }
        }
    }
}
