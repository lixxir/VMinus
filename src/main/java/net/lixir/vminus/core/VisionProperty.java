package net.lixir.vminus.core;

import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.values.IVisionValue;
import net.lixir.vminus.core.values.VisionConstant;
import net.lixir.vminus.core.values.VisionValue;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class VisionProperty<T extends IVisionValue<V>, V> {
    private final ArrayList<T> values = new ArrayList<>();
    private final String name;
    private final VisionConstant<V> constant = new VisionConstant<>();

    public VisionProperty(String name) {
        this.name = name;
    }

    public void addValue(T value) {
        // If the conditions of a value are empty, set a constant to save on performance cost, since that value will always stay the same.
        if (value instanceof VisionValue visionValue) {
            if (visionValue.getConditions().isEmpty()) {
                if (!constant.isSet() || visionValue.getPriority() >= constant.getPriority()) {
                    constant.setValue(value.get());
                    constant.setPriority(visionValue.getPriority());
                }
            }
        }
        values.add(value);
    }

    public void mergeValues(VisionProperty<T, V> other) {
        // Overrides this value with the other constant if priority is higher
        if (other.getConstant().isSet() && other.getConstant().getPriority() >= this.constant.getPriority()) {
            this.constant.setValue(other.getConstant().getValue());
        }
        // Merges all property values
        for (T value : other.values) {
            this.addValue(value);
        }
    }

    public VisionConstant<V> getConstant() {
        return this.constant;
    }

    public V getValue() {
        return getValue(null);
    }

    public V getValue(@Nullable VisionConditionArguments visionConditionArguments) {
        V chosenResult = null;
        int previousPriority = 0;
        if (constant.isSet()) {
            chosenResult = constant.getValue();
            previousPriority = constant.getPriority();
        }
        for (T value : values) {
            V result = value.get();

            if (result != null && value instanceof VisionValue visionValue) {
                int priority = visionValue.getPriority();
                if (priority >= previousPriority && visionValue.testConditions(visionConditionArguments)) {
                    previousPriority = priority;
                    chosenResult = result;
                }
            }

        }
        return chosenResult;
    }

    public List<V> getValues(@Nullable VisionConditionArguments visionConditionArguments) {
        ArrayList<V> vArrayList = new ArrayList<>();
        for (T value : values) {
            V result = value.get();
            if (result != null && value instanceof VisionValue visionValue) {
                if (visionValue.testConditions(visionConditionArguments)) {
                    vArrayList.add(result);
                }
            }
        }
        return vArrayList.stream().toList();
    }

    public String getName() {
        return name;
    }


}
