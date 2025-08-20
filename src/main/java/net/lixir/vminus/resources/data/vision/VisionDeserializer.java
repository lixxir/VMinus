package net.lixir.vminus.resources.data.vision;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.VisionEntry;
import net.lixir.vminus.vision.VisionProperty;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.resources.data.vision.codec.VisionCodec;
import net.lixir.vminus.vision.values.VisionValue;
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
        visionEntry.addEntries(VisionFormatter.getEntries(listName, jsonObject));

        List<VisionProperty<?>> propertyTypes = VisionProperties.fromVisionType(visionType);
        if (visionEntry.getEntries().contains("villager")) {
            VMinus.LOGGER.info("Is villager and {}", propertyTypes);
        }

        for (VisionProperty<?> property : propertyTypes) {
            String propertyId = property.getId();
            if (jsonObject.has(propertyId)) {
                VisionCodec<?> propertyCodec = property.getCodec();
                List<? extends VisionValue<?>> parsedList = propertyCodec.decode(jsonObject, propertyId);
                visionEntry.addValues(propertyId, parsedList);
            }
        }
        return visionEntry;
    }
}
