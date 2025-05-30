package net.lixir.vminus.visions.values;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.lixir.vminus.visions.conditions.AbstractVisionCondition;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.List;

public class VisionValue<T> {
    public static short DEFAULT_PRIORITY = 500;
    public static short MIN_PRIORITY = 0;
    public static short MAX_PRIORITY = 1000;
    public static String PRIORITY_TAG = "/priority";

    private final ImmutableList<ImmutableList<AbstractVisionCondition>> conditions;
    private final short priority;
    private final T value;

    public VisionValue(T value, List<List<AbstractVisionCondition>> conditions) {
        this.conditions = conditions.stream()
                .map(ImmutableList::copyOf)
                .collect(ImmutableList.toImmutableList());
        this.value = value;
        this.priority = DEFAULT_PRIORITY;
    }

    public VisionValue(T value, List<List<AbstractVisionCondition>> conditions, short priority) {
        this.conditions = conditions.stream()
                .map(ImmutableList::copyOf)
                .collect(ImmutableList.toImmutableList());
        this.value = value;
        this.priority = (short) Mth.clamp(priority, MIN_PRIORITY, MAX_PRIORITY);
    }

    public static <T> VisionValue<T> create(T value, JsonObject arrayObject, JsonObject jsonObject, String key) {
        return new VisionValue<>(value, AbstractVisionCondition.parseVisionConditions(arrayObject, jsonObject), getPriorityFromJsonObject(jsonObject, key));
    }

    private static short getPriorityFromJsonObject(JsonObject jsonObject, String key) throws JsonParseException {
        String priorityName = key + PRIORITY_TAG;
        if (jsonObject.has(priorityName)) {
            if (!jsonObject.get(priorityName).isJsonPrimitive())
                throw new JsonParseException(key + " priority value is not a JsonPrimitive.");
            JsonPrimitive jsonPrimitive = jsonObject.get(priorityName).getAsJsonPrimitive();
            return jsonPrimitive.getAsShort();
        }
        return 0;
    }

    public ImmutableList<ImmutableList<AbstractVisionCondition>> getConditions() {
        return conditions;
    }

    public short getPriorityFromJsonObject() {
        return priority;
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
