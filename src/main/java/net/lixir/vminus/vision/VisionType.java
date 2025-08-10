package net.lixir.vminus.vision;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class VisionType<T> {
    private final String id;
    private final String multiList;
    private final Registry<? extends T> registry;
    private final Function<ResourceLocation, ?> registryGetter;
    private final BiConsumer<Object, ResourceLocation> visionSetter;

    public VisionType(
            String id,
            String multiList,
            Registry<? extends T> registry,
            Function<ResourceLocation, ?> registryGetter,
            BiConsumer<Object, ResourceLocation> visionSetter
    ) {
        this.id = id;
        this.multiList = multiList;
        this.registry = registry;
        this.registryGetter = registryGetter;
        this.visionSetter = visionSetter;
    }

    private final Map<ResourceLocation, Vision> visions = new HashMap<>();

    public @NotNull @UnmodifiableView Map<ResourceLocation, Vision> getVisions() {
        return Collections.unmodifiableMap(visions);
    }

    public <E> void applyVision(E target, ResourceLocation id) {
        visionSetter.accept(target, id);
    }

    public void resetVisionTypes() {
        for (T value : registry) {
            ((VisionDuck) value).vMinus$setVisionId(null);
        }
    }

    public void putVision(ResourceLocation id, Vision vision) {
        visions.put(id,vision);
    }

    public static void resetAllVisionTypes() {
        for (VisionType<?> visionType : VisionTypes.getAll()) {
            visionType.resetVisionTypes();
            visionType.visions.clear();
        }
    }

    public String getId() {
        return id;
    }

    public String getMultiList() {
        return multiList;
    }

    @Contract(pure = true)
    public @NotNull String getDirectory() {
        return "visions/" + multiList;
    }

    public Registry<? extends T> getRegistry() {
        return registry;
    }

    public Function<ResourceLocation, ?> getRegistryGetter() {
        return registryGetter;
    }

    public BiConsumer<Object, ResourceLocation> getVisionSetter() {
        return visionSetter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (VisionType<?>) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.multiList, that.multiList) &&
                Objects.equals(this.registry, that.registry) &&
                Objects.equals(this.registryGetter, that.registryGetter) &&
                Objects.equals(this.visionSetter, that.visionSetter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, multiList, registry, registryGetter, visionSetter);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "VisionType[id=" + id + "]";
    }
}
