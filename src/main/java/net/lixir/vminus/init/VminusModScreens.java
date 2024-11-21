
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.init;

import net.lixir.vminus.client.gui.CapesMenuScreen;
import net.lixir.vminus.client.gui.DefaultGuiScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VminusModScreens {
    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(VminusModMenus.DEFAULT_GUI.get(), DefaultGuiScreen::new);
            MenuScreens.register(VminusModMenus.CAPES_MENU.get(), CapesMenuScreen::new);
        });
    }
}
