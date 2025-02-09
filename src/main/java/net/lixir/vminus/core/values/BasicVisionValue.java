package net.lixir.vminus.core.values;

import java.util.List;

public class BasicVisionValue<T> extends VisionValue implements IVisionValue<T> {
    private final T value;

    public BasicVisionValue(T value, List<ConditionEntry> conditions) {
        super(conditions);
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
