package net.lixir.vminus.vision.resource;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.VisionEntry;
import net.lixir.vminus.vision.VisionPropertyType;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.resource.codec.VisionCodec;
import net.lixir.vminus.vision.values.VisionProperty;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

public class VisionDeserializer<T> implements JsonDeserializer<VisionEntry<T>> {
    private final String listName;
    private final VisionType<T> visionType;

    public VisionDeserializer(String listName, VisionType<T> visionType) {
        this.listName = listName;
        this.visionType = visionType;
    }

    @Override
    public VisionEntry<T> deserialize(@NotNull JsonElement jsonElement, Type typeOfT, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        VisionEntry<T> visionEntry = new VisionEntry<>();
        visionEntry.addEntries(VisionProcessor.getEntries(listName, jsonObject));

        List<VisionPropertyType<?>> propertyTypes = VisionPropertyTypes.fromVisionType(visionType);
        if (visionEntry.getEntries().contains("villager")) {
            VMinus.LOGGER.info("Is villager and {}", propertyTypes);
        }

        for (VisionPropertyType<?> property : propertyTypes) {
            String propertyId = property.getId();
            if (jsonObject.has(propertyId)) {
                VisionCodec<?> propertyCodec = property.getCodec();
                List<? extends VisionProperty<?>> parsedList = propertyCodec.decode(jsonObject, propertyId);
                visionEntry.addValues(propertyId, parsedList);
            }
        }
        return visionEntry;
    }
}
