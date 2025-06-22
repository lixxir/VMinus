package net.lixir.vminus.vision.values;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.lixir.vminus.vision.values.conditions.AbstractVisionCondition;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class VisionProperty<T> {
    public static short DEFAULT_PRIORITY = 500;
    public static short MIN_PRIORITY = 0;
    public static short MAX_PRIORITY = 1000;
    public static String PRIORITY_TAG = "/priority";

    private final ImmutableList<ImmutableList<AbstractVisionCondition>> conditions;
    private final short priority;
    private final T value;

    public VisionProperty(T value, List<List<AbstractVisionCondition>> conditions) {
        this.conditions = conditions.stream()
                .map(ImmutableList::copyOf)
                .collect(ImmutableList.toImmutableList());
        this.value = value;
        this.priority = DEFAULT_PRIORITY;
    }

    public VisionProperty(T value, List<List<AbstractVisionCondition>> conditions, short priority) {
        this.conditions = conditions.stream()
                .map(ImmutableList::copyOf)
                .collect(ImmutableList.toImmutableList());
        this.value = value;
        this.priority = (short) Mth.clamp(priority, MIN_PRIORITY, MAX_PRIORITY);
    }

    public static <T> VisionProperty<T> create(T value, JsonObject arrayObject, JsonObject jsonObject, String key) {
        return new VisionProperty<>(value, AbstractVisionCondition.parseVisionConditions(arrayObject, jsonObject), getPriorityFromJsonObject(jsonObject, key));
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

    public short getPriority() {
        return priority;
    }

    public boolean testConditions(@Nullable VisionContext args) {
        if (args == null || conditions.isEmpty())
            return true;
        for (List<AbstractVisionCondition> visionConditionList : conditions) {
            if (visionConditionList.stream().allMatch(c -> c.test(args) != c.isInverted())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        VisionProperty<?> other = (VisionProperty<?>) obj;
        return priority == other.priority &&
                Objects.equals(value, other.value) &&
                Objects.equals(conditions, other.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, priority, conditions);
    }

    @Override
    public String toString() {
        return "VisionProperty{" +
                "value=" + value +
                ", priority=" + priority +
                ", conditions=" + conditions +
                '}';
    }


    public boolean isConstant() {
        return this.conditions.isEmpty();
    }

    public T getValue() {
        return value;
    }
}
