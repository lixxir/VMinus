package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class VisionCodec<V> {
    protected VisionCodec() {
    }

    public abstract Class<V> getClassType();

    protected static ResourceLocation parseResourceLocation(String key, JsonObject jsonObject) throws JsonParseException {
        return parseResourceLocation("value", key, jsonObject);
    }

    protected static ResourceLocation parseResourceLocation(String valueName, String key, JsonObject jsonObject) throws JsonParseException {
        String id;
        try {
            id = jsonObject.getAsJsonPrimitive(valueName).getAsString();
        } catch (Exception e) {
            throw new JsonParseException(key + " does not have " + valueName + ".");
        }
        ResourceLocation resourceLocation;
        try {
            resourceLocation = new ResourceLocation(id);
        } catch (Exception e) {
            throw new JsonParseException(id + " is not a valid resource location for " + key + ".");
        }
        return resourceLocation;
    }

    public static @NotNull JsonObject encodeInteger(Integer value) {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonPrimitive);
        return jsonObject;
    }

    protected static @NotNull SoundEvent parseSoundInObject(@NotNull JsonObject jsonObject, String key, SoundEvent fallback) throws JsonParseException {
        if (jsonObject.has(key)) {
            String soundString = jsonObject.getAsJsonPrimitive(key).getAsString();
            ResourceLocation soundLocation;
            try {
                soundLocation = new ResourceLocation(soundString);
            } catch (Exception e) {
                throw new JsonParseException(soundString + " is an invalid sound location for " + key);
            }
            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
            if (soundEvent != null) {
                return soundEvent;
            } else {
                throw new JsonParseException(soundString + " as " + key + " does not exist in the sound registries");
            }
        }
        return fallback;
    }

    public abstract @Nullable List<VisionValue<V>> decode(JsonObject jsonObject, String key) throws JsonParseException;

    public abstract @Nullable JsonObject encode(@NotNull V value);
}
