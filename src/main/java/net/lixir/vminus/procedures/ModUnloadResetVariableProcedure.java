package net.lixir.vminus.procedures;

import net.lixir.vminus.VminusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModUnloadResetVariableProcedure {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        execute();
    }

    public static void execute() {
        execute(null);
    }

    private static void execute(@Nullable Event event) {
        VminusModVariables.main_item_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VminusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_block_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VminusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_entity_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VminusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_enchantment_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VminusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_effect_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VminusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
    }
}
