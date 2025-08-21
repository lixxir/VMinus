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

        // Gathers all available vision properties based on vision type
        List<VisionProperty<?>> visionProperties = VisionProperties.fromVisionType(visionType);
        for (VisionProperty<?> property : visionProperties) {
            String id = property.getId();
            // Checks if the vision contains the id of the VisionProperty
            if (jsonObject.has(id)) {
                VisionCodec<?> propertyCodec = property.getCodec();
                // Uses codec from VisionProperty to parse values
                List<? extends VisionValue<?>> parsedList = propertyCodec.decode(jsonObject, id);
                visionEntry.addValues(id, parsedList);
            }
        }
        return visionEntry;
    }
}
