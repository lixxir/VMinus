package net.lixir.vminus.core.values;

public class VisionConstant<V> {
    private V v = null;
    private boolean set = false;
    private int priority = 0;

    public V getValue() {
        return v;
    }

    public void setValue(V v) {
        this.v = v;
        this.set = true;
    }

    public boolean isSet() {
        return set;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
