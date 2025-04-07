package net.lixir.vminus.visions.resources.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VisionSoundTypeParser extends AbstractVisionParser<SoundType> {
    @Override
    public @Nullable List<VisionValue<SoundType>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            SoundEvent fallSound = parseSoundInObject(arrayObject, "fall");
            if (fallSound == null)
                fallSound = SoundEvents.STONE_FALL;
            SoundEvent stepSound = parseSoundInObject(arrayObject, "step");
            if (stepSound == null)
                stepSound = SoundEvents.STONE_STEP;
            SoundEvent breakSound = parseSoundInObject(arrayObject, "break");
            if (breakSound == null)
                breakSound = SoundEvents.STONE_BREAK;
            SoundEvent placeSound = parseSoundInObject(arrayObject, "place");
            if (placeSound == null)
                placeSound = SoundEvents.STONE_PLACE;
            SoundEvent hitSound = parseSoundInObject(arrayObject, "hit");
            if (hitSound == null)
                hitSound = SoundEvents.STONE_HIT;

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
