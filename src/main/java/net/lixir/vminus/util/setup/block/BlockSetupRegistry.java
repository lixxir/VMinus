package net.lixir.vminus.util.setup.block;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockSetupRegistry<V> {
    private final Map<String, List<V>> registries = new ConcurrentHashMap<>();

    public synchronized void register(String modId, V value) {
        registries.computeIfAbsent(modId, k -> new CopyOnWriteArrayList<>()).add(value);
    }

    public List<V> getValues(String modId) {
        return registries.getOrDefault(modId, List.of());
    }

    public Map<String, List<V>> getValues() {
        return registries;
    }
}
