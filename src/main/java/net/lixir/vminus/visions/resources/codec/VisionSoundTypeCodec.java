package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionSoundTypeCodec extends AbstractVisionCodec<SoundType> {
    @Override
    public @Nullable List<VisionValue<SoundType>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<SoundType>> visionValues = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            SoundEvent fallSound = parseSoundInObject(arrayObject, "fall");
            SoundEvent stepSound = parseSoundInObject(arrayObject, "step");
            SoundEvent breakSound = parseSoundInObject(arrayObject, "break");
            SoundEvent placeSound = parseSoundInObject(arrayObject, "place");
            SoundEvent hitSound = parseSoundInObject(arrayObject, "hit");

            float pitch = arrayObject.has("pitch") ? arrayObject.getAsJsonPrimitive("pitch").getAsFloat() : 1f;
            if (pitch < 0) {
                throw new JsonParseException(pitch + " is not an accepted pitch value for " + key + ". Must be greater than 0");
            }

            float level = arrayObject.has("level") ? arrayObject.getAsJsonPrimitive("level").getAsFloat() : 1f;
            if (level < 0) {
                throw new JsonParseException(level + " is not an accepted level value for " + key + ". Must be greater than 0");
            }

            SoundType soundType = new SoundType(level, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);

            visionValues.add(VisionValue.create(soundType, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
