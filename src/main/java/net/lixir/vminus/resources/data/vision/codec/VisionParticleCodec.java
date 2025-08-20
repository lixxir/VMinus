package net.lixir.vminus.resources.data.vision.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.values.VisionValue;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionParticleCodec extends VisionCodec<ParticleType<?>> {
    @SuppressWarnings("unchecked")
    @Override
    public Class<ParticleType<?>> getClassType() {
        return (Class<ParticleType<?>>) (Class<?>) ParticleType.class;
    }

    @Override
    public @Nullable List<VisionValue<ParticleType<?>>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionValue<ParticleType<?>>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
            ParticleType<?> particleType =ForgeRegistries.PARTICLE_TYPES.getValue(resourceLocation);

            visionProperties.add(VisionValue.create(particleType, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull ParticleType<?> value) {
        JsonObject jsonObject = new JsonObject();
        ResourceLocation id = ForgeRegistries.PARTICLE_TYPES.getKey(value);
        if (id != null) {
            jsonObject.addProperty("id", id.toString());
        } else {
            throw new IllegalArgumentException("Unknown particle type: " + value);
        }
        return jsonObject;
    }
}