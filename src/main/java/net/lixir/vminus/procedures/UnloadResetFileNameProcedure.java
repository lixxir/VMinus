package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class UnloadResetFileNameProcedure {
    @SubscribeEvent
    public static void onWorldUnload(net.minecraftforge.event.level.LevelEvent.Unload event) {
        execute(event);
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
                    VMinusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_block_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VMinusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_entity_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VMinusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_enchantment_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VMinusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
        VminusModVariables.main_effect_vision = new Object() {
            public com.google.gson.JsonObject parse(String rawJson) {
                try {
                    return new com.google.gson.Gson().fromJson(rawJson, com.google.gson.JsonObject.class);
                } catch (Exception e) {
                    VMinusMod.LOGGER.error(e);
                    return new com.google.gson.Gson().fromJson("{}", com.google.gson.JsonObject.class);
                }
            }
        }.parse("{}");
    }
}
