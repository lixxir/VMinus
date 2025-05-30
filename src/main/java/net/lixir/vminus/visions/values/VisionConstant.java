package net.lixir.vminus.visions.values;

import javax.annotation.Nullable;

public class VisionConstant<V> {
    private V value = null;
    private short priority = 0;

    public @Nullable V getValue() {
        return value;
    }

    public void setValue(V v) {
        this.value = v;
    }

    public boolean isSet() {
        return value != null;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }
}
