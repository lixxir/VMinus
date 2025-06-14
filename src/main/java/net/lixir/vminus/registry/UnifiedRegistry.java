package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.entry.*;
import net.minecraft.core.Holder;
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
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("deprecation")
public class UnifiedRegistry {
    private static final ConcurrentHashMap<String, UnifiedRegistry> REGISTRIES = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, DefaultRegistryEntry<? extends RegistryEntry<?, ?>, ?>> DEFAULT_ENTRIES_REGISTRY = new ConcurrentHashMap<>();

    private final ArrayDeque<Block> blocks = new ArrayDeque<>();
    private final ArrayDeque<EntityType<?>> entityTypes = new ArrayDeque<>();
    private final ArrayDeque<SoundEvent> soundEvents = new ArrayDeque<>();
    private final ArrayDeque<Item> items = new ArrayDeque<>();
    private final ArrayDeque<SoundDefinitionInfo> soundDefinitionInfo = new ArrayDeque<>();

    private final String modId;
    private Consumer<UnifiedRegistry> setup;

    private UnifiedRegistry(String modId) {
        this.modId = modId;
    }

    public static <E extends RegistryEntry<E, T>, T> void setDefaultRegistryEntry(@NotNull Class<?> clazz, @NotNull DefaultRegistryEntry<E, T> defaultRegistryEntry) {
        if (!DEFAULT_ENTRIES_REGISTRY.containsKey(clazz) || defaultRegistryEntry.isOverwrite()) {
            DEFAULT_ENTRIES_REGISTRY.put(clazz, defaultRegistryEntry);
        }
    }

    @SuppressWarnings("unchecked")
    public static @Nullable <E extends RegistryEntry<E, T>, T> RegistryEntry<E, T> getRegistryEntry(Class<?> clazz) {
        Object exact = DEFAULT_ENTRIES_REGISTRY.get(clazz);
        if (exact instanceof DefaultRegistryEntry<?, ?> exactEntry) {
            VMinus.LOGGER.info("Found exact for {}", clazz);
            return (RegistryEntry<E, T>) exactEntry.getRegistryEntry();
        }


        Class<?> bestMatch = null;
        DefaultRegistryEntry<?, ?> bestEntry = null;

        for (Map.Entry<Class<?>, DefaultRegistryEntry<?, ?>> entry : DEFAULT_ENTRIES_REGISTRY.entrySet()) {

            Class<?> key = entry.getKey();
            if (key.isAssignableFrom(clazz)) {
                if (bestMatch == null || bestMatch.isAssignableFrom(key)) {
                    VMinus.LOGGER.info("Class={} and matches {}", clazz, key);
                    bestMatch = key;
                    bestEntry = entry.getValue();
                }
            }
        }

        return bestEntry != null ? (RegistryEntry<E, T>) bestEntry.getRegistryEntry() : null;
    }



    public static @Nullable BlockEntry getBlockEntry(Class<? extends Block> clazz) {
        RegistryEntry<?, ?> entry = getRegistryEntry(clazz);
        if (entry instanceof BlockEntry)
            VMinus.LOGGER.info("is a block entry..");
        return (entry instanceof BlockEntry) ? (BlockEntry) entry : null;
    }


    public static @NotNull List<UnifiedRegistry> getRegistries() {
        return REGISTRIES.values().stream().toList();
    }

    public static UnifiedRegistry fromId(String id) {
        return REGISTRIES.get(id);
    }

    public static @NotNull UnifiedRegistry create(String modId, Consumer<UnifiedRegistry> setup) {
        UnifiedRegistry registry = new UnifiedRegistry(modId);
        if (REGISTRIES.putIfAbsent(modId, registry) != null)
            throw new IllegalStateException("UnifiedRegistry already exists for mod ID: " + modId);
        registry.setSetup(setup);
        return registry;
    }


    public void setSetup(Consumer<UnifiedRegistry> setup) {
        this.setup = setup;
    }

    public void init() {
        if (setup != null) {
            setup.accept(this);
        }
    }

    public ArrayDeque<SoundEvent> getSoundEvents() {
        return soundEvents;
    }

    public ArrayDeque<SoundDefinitionInfo> getSoundDefinitionInfo() {
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

    public Item blockItem(@NotNull Block block, @NotNull BlockItem blockItem, @Nullable ItemEntry itemEntry) {
        return blockItem(null, block, blockItem, itemEntry);
    }

    public Item blockItem(@Nullable String name, @NotNull Block block, @NotNull BlockItem blockItem, @Nullable ItemEntry itemEntry) {
        if (name == null) {
            ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
            name = blockKey.getPath();
        }
        return item(name, blockItem, itemEntry);
    }

    public Item blockItem(@NotNull Block block, @Nullable ItemEntry itemEntry) {
        return blockItem(null, block, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(@NotNull String name, @NotNull Block block) {
        return this.blockItem(name, block, new BlockItem(block, new Item.Properties()), ItemEntry.from(block));
    }

    public Item blockItem(@NotNull String name, @NotNull Block block, @Nullable ItemEntry itemEntry) {
        return this.blockItem(name, block, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(@NotNull BlockItem blockItem) {
        Block block = blockItem.getBlock();
        return this.blockItem(null, block, blockItem, ItemEntry.from(block));
    }

    public Item blockItem(@NotNull BlockItem blockItem, @Nullable ItemEntry itemEntry) {
        Block block = blockItem.getBlock();
        return this.blockItem(null, block, blockItem, itemEntry);
    }

    public Block block(@NotNull String name, @NotNull Block block, @NotNull BlockEntry blockEntry) {
        BlockEntryAccessor accessor = (BlockEntryAccessor) block;
        if (blockEntry.isDefaulted())
            blockEntry = blockEntry.setDefault(block);
        accessor.vminus$setEntry(blockEntry);
        blocks.add(block);
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, name), block);
    }

    public Item item(@NotNull String name) {
        return item(name, new Item(new Item.Properties()), ItemEntry.defaults());
    }

    public Item item(@NotNull String name, @NotNull Item item) {
        return item(name, item, ItemEntry.defaults());
    }

    public Item item(@NotNull String name, @Nullable ItemEntry itemEntry) {
        return item(name, new Item(new Item.Properties()), itemEntry);
    }

    public Item item(@NotNull String name, @NotNull Item item, @Nullable ItemEntry itemEntry) {
        ItemEntryAccessor accessor = (ItemEntryAccessor) item;
        if (itemEntry != null && itemEntry.isDefaulted())
            itemEntry = itemEntry.setDefault(item);
        accessor.vminus$setEntry(itemEntry);
        items.add(item);
        if (item instanceof BlockItem blockItem)
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, name), item);
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

    public ArrayDeque<Block> getBlocks() {
        return blocks;
    }

    public ArrayDeque<EntityType<?>> getEntityTypes() {
        return entityTypes;
    }

    public ArrayDeque<Item> getItems() {
        return items;
    }

    public static final class DefaultRegistryEntry<E extends RegistryEntry<E, T>, T> {
        private final RegistryEntry<E, T> registryEntry;
        private final boolean overwrite;


        public DefaultRegistryEntry(RegistryEntry<E, T> registryEntry) {
           this(registryEntry, false);
        }


        public DefaultRegistryEntry(RegistryEntry<E, T> registryEntry, boolean overwrite) {
            this.registryEntry = registryEntry;
            this.overwrite = overwrite;
        }

        public RegistryEntry<E, T> getRegistryEntry() {
            return registryEntry;
        }

        public boolean isOverwrite() {
            return overwrite;
        }
    }

}
