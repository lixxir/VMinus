package net.lixir.vminus;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class BannedMobEffects {
    @SubscribeEvent
    public static void onMobEffectEvent(MobEffectEvent.Applicable event) {
        if (event != null && event.getEntity() != null) {
            String effect = event.getEffectInstance().toString();
            int level = new Object() {
                int convert(String s) {
                    try {
                        return (int) Double.parseDouble(s.trim());
                    } catch (Exception e) {
                    }
                    return 0;
                }
            }.convert(effect.substring(effect.indexOf("x ") + "x ".length(), effect.indexOf(",")));
            level = Math.max(1, level);
            int duration = new Object() {
                int convert(String s) {
                    try {
                        return (int) Double.parseDouble(s.trim());
                    } catch (Exception e) {
                    }
                    return 0;
                }
            }.convert(effect.substring(effect.indexOf("Duration: ") + 10));
            effect = effect.replace("effect.", "").replace(".", ":").replace(",", "");
            effect = effect.substring(0, effect.indexOf(" "));
            execute(event, effect);
        }
    }

    public static void execute(String effect) {
        execute(null, effect);
    }

    private static void execute(@Nullable Event event, String effect) {
        if (effect == null)
            return;
        ResourceLocation effectLocation = new ResourceLocation(effect);
        MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectLocation);
        if (mobEffect == null) {
            return;
        }
        JsonObject visionData = VisionHandler.getVisionData(mobEffect);
        if (visionData != null && visionData.has("banned")) {
            boolean banned = VisionValueHelper.isBooleanMet(visionData, "banned");
            if (banned) {
                if (event != null && event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
