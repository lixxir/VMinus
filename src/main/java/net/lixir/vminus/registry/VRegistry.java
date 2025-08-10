package net.lixir.vminus.registry;

import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.datagen.SoundDefinitionInfo;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.EntityEntry;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.RegistryEntry;
import net.lixir.vminus.registry.entry.accessor.BlockEntryAccessor;
import net.lixir.vminus.registry.entry.accessor.EntityEntryAccessor;
import net.lixir.vminus.registry.entry.accessor.ItemEntryAccessor;
import net.lixir.vminus.registry.entry.group.RegistryEntryGroup;
import net.lixir.vminus.registry.entry.group.RegistryEntryGroupsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("deprecation")
public class VRegistry {
    private static final ConcurrentHashMap<String, VRegistry> REGISTRIES = new ConcurrentHashMap<>();
    private final NonNullList<Block> blocks = NonNullList.create();
    private final NonNullList<EntityType<?>> entityTypes = NonNullList.create();
    private final NonNullList<SoundEvent> soundEvents = NonNullList.create();
    private final NonNullList<Item> items = NonNullList.create();
    private final NonNullList<SoundDefinitionInfo> soundDefinitionInfo = NonNullList.create();
    private final NonNullList<Fluid> fluids = NonNullList.create();
    private final String modId;

    private VRegistry(String modId) {
        this.modId = modId;
    }

    @SuppressWarnings("unchecked")
    public static @Nullable <E extends RegistryEntry<E, T>, T> RegistryEntry<E, T> getRegistryEntry(@NotNull T targetObject) {
        Class<?> targetClass = targetObject.getClass();
        VMinus.LOGGER.debug("[DEBUG] Looking for registry entry for object of type: {}", targetClass.getName());

        List<RegistryEntryGroup<?>> compatibleGroups = new ArrayList<>();
        RegistryEntryGroup<?> bestGroup = null;
        int bestDistance = Integer.MAX_VALUE;

        Map<RegistryEntryGroup<?>, Integer> groupDistances = new HashMap<>();
        for (Map.Entry<Class<?>, RegistryEntryGroup<?>> entry : RegistryEntryGroupsProvider.getAssignedEntries().entrySet()) {
            Class<?> groupClass = entry.getKey();
            if (groupClass.isAssignableFrom(targetClass)) {
                RegistryEntryGroup<?> group = entry.getValue();
                compatibleGroups.add(group);

                int distance = getClassDistance(targetClass, groupClass);
                groupDistances.put(group, distance);
                VMinus.LOGGER.debug("[DEBUG] Compatible group found: {} -> distance={}", groupClass.getName(), distance);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestGroup = group;
                    VMinus.LOGGER.debug("[DEBUG] New best group selected (distance {}): {}", distance, groupClass.getName());
                }
            }
        }

        // Match any suppliers
        for (Map.Entry<Supplier<Object>, RegistryEntryGroup<?>> entry : RegistryEntryGroupsProvider.getAssignedSuppliers().entrySet()) {
            Object supplied = entry.getKey().get();
            if (targetObject.equals(supplied)) {
                bestGroup = entry.getValue();
                VMinus.LOGGER.debug("[DEBUG] Supplier match found, overriding best group: {}", bestGroup);
                break;
            }
        }

        compatibleGroups.remove(bestGroup);
        if (bestGroup != null) {
            VMinus.LOGGER.debug("[DEBUG] Final best group: {}", bestGroup);
            RegistryEntry<E, T> bestEntry = (RegistryEntry<E, T>) bestGroup.getEntry();

            // Sort classes from highest to lowest distance
            compatibleGroups.sort(Comparator.comparingInt(g -> groupDistances.getOrDefault(g, Integer.MAX_VALUE)));
            for (RegistryEntryGroup<?> group : compatibleGroups) {
                RegistryEntry<E, T> otherEntry = (RegistryEntry<E, T>) group.getEntry();
                VMinus.LOGGER.debug("[DEBUG] Merging group entry into best entry: {}", otherEntry);
                bestEntry.merge((E) otherEntry);
            }
            VMinus.LOGGER.debug("[DEBUG] Final best entry: {}", bestEntry);
            return bestEntry;
        }

