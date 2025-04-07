package net.lixir.vminus.datagen.util.simple;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatagenRegistry {
    // Serves as an object which new DatagenObjects can be registered from for simplifying DatagenObject use.
    private static final Map<String, List<DatagenObject>> VALUES = new ConcurrentHashMap<>();
    private final String modId;

    private DatagenRegistry(String modId) {
        this.modId = modId;
    }

    public static DatagenRegistry create(String modId) {
        return new DatagenRegistry(modId);
    }

    public List<DatagenObject> getValuesFromModId() {
        return VALUES.getOrDefault(modId, List.of());
    }

    public static List<DatagenObject> getValuesFromModId(String modId) {
        return VALUES.getOrDefault(modId, List.of());
    }

    public static Map<String, List<DatagenObject>> getValues() {
        return VALUES;
    }

    public synchronized void register(DatagenObject value) {
        VALUES.computeIfAbsent(modId, k -> new CopyOnWriteArrayList<>()).add(value);
    }
}
