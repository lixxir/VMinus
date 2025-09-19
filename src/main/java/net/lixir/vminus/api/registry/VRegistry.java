package net.lixir.vminus.api.registry;

import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.api.datagen.sound.SoundDefinitionInfo;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.EntityDefinition;
import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.lixir.vminus.api.registry.definition.RegistryDefinition;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.lixir.vminus.api.registry.definition.duck.EntityDefinitionDuck;
import net.lixir.vminus.api.registry.definition.duck.ItemDefinitionDuck;
import net.lixir.vminus.api.registry.definition.group.DefinitionGroup;
import net.lixir.vminus.api.registry.definition.group.DefinitionGroupProvider;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
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
    @Nullable
    private DefinitionGroupProvider definitionGroupProvider;
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

    /**
     * Retrieves the most appropriate {@link RegistryDefinition} for a given object.
     * <p>
     * This method searches through all registered {@link DefinitionGroup}s and their
     * associated entries or suppliers to find the best match for the target object.
     * It calculates the class distance to pick the closest match in the type hierarchy
     * and merges metadata from compatible groups.
     *
     * @param targetObject the object for which a registry entry is being retrieved
     * @param <E>          the type of the {@link RegistryDefinition} being returned
     * @param <T>          the type of the object being looked up
     * @return the merged {@link RegistryDefinition} associated with the object, or {@code null} if none is found
     */
    @SuppressWarnings("unchecked")
    public static @Nullable <E extends RegistryDefinition<E, T>, T> RegistryDefinition<E, T> getDefaultDefinition(@NotNull T targetObject) {
        Class<?> targetClass = targetObject.getClass();
        VMinus.LOGGER.info("Getting default definition for object of class: {}", targetClass.getName());

        List<DefinitionGroup<?>> compatibleGroups = new ArrayList<>();
        DefinitionGroup<?> bestGroup = null;
        int bestDistance = Integer.MAX_VALUE;

        Map<DefinitionGroup<?>, Integer> groupDistances = new HashMap<>();

        // Check class-based groups
        for (Map.Entry<Class<?>, DefinitionGroup<?>> entry : DefinitionGroupProvider.getAssignedEntries().entrySet()) {
            Class<?> groupClass = entry.getKey();
            if (groupClass.isAssignableFrom(targetClass)) {
                DefinitionGroup<?> group = entry.getValue();
                compatibleGroups.add(group);

                int distance = getClassDistance(targetClass, groupClass);
                groupDistances.put(group, distance);

                VMinus.LOGGER.info("Compatible group found: {} (distance {})", groupClass.getName(), distance);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestGroup = group;
                    VMinus.LOGGER.info("New best group selected: {} with distance {}", groupClass.getName(), distance);
                }
            }
        }

        // Check suppliers for exact matches
        for (Map.Entry<Supplier<Object>, DefinitionGroup<?>> entry : DefinitionGroupProvider.getAssignedSuppliers().entrySet()) {
            Object supplied = entry.getKey().get();
            if (targetObject.equals(supplied)) {
                bestGroup = entry.getValue();
                VMinus.LOGGER.info("Exact supplier match found for object: {}", supplied);
                break;
            }
        }

        compatibleGroups.remove(bestGroup);

        if (bestGroup != null) {
            RegistryDefinition<E, T> bestEntry = (RegistryDefinition<E, T>) bestGroup.getDefinition();
            VMinus.LOGGER.info("Merging compatible groups into best entry: {}", bestGroup);

            compatibleGroups.sort(Comparator.comparingInt(g -> groupDistances.getOrDefault(g, Integer.MAX_VALUE)));
            for (DefinitionGroup<?> group : compatibleGroups) {
                RegistryDefinition<E, T> otherEntry = (RegistryDefinition<E, T>) group.getDefinition();
                VMinus.LOGGER.info("Merging group {} into best entry", group);
                bestEntry.merge((E) otherEntry);
            }

            VMinus.LOGGER.info("Final default definition obtained for class {}: {}", targetClass.getName(), bestEntry);
            return bestEntry;
        }

        VMinus.LOGGER.info("No default definition found for class {}", targetClass.getName());
        return null;
    }


    /**
     * Calculates the inheritance distance between a child class and a parent class.
     * <p>
     * Distance is defined as the number of steps in the class hierarchy from {@code child} to {@code parent}.
     * Returns {@link Integer#MAX_VALUE} if {@code parent} is not a superclass of {@code child}.
     *
     * @param child  the subclass
     * @param parent the superclass
     * @return the number of inheritance steps from child to parent, or {@link Integer#MAX_VALUE} if unrelated
     */
    private static int getClassDistance(Class<?> child, Class<?> parent) {
        int distance = 0;
        while (child != null && !child.equals(parent)) {
            child = child.getSuperclass();
            distance++;
        }
        return child == null ? Integer.MAX_VALUE : distance;
    }

    public static @Nullable ItemDefinition getDefaultItemDefinition(Item item) {
        RegistryDefinition<?, ?> entry = getDefaultDefinition(item);
        return (entry instanceof ItemDefinition) ? (ItemDefinition) entry : null;
    }

    public static @Nullable BlockDefinition getDefaultBlockDefinition(Block block) {
        RegistryDefinition<?, ?> entry = getDefaultDefinition(block);
        return (entry instanceof BlockDefinition) ? (BlockDefinition) entry : null;
    }

    public static @NotNull List<VRegistry> getRegistries() {
        return REGISTRIES.values().stream().toList();
    }

    public static VRegistry fromId(String id) {
        return REGISTRIES.get(id);
    }

    public static @NotNull VRegistry create(String modId, @Nullable DefinitionGroupProvider definitionGroupProvider) {
        VRegistry registry = new VRegistry(modId);
        registry.definitionGroupProvider = definitionGroupProvider;
        if (REGISTRIES.putIfAbsent(modId, registry) != null)
            throw new IllegalStateException("UnifiedRegistry already exists for mod ID: " + modId);
        return registry;
    }

    public void runDefinitionGroups() {
        if (definitionGroupProvider != null)
            definitionGroupProvider.run();
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
        return block(name, block, BlockDefinition.defaults());
    }

    public Item blockItem(@NotNull Block block) {
        return blockItem(null, block, new BlockItem(block, new Item.Properties()), ItemDefinition.of(block));
    }

    public Item blockItem(@NotNull Block block, @NotNull BlockItem blockItem) {
        return blockItem(null, block, blockItem, ItemDefinition.of(block));
    }

    public Item blockItem(@NotNull Block block, @NotNull BlockItem blockItem, @NotNull ItemDefinition itemEntry) {
        return blockItem(null, block, blockItem, itemEntry);
    }

    public Item blockItem(@Nullable String name, @NotNull Block block, @NotNull BlockItem blockItem, @NotNull ItemDefinition itemEntry) {
        if (name == null) {
            ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
            name = blockKey.getPath();
        }
        return item(name, blockItem, itemEntry.merge(ItemDefinition.of(block))); // Make sure to always get the entry from the block.
    }

    public Item blockItem(@NotNull Block block, @NotNull ItemDefinition itemEntry) {
        return blockItem(null, block, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(@NotNull String name, @NotNull Block block) {
        return this.blockItem(name, block, new BlockItem(block, new Item.Properties()), ItemDefinition.of(block));
    }

    public Item blockItem(@NotNull String name, @NotNull Block block, @NotNull ItemDefinition itemEntry) {
        return this.blockItem(name, block, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(@NotNull BlockItem blockItem) {
        Block block = blockItem.getBlock();
        return this.blockItem(null, block, blockItem, ItemDefinition.of(block));
    }

    public Item blockItem(@NotNull BlockItem blockItem, @NotNull ItemDefinition itemEntry) {
        Block block = blockItem.getBlock();
        return this.blockItem(null, block, blockItem, itemEntry);
    }

    // Main method
    public Block block(@NotNull String name, @NotNull Block block, @NotNull BlockDefinition blockDefinition) {
        BlockDefinitionDuck duck = BlockDefinitionDuck.of(block);
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(modId, name);
        Block registered = Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
        blocks.add(registered);
        duck.vMinus$setDefinition(blockDefinition);
        if (blockDefinition.isDefaulted()) {
            VMinus.LOGGER.info("block({}) is defaulted", block);
            blockDefinition.setDefault(registered);
        }
        return registered;
    }

    public Item item(@NotNull String name) {
        return item(name, new Item(new Item.Properties()), ItemDefinition.defaults());
    }

    public Item item(@NotNull String name, @NotNull Item item) {
        return item(name, item, ItemDefinition.defaults());
    }

    public Item item(@NotNull String name, @NotNull ItemDefinition itemEntry) {
        return item(name, new Item(new Item.Properties()), itemEntry);
    }

    // Main method
    public Item item(@NotNull String name, @NotNull Item item, @NotNull ItemDefinition itemDefinition) {
        ItemDefinitionDuck duck = ItemDefinitionDuck.of(item);
        Item registered = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, name), item);
        items.add(registered);
        duck.vMinus$setDefinition(itemDefinition);
        if (itemDefinition.isDefaulted())
            itemDefinition.setDefault(registered);

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

    public <T extends Entity> EntityType<T> entity(@NotNull String name, EntityType.@NotNull Builder<T> entityTypeBuilder, @Nullable EntityDefinition entityDefinition) {
        EntityType<T> entityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), entityTypeBuilder.build(name));
        EntityDefinitionDuck accessor = (EntityDefinitionDuck) entityType;
        if (entityDefinition == null)
            entityDefinition = EntityDefinition.of();
        accessor.vMinus$setDefinition(entityDefinition);
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
        return Registry.register(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(modId, name), new RangedAttribute("attribute." + modId + ".key." + name, defaultValue, minimumValue, maximumValue).setSyncable(true));
    }

    public PaintingVariant painting(@NotNull String name, @NotNull PaintingVariant paintingVariant) {
        return Registry.register(BuiltInRegistries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath(modId, name), paintingVariant);
    }


    public Item dye(@NotNull String id, @NotNull DyeColor dyeColor) {
        return item(id, new DyeItem(dyeColor, new Item.Properties()));
    }

    public <S extends RecipeSerializer<T>, T extends Recipe<?>> S recipeSerializer(String name, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(modId, name), serializer);
    }

    public <T extends Recipe<?>> RecipeType<T> recipeType(final String name) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE,  ResourceLocation.fromNamespaceAndPath(modId, name), new RecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }

    public <T extends BlockEntity> BlockEntityType<T> blockEntity(@NotNull String name, @NotNull BlockEntityType.BlockEntitySupplier<T> factory, Block @NotNull ... blocks) {
        if (blocks.length == 0) {
            VMinus.LOGGER.warn("Block entity type {} requires at least one valid block to be defined!",  name);
        }

        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), BlockEntityType.Builder.of(factory, blocks).build(type));
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

    @SafeVarargs
    public final List<Block> getBlocksOfClass(Class<? extends Block>... clazz) {
        return blocks.stream()
                .filter(block -> {
                    for (Class<? extends Block> c : clazz) {
                        if (c.isInstance(block))
                            return true;
                    }
                    return false;
                })
                .toList();
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

    public List<Pair<Item, ItemDefinition>> getItemEntryPairs() {
        return items.stream()
                .map(item -> new Pair<>(item, ItemDefinition.of(item)))
                .filter(entry -> !entry.getSecond().isEmpty())
                .toList();
    }
}
