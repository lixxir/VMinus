package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FMLCommonSetupEventHandler {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        VminusModVariables.main_item_vision = new JsonObject();
        VminusModVariables.main_block_vision = new JsonObject();
        VminusModVariables.main_entity_vision = new JsonObject();
        VminusModVariables.main_enchantment_vision = new JsonObject();
        VminusModVariables.main_effect_vision = new JsonObject();
    }
}
