package net.lixir.vminus.visions.values;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.resources.parser.AbstractVisionParser;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class VisionProperty<V> {
    private final ArrayList<VisionValue<V>> values = new ArrayList<>();
    private final String id;
    private final AbstractVisionParser<V> parser;
    private final VisionConstant<V> constant = new VisionConstant<>();

    public VisionProperty(String id, AbstractVisionParser<V> parser) {
        this.id = id;
        this.parser = parser;
    }

    public void addList(@Nullable List<VisionValue<V>> visionValues) {
        if (visionValues == null)
            return;
        for (VisionValue<V> visionValue : visionValues) {
            add(visionValue);
        }
    }

    public void add(@Nullable VisionValue<V> value) {
        if (value == null)
            return;
        // If the conditions of a value are empty, set a constant to save on performance cost, since that value will always stay the same.
        if (value.getConditions().isEmpty()) {
                if (!constant.isSet() || value.getPriorityFromJsonObject() >= constant.getPriority()) {
                    constant.setValue(value.get());
                    constant.setPriority(value.getPriorityFromJsonObject());
                }
            }

        values.add(value);
    }

    public void merge(VisionProperty<V> other) {
        // Overrides this value with the other constant if priority is higher
        if (other.getConstant().isSet() && other.getConstant().getPriority() >= this.constant.getPriority()) {
            this.constant.setValue(other.getConstant().getValue());
        }
        // Merges all property values
        for (VisionValue<V> value : other.values) {
            this.add(value);
        }
    }

    public VisionConstant<V> getConstant() {
        return this.constant;
    }

    public V value() {
        return value(null);
    }

    public V value(@Nullable VisionConditionArguments visionConditionArguments) {
        V chosenResult = null;
        int previousPriority = 0;
        if (constant.isSet()) {
            chosenResult = constant.getValue();
            previousPriority = constant.getPriority();
        }
        for (VisionValue<V> value : values) {
            V result = value.get();

            if (result != null) {
                int priority = value.getPriorityFromJsonObject();
                if (priority >= previousPriority && value.testConditions(visionConditionArguments)) {
                    previousPriority = priority;
                    chosenResult = result;
                }
            }

        }
        return chosenResult;
    }

    public List<V> values() {
        return values(null);
    }

    public List<V> values(@Nullable VisionConditionArguments visionConditionArguments) {
        ArrayList<V> vArrayList = new ArrayList<>();
        for (VisionValue<V> value : values) {
            V result = value.get();
            if (result != null) {
                if (value.testConditions(visionConditionArguments)) {
                    vArrayList.add(result);
                }
            }
        }
        return vArrayList.stream().toList();
    }

    public String getId() {
        return id;
    }

    public void parseAndAdd(JsonObject jsonObject, ICondition.IContext context) {
        List<VisionValue<V>> parsedList = parser.parse(jsonObject, this.id, context);
        addList(parsedList);
    }
}
