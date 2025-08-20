package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class VisionSoundTypeCodec extends VisionCodec<SoundType> {
    @Override
    public Class<SoundType> getClassType() {
        return SoundType.class;
    }


    @Override
    public @Nullable List<VisionValue<SoundType>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<SoundType>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            SoundEvent fallSound = parseSoundInObject(arrayObject, "fall", SoundEvents.STONE_FALL);
            SoundEvent stepSound = parseSoundInObject(arrayObject, "step", SoundEvents.STONE_STEP);
            SoundEvent breakSound = parseSoundInObject(arrayObject, "break", SoundEvents.STONE_BREAK);
            SoundEvent placeSound = parseSoundInObject(arrayObject, "place", SoundEvents.STONE_PLACE);
            SoundEvent hitSound = parseSoundInObject(arrayObject, "hit", SoundEvents.STONE_HIT);

            float pitch = arrayObject.has("pitch") ? arrayObject.getAsJsonPrimitive("pitch").getAsFloat() : 1f;
            if (pitch < 0) {
                throw new JsonParseException(pitch + " is not an accepted pitch value for " + key + ". Must be greater than 0");
            }

            float level = arrayObject.has("level") ? arrayObject.getAsJsonPrimitive("level").getAsFloat() : 1f;
            if (level < 0) {
                throw new JsonParseException(level + " is not an accepted level value for " + key + ". Must be greater than 0");
            }

            SoundType soundType = new SoundType(level, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);

            visionProperties.add(VisionValue.create(soundType, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull SoundType value) {
        JsonObject jsonObject = new JsonObject();

        if (value.getPitch() != 1.0f)
            jsonObject.addProperty("pitch", value.getPitch());
        if (value.getVolume() != 1.0f)
            jsonObject.addProperty("level", value.getVolume());

        jsonObject.addProperty("break", getSoundId(value.getBreakSound()));
        jsonObject.addProperty("step", getSoundId(value.getStepSound()));
        jsonObject.addProperty("place", getSoundId(value.getPlaceSound()));
        jsonObject.addProperty("hit", getSoundId(value.getHitSound()));
        jsonObject.addProperty("fall", getSoundId(value.getFallSound()));

        return jsonObject;
    }

    private @NotNull String getSoundId(@NotNull SoundEvent soundEvent) {
        return soundEvent.getLocation().toString();
    }
}
