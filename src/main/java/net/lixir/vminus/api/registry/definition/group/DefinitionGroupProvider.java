package net.lixir.vminus.api.registry.definition.group;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Provides centralized management for {@link DefinitionGroup} assignments.
 * <p>
 * Supports assigning groups to classes or deferred suppliers and retrieving them
 * for datagen, rendering, or other processing.
 */
public abstract class DefinitionGroupProvider {
    private static final Map<Class<?>, DefinitionGroup<?>> ASSIGNED_ENTRIES = new ConcurrentHashMap<>();
    private static final Map<Supplier<Object>, DefinitionGroup<?>> ASSIGNED_SUPPLIERS = new ConcurrentHashMap<>();

    /** Runs the provider to assign groups as needed. */
    public abstract void run();

    /**
     * Assigns a definition group to one or more classes.
     *
     * @param group the definition group to assign
     * @param classes the classes to assign it to
     * @param <T> the type of objects in the group
     */
    public static <T> void assign(@NotNull DefinitionGroup<T> group, @NotNull Class<?> @NotNull ... classes) {
        for (Class<?> clazz : classes)
            ASSIGNED_ENTRIES.put(clazz, group);
    }

    /**
     * Assigns a definition group to one or more suppliers.
     *
     * @param group the definition group to assign
     * @param suppliers the suppliers to assign it to
     * @param <T> the type of objects in the group
     */
    @SafeVarargs
    public static <T> void assign(@NotNull DefinitionGroup<T> group, @NotNull Supplier<Object> @NotNull ... suppliers) {
        for (Supplier<Object> supplier : suppliers)
            ASSIGNED_SUPPLIERS.put(supplier, group);
    }

    /**
     * Returns the map of suppliers assigned to definition groups.
     */
    @Contract(pure = true)
    public static @NotNull Map<Supplier<Object>, DefinitionGroup<?>> getAssignedSuppliers() {
        return ASSIGNED_SUPPLIERS;
    }

    /**
     * Returns the map of classes assigned to definition groups.
     */
    @Contract(pure = true)
    public static @NotNull Map<Class<?>, DefinitionGroup<?>> getAssignedEntries() {
        return ASSIGNED_ENTRIES;
    }

    /**
     * Creates a deferred block supplier from a namespace and path.
     *
     * @param namespace the namespace of the block
     * @param path the path of the block
     * @return a supplier that returns the block from the registry when accessed
     */
    public static @NotNull Supplier<Object> deferredBlock(String namespace, String path) {
        return deferredBlock(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    /**
     * Creates a deferred block supplier from a {@link ResourceLocation}.
     *
     * @param resourceLocation the location of the block in the registry
     * @return a supplier that returns the block from the registry when accessed
     */
    @SuppressWarnings("deprecation")
    public static @NotNull Supplier<Object> deferredBlock(ResourceLocation resourceLocation) {
        return () -> BuiltInRegistries.BLOCK.get(resourceLocation);
    }
}
