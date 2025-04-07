package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.parser.AbstractVisionParser;
import net.lixir.vminus.visions.values.VisionProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Vision {
    private final ConcurrentHashMap<String, VisionProperty<?>> visionProperties = new ConcurrentHashMap<>();
    private final ArrayList<String> entries = new ArrayList<>();

    public ArrayList<String> getEntries() {
        return entries;
    }

    public void mergeEntries(ArrayList<String> entries) {
        this.entries.addAll(entries);
    }

    public List<VisionProperty<?>> getProperties() {
        return visionProperties.values().stream().toList();
    }

    public @Nullable VisionProperty<?> getProperty(String key) {
        return visionProperties.get(key);
    }

    public <V> VisionProperty<V> create(String id, AbstractVisionParser<V> abstractVisionParser) {
        if (visionProperties.containsKey(id)) {
            throw new IllegalArgumentException("VisionProperty with id '" + id + "' already exists.");
        }
        VisionProperty<V> property = new VisionProperty<>(id, abstractVisionParser);
        visionProperties.put(id, property);
        return property;
    }

    @SuppressWarnings("unchecked")
    public void merge(@NotNull Vision vision) {
        for (VisionProperty<?> entry : vision.getProperties()) {
            VisionProperty<?> local = getProperty(entry.getId());
            if (local != null) {
                ((VisionProperty<Object>) local).merge((VisionProperty<Object>) entry);
            }
        }
    }
}
