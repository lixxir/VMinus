package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionFoodPropertiesCodec extends VisionCodec<VisionFoodProperties> {
    @Override
    public @Nullable List<VisionProperty<VisionFoodProperties>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<VisionFoodProperties>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Integer nutrition = null;
            if (arrayObject.has("nutrition")) {
                nutrition = arrayObject.getAsJsonPrimitive("nutrition").getAsInt();
                if (nutrition > 20 || nutrition < 0) {
                    throw new JsonParseException(nutrition + " is not an accepted nutrition for " + key + ". Must be within 0 to 20");
                }
            }
            Float saturation = null;
            if (arrayObject.has("saturation")) {
                saturation = arrayObject.getAsJsonPrimitive("saturation").getAsFloat();
                if (saturation < 0) {
                    throw new JsonParseException(saturation + " is not an accepted saturation for " + key + ". Must be greater than 0");
                }
            }
            Boolean alwaysEdible = null;
            if (arrayObject.has("always_edible"))
                alwaysEdible = arrayObject.getAsJsonPrimitive("always_edible").getAsBoolean();
            Boolean isMeat = null;
            if (arrayObject.has("is_meat"))
                isMeat = arrayObject.getAsJsonPrimitive("is_meat").getAsBoolean();

            SoundEvent eatSound = parseSoundInObject(arrayObject, "eat_sound", SoundEvents.GENERIC_EAT);
            SoundEvent burpSound = parseSoundInObject(arrayObject, "burp_sound", SoundEvents.PLAYER_BURP);

            List<Pair<MobEffectInstance, Float>> effects = parseFoodEffects(arrayObject);

            VisionFoodProperties visionFoodProperties = new VisionFoodProperties(nutrition, saturation, alwaysEdible, isMeat, eatSound, burpSound, effects);

            visionProperties.add(VisionProperty.create(visionFoodProperties, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(VisionFoodProperties value) {
        return null;
    }

    private static List<Pair<MobEffectInstance, Float>> parseFoodEffects(JsonObject jsonObject) throws JsonParseException {
        List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();

        if (jsonObject.has("effects")) {
            JsonArray effectsArray = jsonObject.getAsJsonArray("effects");
            for (JsonElement effectElement : effectsArray) {
                JsonObject effectObject = effectElement.getAsJsonObject();

                String effectId = effectObject.getAsJsonPrimitive("effect_id").getAsString();
                ResourceLocation effectLocation;
                try {
                    effectLocation = new ResourceLocation(effectId);
                } catch (Exception e) {
                    throw new JsonParseException(effectId + " is an invalid effect ID");
                }

                MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectLocation);
                if (mobEffect == null) {
                    throw new JsonParseException(effectId + " is not a registered effect");
                }

                int amplifier = effectObject.has("amplifier") ? effectObject.getAsJsonPrimitive("amplifier").getAsInt() : 0;
                int duration = effectObject.has("duration") ? effectObject.getAsJsonPrimitive("duration").getAsInt() : 200;
                float chance = effectObject.has("chance") ? effectObject.getAsJsonPrimitive("chance").getAsFloat() : 1.0f;

                if (amplifier < 0) {
                    throw new JsonParseException("Amplifier cannot be negative for effect: " + effectId);
                }
                if (duration <= 0) {
                    throw new JsonParseException("Duration must be greater than 0 for effect: " + effectId);
                }
                if (chance < 0 || chance > 1) {
                    throw new JsonParseException("Chance must be between 0 and 1 for effect: " + effectId);
                }

                effects.add(new Pair<>(new MobEffectInstance(mobEffect, duration, amplifier), chance));
            }
        }

        return effects;
    }
}
