package net.lixir.vminus.registry;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
import net.minecraft.client.particle.ParticleEngine;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("deprecation")
public class UnifiedRegistry {
    private static final ConcurrentHashMap<String, UnifiedRegistry> REGISTRIES = new ConcurrentHashMap<>();
    private static final ArrayDeque<ParticleProviderRegistration<?>> PARTICLE_PROVIDER_REGISTRATIONS = new ArrayDeque<>();
    private final ArrayDeque<Block> blocks = new ArrayDeque<>();
    private final ArrayDeque<SoundEvent> soundEvents = new ArrayDeque<>();
    private final ArrayDeque<Item> items = new ArrayDeque<>();
    private final ArrayDeque<SoundDefinitionInfo> soundDefinitionInfo = new ArrayDeque<>();
    private final String modId;
    private Consumer<UnifiedRegistry> setup;

    private UnifiedRegistry(String modId) {
        this.modId = modId;
    }

    public static @NotNull List<UnifiedRegistry> getRegistries() {
        return REGISTRIES.values().stream().toList();
    }

    public void setSetup(Consumer<UnifiedRegistry> setup) {
        this.setup = setup;
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


    public static <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> type, ParticleEngine.SpriteParticleRegistration<T> provider) {
        PARTICLE_PROVIDER_REGISTRATIONS.add(new ParticleProviderRegistration<>(type, provider));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        for (ParticleProviderRegistration<?> reg : PARTICLE_PROVIDER_REGISTRATIONS) {
            register(reg, event);
        }
    }

    public void init() {
        if (setup != null) {
            setup.accept(this);
        }
    }


    @SuppressWarnings("unchecked")
    private static <T extends ParticleOptions> void register(ParticleProviderRegistration<?> reg, @NotNull RegisterParticleProvidersEvent event) {
        ParticleProviderRegistration<T> casted = (ParticleProviderRegistration<T>) reg;
        event.registerSpriteSet(casted.type, casted.provider);
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

    public Block block(String name, Block block) {
        return block(name, block, BlockEntry.defaults());
    }

    public Item blockItem(Block block) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String name = resourceLocation.getPath();
        return item(name, new BlockItem(block, new Item.Properties()), ItemEntry.from(block));
    }

    public Item blockItem(Block block, @Nullable ItemEntry itemEntry) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String name = resourceLocation.getPath();
        return item(name, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(String name, Block block) {
        return item(name, new BlockItem(block, new Item.Properties()), ItemEntry.from(block));
    }

    public Item blockItem(String name, Block block, @Nullable ItemEntry itemEntry) {
        return item(name, new BlockItem(block, new Item.Properties()), itemEntry);
    }

    public Item blockItem(BlockItem blockItem) {
        Block block = blockItem.getBlock();
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String name = resourceLocation.getPath();
        return this.item(name, blockItem, ItemEntry.from(block));
    }

    public Item blockItem(BlockItem blockItem, @Nullable ItemEntry itemEntry) {
        Block block = blockItem.getBlock();
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String name = resourceLocation.getPath();
        return this.item(name, blockItem, itemEntry);
    }

    public Block block(String name, Block block, @Nullable BlockEntry blockEntry) {
        if (block == null)
            throw new IllegalArgumentException("Trying to register null block for name: " + name);
        BlockEntryAccessor accessor = (BlockEntryAccessor) block;
        if (blockEntry != null && blockEntry.isDefaulted())
            blockEntry = blockEntry.setDefault(block);
        accessor.vminus$setEntry(blockEntry);
        blocks.add(block);
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, name), block);
    }

    public Item item(String name) {
        return item(name, new Item(new Item.Properties()), null);
    }

    public Item item(String name, Item item) {
        return item(name, item, null);
    }

    public Item item(String name, Item item, @Nullable ItemEntry itemEntry) {
        if (itemEntry == null)
            itemEntry = ItemEntry.of();
        ItemEntryAccessor accessor = (ItemEntryAccessor) item;
        accessor.vminus$setEntry(itemEntry);
        items.add(item);
        if (item instanceof BlockItem blockItem)
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, name), item);
    }

    public MobEffect effect(String name, MobEffect mobEffect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(modId, name), mobEffect);
    }

    public <T extends Entity> EntityType<T> entity(String name, EntityType.@NotNull Builder<T> entityTypeBuilder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), entityTypeBuilder.build(name));
    }

    public CreativeModeTab tab(String name, CreativeModeTab creativeModeTab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(modId, name), creativeModeTab);
    }

    public Feature<?> feature(String name, Feature<?> feature) {
        return Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.fromNamespaceAndPath(modId, name), feature);
    }

    public <T extends FoliagePlacer> FoliagePlacerType<T> foliagePlacer(String name, FoliagePlacerType<T> foliagePlacerType) {
        return Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), foliagePlacerType);
    }

    public <T extends TrunkPlacer> TrunkPlacerType<T> trunkPlacer(String name, TrunkPlacerType<T> trunkPlacerType) {
        return Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), trunkPlacerType);
    }

    public PaintingVariant painting(String name, int width, int height) {
        return painting(name, new PaintingVariant(width * 16, height * 16));
    }

    public PaintingVariant painting(String name, PaintingVariant paintingVariant) {
        return Registry.register(BuiltInRegistries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath(modId, name), paintingVariant);
    }

    public <T extends BlockEntity> BlockEntityType<T> blockEntity(String name, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), type);
    }

    public Item dye(String id, DyeColor dyeColor) {
        return item(id, new DyeItem(dyeColor, new Item.Properties()));
    }

    public <T extends BlockEntity> BlockEntityType<T> blockEntity(String name, BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), BlockEntityType.Builder.of(factory, blocks).build(null));
    }

    public SoundEvent sound(String name, String eventPath) {
        return sound(name, SoundDefinitionInfo.of(eventPath));
    }

    public SoundEvent sound(String name, SoundDefinitionInfo soundDefinitionInfo) {
        SoundEvent soundEvent = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(modId, name), SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
        this.soundDefinitionInfo.add(soundDefinitionInfo.setSoundEvent(soundEvent));
        return soundEvent;
    }

    public Holder.Reference<SoundEvent> soundHolder(String name, SoundDefinitionInfo soundDefinitionInfo) {
        SoundEvent soundEvent = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(modId, name), SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
        this.soundDefinitionInfo.add(soundDefinitionInfo.setSoundEvent(soundEvent));
        return BuiltInRegistries.SOUND_EVENT.getHolder(BuiltInRegistries.SOUND_EVENT.getResourceKey(soundEvent).orElseThrow()).orElseThrow();
    }

    public <T extends ParticleOptions> ParticleType<T> particle(String name, ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> registration) {
        ParticleType<T> particleType1 = Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), particleType);
        registerParticleProvider(particleType1, registration);
        return particleType1;
    }

    public <T extends TreeDecorator> TreeDecoratorType<T> treeDecorator(String name, TreeDecoratorType<T> treeDecoratorType) {
        return Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), treeDecoratorType);
    }

    public ArrayDeque<Block> getBlocks() {
        return blocks;
    }

    public ArrayDeque<Item> getItems() {
        return items;
    }

    private record ParticleProviderRegistration<T extends ParticleOptions>(ParticleType<T> type,
                                                                           ParticleEngine.SpriteParticleRegistration<T> provider) {
    }
}
