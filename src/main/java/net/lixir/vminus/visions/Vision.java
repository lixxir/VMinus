package net.lixir.vminus.visions;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lixir.vminus.util.FixedByteKeyMap;
import net.lixir.vminus.visions.resources.codec.AbstractVisionCodec;
import net.lixir.vminus.visions.values.VisionProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Vision {
    private Map<String, VisionProperty<?>> visionProperties = new HashMap<>();
    private ImmutableMap<String, VisionProperty<?>> frozenVisionProperties = ImmutableMap.of();
    private final ArrayDeque<String> entries = new ArrayDeque<>();
    private boolean isFrozen = false;

    public ArrayDeque<String> getEntries() {
        return entries;
    }

    public void mergeEntries(ArrayList<String> newEntries) {
        if (isFrozen)
            throw new IllegalStateException("Cannot merge entries into Vision after it has been frozen.");
        this.entries.addAll(newEntries);
    }

    public List<VisionProperty<?>> getProperties() {
        return (isFrozen) ? new ArrayList<>(frozenVisionProperties.values()) : new ArrayList<>(visionProperties.values());
    }

    public @Nullable VisionProperty<?> getProperty(String key) {
        return (isFrozen) ? frozenVisionProperties.get(key) : visionProperties.get(key);
    }

    public <V> VisionProperty<V> create(String id, AbstractVisionCodec<V> codec) {
        return create(id, codec, false);
    }

    public <V> VisionProperty<V> create(String id, AbstractVisionCodec<V> codec, boolean stackable) {
        if (isFrozen)
            throw new IllegalStateException("Cannot create VisionProperty '" + id + "' after Vision has been frozen.");
        if (visionProperties.containsKey(id))
            throw new IllegalArgumentException("VisionProperty with name '" + id + "' already exists.");
        VisionProperty<V> property = new VisionProperty<>(id, codec, stackable);
        visionProperties.put(id, property);
        return property;
    }

    @SuppressWarnings("unchecked")
    public void merge(@NotNull Vision vision) {
        if (isFrozen)
            throw new IllegalStateException("Cannot merge into Vision after it has been frozen.");
        for (VisionProperty<?> entry : vision.getProperties()) {
            VisionProperty<?> local = getProperty(entry.getId());
            if (local != null) {
                ((VisionProperty<Object>) local).merge((VisionProperty<Object>) entry);
            }
        }
    }

    public List<String> getPropertyStrings() {
        return visionProperties.values().stream()
                .map(Object::toString)
                .toList();
    }

    @Override
    public String toString() {
        return "Vision{" +
                "properties=" + getPropertyStrings()
                + '}';
    }

    @SuppressWarnings("unchecked")
    public @Nullable JsonObject serialize() {
        JsonObject root = new JsonObject();
        for (Map.Entry<String, VisionProperty<?>> entry : visionProperties.entrySet()) {
            String id = entry.getKey();
            VisionProperty<Object> visionProperty = (VisionProperty<Object>) entry.getValue();
            JsonArray jsonArray = new JsonArray();

            for (Object value : visionProperty.values()) {
                JsonObject propertyObject = visionProperty.getCodec().encode(value);
                if (propertyObject != null) {
                    jsonArray.add(propertyObject);
                }
            }

            if (!jsonArray.isEmpty()) {
                root.add(id, jsonArray);
            }
        }

        return root;
    }


    public boolean isEmpty() {
        for (Map.Entry<String, VisionProperty<?>> entry : visionProperties.entrySet()) {
            VisionProperty<?> visionProperty = entry.getValue();
            if (!visionProperty.values().isEmpty())
                return false;
        }
        return true;
    }

    public abstract String getEntryListName();

    public void freeze() {
        if (isFrozen)
            return;
        isFrozen = true;

        for (VisionProperty<?> prop : visionProperties.values())
            prop.freeze();

        frozenVisionProperties = ImmutableMap.copyOf(visionProperties);
        visionProperties = Collections.unmodifiableMap(visionProperties);
        entries.clear();
    }

    public boolean isFrozen() {
        return isFrozen;
    }
}
