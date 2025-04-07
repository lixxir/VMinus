package net.lixir.vminus.visions.values;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.lixir.vminus.visions.conditions.AbstractVisionCondition;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;

import javax.annotation.Nullable;
import java.util.List;

public class VisionValue<T> {
    private final List<List<AbstractVisionCondition>> conditions;
    private final int priority;
    private final T value;

    public VisionValue(T value, List<List<AbstractVisionCondition>> conditions) {
        this.conditions = conditions;
        this.value = value;
        this.priority = 500;
    }

    public VisionValue(T value, List<List<AbstractVisionCondition>> conditions, int priority) {
        this.conditions = conditions;
        this.value = value;
        this.priority = priority;
    }

    public static <T> VisionValue<T> create(T value, JsonObject arrayObject, JsonObject jsonObject, String key) {
        return  new VisionValue<>(value, AbstractVisionCondition.parseVisionConditions(arrayObject, jsonObject), getPriorityFromJsonObject(jsonObject, key));
    }


    public List<List<AbstractVisionCondition>> getConditions() {
        return conditions;
    }

    public int getPriorityFromJsonObject() {
        return priority;
    }

    private static int getPriorityFromJsonObject(JsonObject jsonObject, String key) throws JsonParseException {
        String priorityName = key + "/priority";
        if (jsonObject.has(priorityName)) {
            if (!jsonObject.get(priorityName).isJsonPrimitive()) {
                throw new JsonParseException(key + " priority value is not a JsonPrimitive.");
            }
            JsonPrimitive jsonPrimitive = jsonObject.get(priorityName).getAsJsonPrimitive();
            return jsonPrimitive.getAsInt();
        }
        return 0;
    }

    public boolean testConditions(@Nullable VisionConditionArguments args) {
        if (args == null || conditions.isEmpty())
            return true;
        for (List<AbstractVisionCondition> visionConditionList : conditions) {
            if (visionConditionList.stream().allMatch(c -> c.test(args) != c.isInverted())) {
                return true;
            }
        }
        return false;
    }


    public T get() {
        return value;
    }
}
