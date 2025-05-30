package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVisionCodec<V> {
    protected AbstractVisionCodec() {}

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

    public static JsonObject encodeInteger(Integer value) {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("value", jsonPrimitive);
        return jsonObject;
    }

    protected static SoundEvent parseSoundInObject(JsonObject jsonObject, String key) throws JsonParseException {
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
        return null;
    }

    public abstract @Nullable List<VisionValue<V>> decode(JsonObject jsonObject, String key, @Nullable ICondition.IContext context) throws JsonParseException;

    public @Nullable JsonObject encode(V value) {
        return null;
    }
}
