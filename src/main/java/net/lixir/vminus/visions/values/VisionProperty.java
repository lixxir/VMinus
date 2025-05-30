package net.lixir.vminus.visions.values;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.lixir.vminus.util.FixedByteKeyMap;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.resources.codec.AbstractVisionCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class VisionProperty<V> {
    private final String id;
    private final AbstractVisionCodec<V> codec;
    private final VisionConstant<V> constant = new VisionConstant<>();
    private final boolean stackable;


    private List<VisionValue<V>> mutableValues = new ArrayList<>();
    private ImmutableList<VisionValue<V>> frozenValues = ImmutableList.of();
    private boolean isFrozen = false;

    public VisionProperty(String id, AbstractVisionCodec<V> codec, boolean stackable) {
        this.id = id;
        this.codec = codec;
        this.stackable = stackable;

    }

    public AbstractVisionCodec<V> getCodec() {
        return codec;
    }

    public void add(@Nullable List<VisionValue<V>> visionValues) {
        if (isFrozen)
            throw new IllegalStateException("Cannot add values to VisionProperty '" + id + "' after it has been frozen.");
        if (visionValues == null || (!stackable && constant.isSet()))
            return;
        for (VisionValue<V> visionValue : visionValues) {
            add(visionValue);
        }
    }

    public void add(@Nullable VisionValue<V> value) {
        if (isFrozen)
            throw new IllegalStateException("Cannot add values to VisionProperty '" + id + "' after it has been frozen.");
        if (value == null || (!stackable && constant.isSet()))
            return;
        if (value.getConditions().isEmpty()) {
            if (!constant.isSet() || value.getPriorityFromJsonObject() >= constant.getPriority()) {
                constant.setValue(value.get());
                constant.setPriority(value.getPriorityFromJsonObject());
            }
        }

        mutableValues.add(value);
    }

    public void merge(VisionProperty<V> other) {
        if (isFrozen)
            throw new IllegalStateException("Cannot merge into VisionProperty '" + id + "' after it has been frozen.");

        if (other.constant.isSet() && other.constant.getPriority() >= this.constant.getPriority())
            this.constant.setValue(other.constant.getValue());

        for (VisionValue<V> value : other.getAllValues())
            this.add(value);
    }

    public void freeze() {
        if (isFrozen)
            return;
        this.frozenValues = ImmutableList.copyOf(mutableValues);
        this.mutableValues = null;
        this.isFrozen = true;
    }

    private List<VisionValue<V>> getAllValues() {
        return isFrozen ? frozenValues : mutableValues;
    }

    public VisionConstant<V> getConstant() {
        return constant;
    }

    public V value() {
        return value(null);
    }

    public @Nullable V value(@Nullable VisionConditionArguments visionConditionArguments) {
        V chosenResult = null;
        int previousPriority = 0;

        if (constant.isSet()) {
            chosenResult = constant.getValue();
          //  previousPriority = constant.getPriority();
            return chosenResult;
        }

        for (VisionValue<V> value : getAllValues()) {
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
        List<V> resultList = new ArrayList<>();
        if (constant.isSet() && !stackable) {
            assert constant.getValue() != null;
            return List.of(constant.getValue());
        }
        for (VisionValue<V> value : getAllValues()) {
            V result = value.get();
            if (result != null && value.testConditions(visionConditionArguments)) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    public String getId() {
        return id;
    }

    public void parseAndAdd(JsonObject jsonObject, @Nullable ICondition.IContext context) {
        if (isFrozen)
            throw new IllegalStateException("Cannot parse and add to VisionProperty '" + id + "' after it has been frozen.");
        List<VisionValue<V>> parsedList = codec.decode(jsonObject, this.id, context);
        add(parsedList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VisionProperty[");
        sb.append("name=\"").append(id).append("\", ");
        if (constant.isSet()) {
            sb.append("constant=").append(constant.getValue()).append(" (priority=").append(constant.getPriority()).append("), ");
        } else {
            List<VisionValue<V>> currentValues = getAllValues();
            if (!currentValues.isEmpty()) {
                sb.append("values=[");
                for (int i = 0; i < currentValues.size(); i++) {
                    VisionValue<V> v = currentValues.get(i);
                    sb.append("\"").append(v.get()).append("\"");
                    if (i < currentValues.size() - 1) sb.append(", ");
                }
                sb.append("], ");
            }
        }
        sb.append("parser=\"").append(codec != null ? codec.getClass().getSimpleName() : "null").append("\"]");
        return sb.toString();
    }

    public boolean isStackable() {
        return stackable;
    }

    public boolean isFrozen() {
        return isFrozen;
    }


}
