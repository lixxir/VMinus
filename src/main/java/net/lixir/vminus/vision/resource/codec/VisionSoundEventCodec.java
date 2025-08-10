package net.lixir.vminus.vision.resource.codec;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class VisionSoundEventCodec extends VisionCodec<SoundEvent> {
    @Override
    public Class<SoundEvent> getClassType() {
        return SoundEvent.class;
    }

    @Override
    public @Nullable List<VisionValue<SoundEvent>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<SoundEvent>> visionProperties = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);

        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            SoundEvent value;
            try {
                String soundId = arrayObject.getAsJsonPrimitive("sound").getAsString();
                ResourceLocation rl = new ResourceLocation(soundId);
                value = BuiltInRegistries.SOUND_EVENT.get(rl);
                if (value == null) {
                    throw new JsonParseException("Unknown SoundEvent: " + soundId);
                }
            } catch (Exception e) {
                VMinus.LOGGER.error("Failed to parse SoundEvent from: {}", jsonObject);
                throw new JsonParseException(e);
            }

            visionProperties.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }

        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull SoundEvent value) {
        ResourceLocation id = BuiltInRegistries.SOUND_EVENT.getKey(value);
        if (id == null) {
            VMinus.LOGGER.warn("Unknown SoundEvent when encoding: {}", value);
            return null;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sound", id.toString());
        return jsonObject;
    }
}
