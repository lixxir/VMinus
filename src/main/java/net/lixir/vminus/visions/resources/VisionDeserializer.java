package net.lixir.vminus.visions.resources;

import com.google.gson.*;
import net.lixir.vminus.visions.VisionType;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.Vision;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.lang.reflect.Type;

public class VisionDeserializer<T extends Vision> implements JsonDeserializer<T> {
    private final ICondition.IContext context;
    private final Class<T> visionClass;

    public VisionDeserializer(Class<T> visionClass, ICondition.IContext context) {
        this.visionClass = visionClass;
        this.context = context;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        T vision;
        try {
            vision = visionClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new JsonParseException("Error instantiating vision class", e);
        }

        vision.mergeEntries(VisionProcessor.getEntries(jsonObject, VisionType.valueOf(visionClass.getSimpleName().toUpperCase().substring(0,visionClass.getSimpleName().indexOf("V")))));

        for (VisionProperty<?> property : vision.getProperties()) {
            if (jsonObject.has(property.getId())) {
                property.parseAndAdd(jsonObject, context);
            }
        }
        return vision;
    }
}
