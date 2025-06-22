package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionParticleCodec extends VisionCodec<ParticleType<?>> {
    @Override
    public @Nullable List<VisionProperty<ParticleType<?>>> decode(JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<ParticleType<?>>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
            ParticleType<?> particleType =ForgeRegistries.PARTICLE_TYPES.getValue(resourceLocation);

            visionProperties.add(VisionProperty.create(particleType, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }
}