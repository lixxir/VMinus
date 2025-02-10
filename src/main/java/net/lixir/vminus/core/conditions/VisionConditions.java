package net.lixir.vminus.core.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.core.values.VisionValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VisionConditions {
    private static final Map<String, IVisionCondition> CONDITIONS = new HashMap<>();

    static {
        CONDITIONS.put("rarity", new RarityCondition());
        CONDITIONS.put("mod_loaded", new ModLoadedCondition());
        CONDITIONS.put("block_item", new BlockItemCondition());
        CONDITIONS.put("durability", new DurabilityCondition());
    }

    public static List<VisionValue.ConditionEntry> resolveConditions(JsonObject arrayObject) throws JsonParseException {
        JsonArray conditionArray;
        if (arrayObject.has("conditions")) {
            if (!arrayObject.get("conditions").isJsonArray()) {
                throw new JsonParseException("Conditions are not a JsonArray.");
            }
            conditionArray = arrayObject.getAsJsonArray("conditions");
        } else {
            return new ArrayList<>();
        }

        return conditionArray.asList().stream()
                .map(JsonElement::getAsString)
                .map(str -> {
                    String[] parts = str.split("=", 2);
                    if (parts.length < 2) {
                        throw new JsonParseException("Invalid condition format: '" + str + "'. Expected 'name value'.");
                    }

                    String conditionName = parts[0].trim();
                    String value = parts[1].trim();

                    IVisionCondition condition = CONDITIONS.get(conditionName);
                    if (condition == null) {
                        throw new JsonParseException("Unknown condition: '" + conditionName + "'.");
                    }

                    return new VisionValue.ConditionEntry(condition, value);
                })
                .collect(Collectors.toList());
    }
}
