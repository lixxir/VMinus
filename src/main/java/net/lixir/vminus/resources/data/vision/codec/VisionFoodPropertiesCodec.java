package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionFoodPropertiesCodec extends VisionCodec<VisionFoodProperties> {
    @Override
    public Class<VisionFoodProperties> getClassType() {
        return VisionFoodProperties.class;
    }

    @Override
    public @Nullable List<VisionValue<VisionFoodProperties>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<VisionFoodProperties>> visionProperties = new ArrayList<>();
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
            if (arrayObject.has("meat"))
                isMeat = arrayObject.getAsJsonPrimitive("meat").getAsBoolean();

            SoundEvent eatSound = parseSoundInObject(arrayObject, "eat_sound", SoundEvents.GENERIC_EAT);
            SoundEvent burpSound = parseSoundInObject(arrayObject, "burp_sound", SoundEvents.PLAYER_BURP);

            List<Pair<MobEffectInstance, Float>> effects = parseFoodEffects(arrayObject);

            VisionFoodProperties visionFoodProperties = new VisionFoodProperties(nutrition, saturation, alwaysEdible, isMeat, eatSound, burpSound, effects);

            visionProperties.add(VisionValue.create(visionFoodProperties, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull VisionFoodProperties value) {
        JsonObject object = new JsonObject();

        if (value.nutrition() != null)
            object.addProperty("nutrition", value.nutrition());

        if (value.saturation() != null)
            object.addProperty("saturation", value.saturation());

        if (value.alwaysEdible() != null)
            object.addProperty("always_edible", value.alwaysEdible());

        if (value.isMeat() != null)
            object.addProperty("meat", value.isMeat());

        if (value.eatSound() != null && ForgeRegistries.SOUND_EVENTS.getKey(value.eatSound()) != null)
            object.addProperty("eat_sound", ForgeRegistries.SOUND_EVENTS.getKey(value.eatSound()).toString());

        if (value.burpSound() != null && ForgeRegistries.SOUND_EVENTS.getKey(value.burpSound()) != null)
            object.addProperty("burp_sound", ForgeRegistries.SOUND_EVENTS.getKey(value.burpSound()).toString());

        if (!value.effects().isEmpty()) {
            JsonArray effectsArray = new JsonArray();
            for (Pair<MobEffectInstance, Float> pair : value.effects()) {
                MobEffectInstance instance = pair.getFirst();
                Float chance = pair.getSecond();
                MobEffect effect = instance.getEffect();

                JsonObject effectObj = new JsonObject();
                ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
                if (id != null)
                    effectObj.addProperty("id", id.toString());

                if (instance.getAmplifier() != 0)
                    effectObj.addProperty("amplifier", instance.getAmplifier());

                if (instance.getDuration() != 600)
                    effectObj.addProperty("duration", instance.getDuration());

                if (chance != 1.0f)
                    effectObj.addProperty("chance", chance);

                effectsArray.add(effectObj);
            }
            object.add("effects", effectsArray);
        }

        return object;
    }

    private static List<Pair<MobEffectInstance, Float>> parseFoodEffects(JsonObject jsonObject) throws JsonParseException {
        List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();

        if (jsonObject.has("effects")) {
            JsonArray effectsArray = jsonObject.getAsJsonArray("effects");
            for (JsonElement effectElement : effectsArray) {
                JsonObject effectObject = effectElement.getAsJsonObject();

                String effectId = effectObject.getAsJsonPrimitive("id").getAsString();
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
                int duration = effectObject.has("duration") ? effectObject.getAsJsonPrimitive("duration").getAsInt() : 600;
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
