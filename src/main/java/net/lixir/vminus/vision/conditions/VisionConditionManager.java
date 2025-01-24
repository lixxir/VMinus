package net.lixir.vminus.vision.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class VisionConditionManager {
    private static final Map<String, IVisionCondition> CONDITIONS = new HashMap<>();

    static {
        CONDITIONS.put("rarity", new RarityCondition());
    }

    public static boolean evaluateCondition(String key, Object object, @Nullable String excludeCondition) {
        String[] parts = key.split("=", 2);
        if (parts.length < 2) return false;

        String conditionType = parts[0].trim();
        String value = parts[1].trim();

        if (conditionType.equals(excludeCondition))
            return true;

        IVisionCondition condition = CONDITIONS.get(conditionType);
        if (condition != null) {
            return condition.test(object, value);
        }

        return false;
    }

    private final static HashMap<String, String> CONDITION_IDENTIFIERS = new HashMap<>();

    private static String getConditionIdentifier(@Nullable String originalKey) {
        if (originalKey == null || !originalKey.contains("-")) {
            return "";
        }
        return CONDITION_IDENTIFIERS.computeIfAbsent(originalKey, key ->
                key.substring(0, key.indexOf('-')) + "$conditions"
        );
    }


    public static boolean evaluateAllConditions(@Nullable JsonObject visionData, @Nullable JsonArray jsonArray, @Nullable Object object, @Nullable String excludeCondition, @Nullable String originalKey) {
        boolean valid = true;
        String conditionsIdentifier = getConditionIdentifier(originalKey);
        for (int i = 0; i < 2; i++) {
            JsonArray iteratingArray;
            if (i == 0 && visionData != null && visionData.has(conditionsIdentifier)) {
                iteratingArray = visionData.getAsJsonArray(conditionsIdentifier);
            } else if (i == 1 && jsonArray != null && valid) {
                iteratingArray = jsonArray;
            } else {
                continue;
            }
            for (JsonElement element : iteratingArray) {
                if (!element.isJsonPrimitive()) {
                    continue;
                }
                String conditionValue = element.getAsString();
                if (conditionValue.equals("or")) {
                    if (valid)
                        return valid;
                    valid = true;
                } else {
                    boolean inverted = conditionValue.startsWith("!");
                    if (inverted)
                        conditionValue = conditionValue.substring(1);
                    if (evaluateCondition(conditionValue, object, excludeCondition) == inverted) {
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

    public static void registerCondition(String name, IVisionCondition condition) {
        CONDITIONS.put(name, condition);
    }
}
