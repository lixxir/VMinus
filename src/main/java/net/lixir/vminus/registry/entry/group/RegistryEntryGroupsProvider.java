package net.lixir.vminus.registry.entry.group;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public abstract class RegistryEntryGroupsProvider {

    private static final Map<Class<?>, RegistryEntryGroup<?>> ASSIGNED_ENTRIES = new ConcurrentHashMap<>();
    private static final Map<Supplier<Object>, RegistryEntryGroup<?>> ASSIGNED_SUPPLIERS = new ConcurrentHashMap<>();

    public abstract void run();

    public static <T> void assign(@NotNull RegistryEntryGroup<T> group, @NotNull Class<?> @NotNull ... classes) {
        for (Class<?> clazz : classes)
            ASSIGNED_ENTRIES.put(clazz, group);
    }

    @SafeVarargs
    public static <T> void assign(@NotNull RegistryEntryGroup<T> group, @NotNull Supplier<Object> @NotNull ... suppliers) {
        for (Supplier<Object> supplier : suppliers)
            ASSIGNED_SUPPLIERS.put( supplier, group);
    }

    @Contract(pure = true)
    public static @NotNull Map<Supplier<Object>, RegistryEntryGroup<?>> getAssignedSuppliers() {
        return ASSIGNED_SUPPLIERS;
    }

    @Contract(pure = true)
    public static @NotNull Map<Class<?>, RegistryEntryGroup<?>> getAssignedEntries() {
        return ASSIGNED_ENTRIES;
    }

    public static @NotNull Supplier<Object> deferredBlock(String namespace, String path) {
        return deferredBlock(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static @NotNull Supplier<Object> deferredBlock(ResourceLocation resourceLocation) {
        return () -> BuiltInRegistries.BLOCK.get(resourceLocation);
    }

}
