package net.lixir.vminus.visions.conditions;

import com.google.gson.*;
import net.lixir.vminus.registry.VMinusRarities;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVisionCondition {
    protected final boolean inverted;

    public AbstractVisionCondition(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return inverted;
    }

    abstract public boolean test(VisionConditionArguments visionConditionArguments);

    public static List<List<AbstractVisionCondition>> parseVisionConditions(JsonObject arrayObject, JsonObject jsonObject) throws JsonParseException {
        ArrayList<List<AbstractVisionCondition>> visionConditionList = new ArrayList<>();

        JsonArray conditionArray;
        if (arrayObject.has("conditions")) {
            conditionArray = arrayObject.getAsJsonArray("conditions");
        } else {
            return new ArrayList<>();
        }
        JsonObject groups;
        if (jsonObject.has("groups")) {
            groups = jsonObject.getAsJsonObject("groups");
        } else {
            throw new JsonParseException("Does not have a groups object (somehow!)");
        }
        for (JsonElement jsonElement : conditionArray.asList()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            String string = jsonPrimitive.getAsString();
            if (!groups.has(string))
                throw new JsonParseException(string + " could not be found in groups.");

            JsonElement conditionElement = groups.get(string);

            JsonArray realConditionArray = conditionElement.getAsJsonArray();
            ArrayList<AbstractVisionCondition> nestedConditionList = new ArrayList<>();
            for (JsonElement conditionArrayElement : realConditionArray.asList()) {
                AbstractVisionCondition visionCondition = getAbstractVisionCondition(conditionArrayElement);
                if (visionCondition != null)
                    nestedConditionList.add(visionCondition);

            }
            visionConditionList.add(nestedConditionList);
        }
        return visionConditionList;
    }

    private static @Nullable AbstractVisionCondition getAbstractVisionCondition(JsonElement conditionArrayElement) {
        JsonObject conditionObject = conditionArrayElement.getAsJsonObject();
        if (!conditionObject.has("type"))
            throw new JsonParseException(conditionObject + " has no defined type string.");
        JsonPrimitive typeElement = conditionObject.getAsJsonPrimitive("type");
        String type = typeElement.getAsString();
        AbstractVisionCondition visionCondition = null;
        boolean inverted = conditionObject.has("invert") && conditionObject.getAsJsonPrimitive("invert").getAsBoolean();
        switch (type) {
            case "rarity" -> visionCondition = parseRarityCondition(conditionObject, inverted);
            case "entity_string_nbt" -> visionCondition = parseEntityStringNbtCondition(conditionObject, inverted);
        }
        return visionCondition;
    }

    private static AbstractVisionCondition parseRarityCondition(JsonObject conditionObject, boolean inverted) throws JsonParseException {
        String value = getConditionValue(conditionObject).getAsString().toUpperCase();
        Rarity rarity;
        switch (value) {
            case "INVERTED" -> rarity = VMinusRarities.INVERTED;
            case "DELICACY" -> rarity = VMinusRarities.DELICACY;
            case "LEGENDARY" -> rarity = VMinusRarities.LEGENDARY;
            case "UNOBTAINABLE" -> rarity = VMinusRarities.UNOBTAINABLE;
            default -> rarity = Rarity.valueOf(value);
        }
        return new RarityVisionCondition(rarity, inverted);
    }

    private static AbstractVisionCondition parseEntityStringNbtCondition(JsonObject conditionObject, boolean inverted) throws JsonParseException {
        String path = conditionObject.getAsJsonPrimitive("path").getAsString();
        String value = conditionObject.getAsJsonPrimitive("value").getAsString();
        return new EntityStringNbtVisionCondition(path, value, inverted);
    }

    private static JsonPrimitive getConditionValue(JsonObject conditionObject) throws JsonParseException {
        return getConditionValue("value", conditionObject);
    }

    private static JsonPrimitive getConditionValue(String valueName, JsonObject conditionObject) throws JsonParseException {
        if (!conditionObject.has(valueName))
            throw new JsonParseException(conditionObject + " has no defined value string.");
        return conditionObject.getAsJsonPrimitive(valueName);
    }
}