        VMinus.LOGGER.debug("[DEBUG] No matching registry entry found for object: {}", targetObject);
        return null;
    }


    private static int getClassDistance(Class<?> child, Class<?> parent) {
        int distance = 0;
        while (child != null && !child.equals(parent)) {
            child = child.getSuperclass();
            distance++;
        }
        return child == null ? Integer.MAX_VALUE : distance;
    }

    public static @Nullable ItemEntry getItemEntry(Item item) {
        RegistryEntry<?, ?> entry = getRegistryEntry(item);
        return (entry instanceof ItemEntry) ? (ItemEntry) entry : null;
    }

    public static @Nullable BlockEntry getBlockEntry(Block block) {
        RegistryEntry<?, ?> entry = getRegistryEntry(block);
        return (entry instanceof BlockEntry) ? (BlockEntry) entry : null;
    }

    public static @NotNull List<VRegistry> getRegistries() {
        return REGISTRIES.values().stream().toList();
    }

    public static VRegistry fromId(String id) {
        return REGISTRIES.get(id);
    }

    public static @NotNull VRegistry create(String modId, @Nullable RegistryEntryGroupsProvider registryEntryGroupsProvider) {
        VRegistry registry = new VRegistry(modId);
        if (REGISTRIES.putIfAbsent(modId, registry) != null)
            throw new IllegalStateException("UnifiedRegistry already exists for mod ID: " + modId);
        if (registryEntryGroupsProvider != null)
            registryEntryGroupsProvider.run();
        VMinus.LOGGER.info("Registered Unified Registry with ID: {}", modId);
        return registry;
    }

    public List<Fluid> getFluids() {
        return fluids;
    }

    public <T extends Fluid> T fluid(@NotNull String name, @NotNull T fluid) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(modId, name);
        T registered = Registry.register(BuiltInRegistries.FLUID, location, fluid);
        fluids.add(registered);
        return registered;
    }

    public List<SoundEvent> getSoundEvents() {
        return soundEvents;
    }

    public List<SoundDefinitionInfo> getSoundDefinitionInfo() {
        return soundDefinitionInfo;
    }

    public String getModId() {
        return modId;
    }

    public Block block(@NotNull String name, @NotNull Block block) {
        return block(name, block, BlockEntry.defaults());
    }

    public Item blockItem(@NotNull Block block) {
        return blockItem(null, block, new BlockItem(block, new Item.Properties()), ItemEntry.from(block));
    }

    public Item blockItem(@NotNull Block block, @NotNull BlockItem blockItem) {
        return blockItem(null, block, blockItem, ItemEntry.from(block));
    }

    public Item blockItem(@NotNull Block block, @NotNull BlockItem blockItem, @NotNull ItemEntry itemEntry) {
        return blockItem(null, block, blockItem, itemEntry);
    }

    public Item blockItem(@Nullable String name, @NotNull Block block, @NotNull BlockItem blockItem, @NotNull ItemEntry itemEntry) {
        if (name == null) {
            ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
            name = blockKey.getPath();
        }
        return item(name, blockItem, itemEntry.merge(ItemEntry.from(block))); // Make sure to always get the entry from the block.
    }

    public Item blockItem(@NotNull Block block, @NotNull ItemEntry itemEntry) {
        return blockItem(null, block, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(@NotNull String name, @NotNull Block block) {
        return this.blockItem(name, block, new BlockItem(block, new Item.Properties()), ItemEntry.from(block));
    }

    public Item blockItem(@NotNull String name, @NotNull Block block, @NotNull ItemEntry itemEntry) {
        return this.blockItem(name, block, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(@NotNull BlockItem blockItem) {
        Block block = blockItem.getBlock();
        return this.blockItem(null, block, blockItem, ItemEntry.from(block));
    }

    public Item blockItem(@NotNull BlockItem blockItem, @NotNull ItemEntry itemEntry) {
        Block block = blockItem.getBlock();
        return this.blockItem(null, block, blockItem, itemEntry);
    }

    // Main method
    public Block block(@NotNull String name, @NotNull Block block, @NotNull BlockEntry blockEntry) {
        BlockEntryAccessor accessor = (BlockEntryAccessor) block;
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(modId, name);
        Block registered = Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        blocks.add(registered);
        accessor.vminus$setEntry(blockEntry);
        if (blockEntry.isDefaulted())
            blockEntry.setDefault(registered);
        VMinus.LOGGER.debug("Final Registered entry({}) for {}", accessor.vminus$getEntry(), name);
        return registered;
    }

    public Item item(@NotNull String name) {
        return item(name, new Item(new Item.Properties()), ItemEntry.defaults());
    }

    public Item item(@NotNull String name, @NotNull Item item) {
        return item(name, item, ItemEntry.defaults());
    }

    public Item item(@NotNull String name, @NotNull ItemEntry itemEntry) {
        return item(name, new Item(new Item.Properties()), itemEntry);
    }

    // Main method
    public Item item(@NotNull String name, @NotNull Item item, @NotNull ItemEntry itemEntry) {
        ItemEntryAccessor accessor = (ItemEntryAccessor) item;
        Item registered = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, name), item);
        items.add(registered);
        accessor.vminus$setEntry(itemEntry);
        if (itemEntry.isDefaulted())
            itemEntry.setDefault(registered);

        if (registered instanceof BlockItem blockItem)
            blockItem.registerBlocks(Item.BY_BLOCK, registered);
        return registered;
    }

    public MobEffect effect(@NotNull String name, @NotNull MobEffect mobEffect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(modId, name), mobEffect);
    }

    public <T extends Entity> EntityType<T> entity(@NotNull String name, EntityType.@NotNull Builder<T> entityTypeBuilder) {
        return entity(name, entityTypeBuilder, null);
    }

    public <T extends Entity> EntityType<T> entity(@NotNull String name, EntityType.@NotNull Builder<T> entityTypeBuilder, @Nullable EntityEntry entityEntry) {
        EntityType<T> entityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), entityTypeBuilder.build(name));
        EntityEntryAccessor accessor = (EntityEntryAccessor) entityType;
        if (entityEntry == null)
            entityEntry = EntityEntry.of();
        accessor.vminus$setEntry(entityEntry);
        entityTypes.add(entityType);
        return entityType;
    }

    public CreativeModeTab tab(@NotNull String name, @NotNull CreativeModeTab creativeModeTab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(modId, name), creativeModeTab);
    }

    public Feature<?> feature(@NotNull String name, @NotNull Feature<?> feature) {
        return Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.fromNamespaceAndPath(modId, name), feature);
    }

    public <T extends FoliagePlacer> FoliagePlacerType<T> foliagePlacer(@NotNull String name, @NotNull FoliagePlacerType<T> foliagePlacerType) {
        return Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), foliagePlacerType);
    }

    public <T extends TrunkPlacer> TrunkPlacerType<T> trunkPlacer(@NotNull String name, @NotNull TrunkPlacerType<T> trunkPlacerType) {
        return Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), trunkPlacerType);
    }

    public PaintingVariant painting(@NotNull String name, int width, int height) {
        return painting(name, new PaintingVariant(width * 16, height * 16));
    }

    public Attribute attribute(@NotNull String name, @NotNull Attribute attribute) {
        return Registry.register(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(modId, name), attribute);
    }

    public Attribute attribute(@NotNull String name, double defaultValue, double minimumValue, double maximumValue) {
        return Registry.register(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(modId, name), new RangedAttribute("attribute." + modId + ".name." + name, defaultValue, minimumValue, maximumValue).setSyncable(true));
    }

    public PaintingVariant painting(@NotNull String name, @NotNull PaintingVariant paintingVariant) {
        return Registry.register(BuiltInRegistries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath(modId, name), paintingVariant);
    }

    public <T extends BlockEntity> BlockEntityType<T> blockEntity(@NotNull String name, @NotNull BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), type);
    }

    public Item dye(@NotNull String id, @NotNull DyeColor dyeColor) {
        return item(id, new DyeItem(dyeColor, new Item.Properties()));
    }

    public <T extends BlockEntity> BlockEntityType<T> blockEntity(@NotNull String name, @NotNull BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), BlockEntityType.Builder.of(factory, blocks).build(null));
    }

    public SoundEvent sound(@NotNull String name, String eventPath) {
        return sound(name, SoundDefinitionInfo.of(eventPath));
    }

    public SoundEvent sound(@NotNull String name, @NotNull SoundDefinitionInfo soundDefinitionInfo) {
        SoundEvent soundEvent = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(modId, name), SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
        this.soundDefinitionInfo.add(soundDefinitionInfo.setSoundEvent(soundEvent));
        return soundEvent;
    }

    public Holder.Reference<SoundEvent> soundHolder(@NotNull String name, @NotNull SoundDefinitionInfo soundDefinitionInfo) {
        SoundEvent soundEvent = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(modId, name), SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
        this.soundDefinitionInfo.add(soundDefinitionInfo.setSoundEvent(soundEvent));
        return BuiltInRegistries.SOUND_EVENT.getHolder(BuiltInRegistries.SOUND_EVENT.getResourceKey(soundEvent).orElseThrow()).orElseThrow();
    }

    public <T extends ParticleOptions> ParticleType<T> particle(@NotNull String name, @NotNull ParticleType<T> particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), particleType);
    }

    public <T extends TreeDecorator> TreeDecoratorType<T> treeDecorator(@NotNull String name, @NotNull TreeDecoratorType<T> treeDecoratorType) {
        return Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), treeDecoratorType);
    }

    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public List<EntityType<?>> getEntityTypes() {
        return Collections.unmodifiableList(entityTypes);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Pair<Item, ItemEntry>> getItemEntryPairs() {
        return items.stream()
                .map(item -> new Pair<>(item, ItemEntry.of(item)))
                .filter(entry -> !entry.getSecond().isEmpty())
                .toList();
    }
}
