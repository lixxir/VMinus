package net.lixir.vminus.registry.util;

import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.block.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class BlockSet {
    public static final List<BlockSet> BLOCK_SETS = new ArrayList<>();
    private final List<BlockItemRegistryPair> blockItemPairs = new ArrayList<>();

    private final String modId;
    private final Block baseBlock;
    private final RegistryObject<Block> baseBlockObject;
    private final String renderType;
    private final BlockSetCreativeOrder creativeOrder;
    private final DeferredRegister<Item> itemRegistry;
    private final DeferredRegister<Block> blockRegistry;
    private final String baseBlockName;
    private final String baseBlockPath;
    private final String baseBlockNamespace;
    private final BlockBehaviour.Properties properties;
    private ResourceLocation baseTexture;

    private BlockItemRegistryPair stairs = null;
    private BlockItemRegistryPair slab = null;
    private BlockItemRegistryPair wall = null;
    private BlockItemRegistryPair fence = null;
    private BlockItemRegistryPair fenceGate = null;
    private BlockItemRegistryPair pressurePlate = null;
    private BlockItemRegistryPair button = null;
    private BlockItemRegistryPair door = null;
    private BlockItemRegistryPair trapdoor = null;
    private BlockItemRegistryPair standingSign = null;
    private BlockItemRegistryPair wallSign = null;
    private BlockItemRegistryPair hangingSign = null;
    private BlockItemRegistryPair wallHangingSign = null;
    private BlockItemRegistryPair log = null;
    private BlockItemRegistryPair wood = null;
    private BlockItemRegistryPair strippedLog = null;
    private BlockItemRegistryPair strippedWood = null;
    private Pair<TagKey<Block>, TagKey<Item>> logsTag = null;

    private final ArrayList<TagKey<Block>> blockTags;

    private final WoodType woodType;
    private final BlockSetType blockSetType;

    private final boolean isWoodSet;
    private final boolean isStoneSet;
    private final boolean isNetherWoodSet;
    private final String blockId;

    private BlockSet(Builder builder) {
        this.itemRegistry = builder.itemRegistry;
        this.blockRegistry = builder.blockRegistry;
        this.modId = builder.modId;
        this.blockId = builder.blockId;
        Supplier<? extends Block> supplier = builder.supplier;
        this.baseBlock = builder.baseBlock;
        this.properties = builder.properties;
        this.blockTags = builder.blockTags;
        this.isStoneSet = builder.isStoneSet;
        this.isNetherWoodSet = builder.isNetherWoodSet;

        if (supplier != null) {
            baseBlockObject = blockRegistry.register(blockId, supplier);
        } else {
            this.baseBlockObject = builder.baseBlockObj;
        }

        this.creativeOrder = builder.creativeOrder;

        this.isWoodSet = builder.isWoodSet;
        this.baseBlockName = getCorrectBaseName();
        this.baseBlockPath = getCorrectBasePath();
        if (builder.isWoodSet) {
            this.blockSetType = BlockSetType.register(new BlockSetType(modId + ":" + baseBlockName));
            this.woodType = WoodType.register(new WoodType(modId + ":" + baseBlockName, blockSetType));
        } else {
            this.woodType = WoodType.OAK;
            this.blockSetType = BlockSetType.OAK;
        }
        if (baseBlock != null) {
            this.baseBlockNamespace = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.baseBlock)).getNamespace();
        } else {
            this.baseBlockNamespace = baseBlockObject.getId().getNamespace();
        }
        this.baseTexture = builder.texture;
        if (baseTexture == null) {
            if (baseBlock != null) {
                this.baseTexture = new ResourceLocation(baseBlockNamespace, "block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.baseBlock)).getPath());
            } else {
                this.baseTexture = new ResourceLocation(baseBlockNamespace, "block/" + baseBlockObject.getId().getPath());
            }
        }

        this.renderType = builder.renderType;

        registerBlockSet(builder);
        BLOCK_SETS.add(this);
    }


    private TagKey<Block> blockTag(String name) {
        return BlockTags.create(new ResourceLocation(modId, name));
    }

    private TagKey<Item> itemTag(String name) {
        return ItemTags.create(new ResourceLocation(modId, name));
    }

    public String getBaseBlockName() {
        return this.baseBlockName;
    }

    private String getCorrectBasePath() {
        if (blockId != null && !blockId.isEmpty() && baseBlockObject == null)
            return blockId;
        return this.baseBlock != null ? Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(this.baseBlock)).getPath()
                : this.baseBlockObject.getId().getPath();
    }

    private String getCorrectBaseName() {
        String baseName = getCorrectBasePath();
        if (baseName.endsWith("s")) {
            baseName = baseName.substring(0, baseName.length() - 1);
        }
        if (baseName.endsWith("_block")) {
            baseName = baseName.substring(0, baseName.indexOf("_block"));
        }
        if (baseName.endsWith("_plank")) {
            baseName = baseName.substring(0, baseName.indexOf("_plank"));
        }
        return baseName;
    }

    public List<BlockItemRegistryPair> getBlockItemPairs() {
        return this.blockItemPairs;
    }

    private void registerBlockSet(Builder builder) {
        BlockBehaviour.Properties properties = this.properties != null ? this.properties : BlockBehaviour.Properties.copy(baseBlock);

        if (builder.hasLogs) {
            if (isNetherWoodSet) {
                log = registerPair("_stem", () -> new StrippablePillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_STEM), strippedLog.blockObject()));
                wood = registerPair("_hyphae", () -> new StrippablePillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CRIMSON_STEM), strippedWood.blockObject()));
                strippedLog = registerPair("stripped_", "_stem", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_HYPHAE)));
                strippedWood = registerPair("stripped_", "_hyphae", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CRIMSON_HYPHAE)));
                logsTag = new Pair<>(blockTag(baseBlockName + "_stems"), itemTag(baseBlockName + "_stems"));
            } else {
                log = registerPair("_log", () -> new StrippableFlammablePillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG), strippedLog.blockObject()));
                wood = registerPair("_wood", () -> new StrippableFlammablePillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD), strippedWood.blockObject()));
                strippedLog = registerPair("stripped_", "_log", () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
                strippedWood = registerPair("stripped_", "_wood", () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
                logsTag = new Pair<>(blockTag(baseBlockName + "_logs"), itemTag(baseBlockName + "_logs"));
            }


        }
        // Make sure base block is after some things
        if (baseBlockObject != null) {
            RegistryObject<Item> itemRegistryObject = RegistryUtil.itemForBlock(baseBlockObject, itemRegistry);
            creativeOrder.addItemRegistry(itemRegistryObject);
            blockItemPairs.add(new BlockItemRegistryPair(baseBlockObject, itemRegistryObject));
        }

        if (builder.hasStairs) {
            stairs = registerPair("_stairs", () -> new StairBlock(getBaseBlock()::defaultBlockState, properties));
        }
        if (builder.hasSlab) {
            slab = registerPair("_slab", () -> new SlabBlock(properties));
        }
        if (builder.hasWall) {
            wall = registerPair("_wall", () -> new WallBlock(properties));
        }
        if (builder.hasFence) {
            fence = registerPair("_fence", () -> new FenceBlock(properties));
        }
        if (builder.hasFenceGate) {
            fenceGate = registerPair("_fence_gate", () -> new FenceGateBlock(properties, woodType));
        }
        if (builder.hasDoor) {
            door = registerPair("_door", () -> new DoorBlock(properties, blockSetType));
        }
        if (builder.hasTrapdoor) {
            trapdoor = registerPair("_trapdoor", () -> new TrapDoorBlock(properties, blockSetType));
        }
        if (builder.hasPressurePlate) {
            pressurePlate = registerPair("_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, properties, blockSetType));
        }
        if (builder.hasButton) {
            button = registerPair("_button", () -> new ButtonBlock(properties, blockSetType, 15, true));
        }
        if (builder.hasSign) {
            RegistryObject<Block> standingSign = registerBlock("_sign", () -> new ModStandingSignBlock(properties, woodType));
            RegistryObject<Block> wallSign = registerBlock("_wall_sign", () -> new ModWallSignBlock(properties, woodType));
            RegistryObject<Item> signItem = itemRegistry.register(baseBlockName + "_sign",
                    () -> new SignItem(new Item.Properties().stacksTo(16), standingSign.get(), wallSign.get()));

            BlockItemRegistryPair registryPair1 = new BlockItemRegistryPair(standingSign, signItem);
            BlockItemRegistryPair registryPair2 = new BlockItemRegistryPair(wallSign, signItem);

            this.standingSign = registryPair1;
            this.wallSign = registryPair2;

            blockItemPairs.add(registryPair1);
            blockItemPairs.add(registryPair2);
        }
        if (builder.hasHangingSign) {
            RegistryObject<Block> hangingSign = registerBlock("_hanging_sign", () -> new ModHangingSignBlock(properties, woodType));
            RegistryObject<Block> wallHangingSign = registerBlock("_wall_hanging_sign", () -> new ModWallHangingSignBlock(properties, woodType));
            RegistryObject<Item> hangingSignItem = itemRegistry.register(baseBlockName + "_hanging_sign",
                    () -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(), new Item.Properties().stacksTo(16)));

            BlockItemRegistryPair registryPair1 = new BlockItemRegistryPair(hangingSign, hangingSignItem);
            BlockItemRegistryPair registryPair2 = new BlockItemRegistryPair(wallHangingSign, hangingSignItem);

            this.hangingSign = registryPair1;
            this.wallHangingSign = registryPair2;

            blockItemPairs.add(registryPair1);
            blockItemPairs.add(registryPair2);
        }
    }

    public String getModId() {
        return modId;
    }


    private RegistryObject<Block> registerBlock(String suffix, Supplier<Block> blockSupplier) {
        return blockRegistry.register(baseBlockName + suffix, blockSupplier);
    }
    private BlockItemRegistryPair registerPair(String suffix, Supplier<Block> blockSupplier) {
        return registerPair("", suffix, blockSupplier);
    }

    private BlockItemRegistryPair registerPair(String prefix, String suffix, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = blockRegistry.register(prefix + baseBlockName + suffix, blockSupplier);
        RegistryObject<Item> item = itemRegistry.register(prefix + baseBlockName + suffix, () -> new BlockItem(block.get(), new Item.Properties()));

        if (creativeOrder != null && item != null) {
            creativeOrder.addItemRegistry(item);
        }
        BlockItemRegistryPair registryPair = new BlockItemRegistryPair(block, item);
        blockItemPairs.add(registryPair);
        return registryPair;
    }

    public BlockSetCreativeOrder getCreativeOrder() {
        return creativeOrder;
    }

    public Block getBaseBlock() {
        return this.baseBlockObject != null ? this.baseBlockObject.get() : this.baseBlock;
    }

    public RegistryObject<Block> getBaseBlockRegistryObject() {
        return baseBlockObject;
    }

    public BlockItemRegistryPair getWall() {
        return wall;
    }

    public BlockItemRegistryPair getSlab() {
        return slab;
    }

    public BlockItemRegistryPair getStairs() {
        return stairs;
    }

    public String getRenderType() {
        return renderType;
    }

    public String getBaseBlockNamespace() {
        return baseBlockNamespace;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    public boolean isWoodSet() {
        return isWoodSet;
    }

    public BlockItemRegistryPair getFence() {
        return fence;
    }

    public BlockItemRegistryPair getFenceGate() {
        return fenceGate;
    }

    public BlockItemRegistryPair getPressurePlate() {
        return pressurePlate;
    }

    public BlockItemRegistryPair getButton() {
        return button;
    }

    public BlockItemRegistryPair getDoor() {
        return door;
    }

    public BlockItemRegistryPair getTrapdoor() {
        return trapdoor;
    }

    public ResourceLocation getBaseTexture() {
        return baseTexture;
    }

    public BlockItemRegistryPair getSign() {
        return standingSign;
    }

    public BlockItemRegistryPair getWallSign() {
        return wallSign;
    }

    public BlockItemRegistryPair getHangingSign() {
        return hangingSign;
    }

    public BlockItemRegistryPair getWallHangingSign() {
        return wallHangingSign;
    }

    public String getBaseBlockPath() {
        return baseBlockPath;
    }

    public ArrayList<TagKey<Block>> getBlockTags() {
        return blockTags;
    }

    public BlockItemRegistryPair getLog() {
        return log;
    }

    public BlockItemRegistryPair getWood() {
        return wood;
    }

    public BlockItemRegistryPair getStrippedLog() {
        return strippedLog;
    }

    public BlockItemRegistryPair getStrippedWood() {
        return strippedWood;
    }

    public Pair<TagKey<Block>, TagKey<Item>> getLogsTag() {
        return logsTag;
    }

    public boolean isStoneSet() {
        return isStoneSet;
    }

    public boolean isNetherWoodSet() {
        return isNetherWoodSet;
    }

    public static class Builder {
        private String baseBlockNamespaceId = null;
        private RegistryObject<Block> baseBlockObj;
        private Block baseBlock = null;
        private final DeferredRegister<Block> blockRegistry;
        private final DeferredRegister<Item> itemRegistry;
        private final String modId;

        private BlockSetCreativeOrder creativeOrder = null;
        private String renderType = "solid";
        private boolean hasStairs = false;
        private boolean hasLogs = false;
        private boolean hasSlab = false;
        private boolean hasWall = false;
        private boolean hasFence = false;
        private boolean hasFenceGate = false;
        private boolean hasPressurePlate = false;
        private boolean hasButton = false;
        private boolean hasTrapdoor = false;
        private boolean hasDoor = false;
        private boolean hasSign = false;
        private boolean hasHangingSign = false;
        private boolean isWoodSet = false;
        private boolean isStoneSet = false;
        private ResourceLocation texture = null;
        private Supplier<? extends Block> supplier = null;
        private BlockBehaviour.Properties properties = null;
        private String blockId = "";
        private final ArrayList<TagKey<Block>> blockTags = new ArrayList<>();
        private boolean isNetherWoodSet = false;

        public Builder(String modId, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
            this.baseBlock = null;
            this.baseBlockObj = null;
            this.blockRegistry = blockRegistry;
            this.itemRegistry = itemRegistry;
            this.modId = modId;
        }

        public Builder(String modId, String baseBlockNamespaceId, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
            this.baseBlockNamespaceId = baseBlockNamespaceId;
            this.baseBlock = null;
            this.baseBlockObj = null;
            this.blockRegistry = blockRegistry;
            this.itemRegistry = itemRegistry;
            this.modId = modId;
        }

        public Builder(String modId, Block baseBlock, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
            this.baseBlock = baseBlock;
            this.baseBlockObj = null;
            this.blockRegistry = blockRegistry;
            this.itemRegistry = itemRegistry;
            this.modId = modId;
        }

        public Builder(String modId, RegistryObject<Block> baseBlockObj, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
            this.baseBlockObj = baseBlockObj;
            this.blockRegistry = blockRegistry;
            this.itemRegistry = itemRegistry;
            this.modId = modId;
        }

        public Builder(String modId, String blockId, BlockBehaviour.Properties properties, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
            this.blockId = blockId;
            this.supplier = () -> new Block(properties);
            this.properties = properties;
            this.baseBlockObj = null;
            this.blockRegistry = blockRegistry;
            this.itemRegistry = itemRegistry;
            this.modId = modId;
        }

        public Builder stairs() {
            this.hasStairs = true;
            return this;
        }

        public Builder door() {
            this.hasDoor = true;
            return this;
        }

        public Builder sign() {
            this.hasSign = true;
            return this;
        }

        public Builder name(String name) {
            this.blockId = name;
            return this;
        }

        public Builder logs() {
            this.hasLogs = true;
            return this;
        }

        public Builder hangingSign() {
            this.hasHangingSign = true;
            return this;
        }

        public Builder stoneSet() {
            this.hasStairs = true;
            this.hasSlab = true;
            this.hasWall = true;
            this.hasPressurePlate = true;
            this.hasButton = true;
            this.isStoneSet = true;
            return this;
        }

        public Builder netherWoodSet() {
            this.hasStairs = true;
            this.hasSlab = true;
            this.hasFence = true;
            this.hasFenceGate = true;
            this.hasPressurePlate = true;
            this.hasButton = true;
            this.hasDoor = true;
            this.hasTrapdoor = true;
            this.hasSign = true;
            this.hasLogs = true;
            this.hasHangingSign = true;
            this.isNetherWoodSet = true;
            this.isWoodSet = true;
            return this;
        }

        public Builder woodSet() {
            this.hasStairs = true;
            this.hasSlab = true;
            this.hasFence = true;
            this.hasFenceGate = true;
            this.hasPressurePlate = true;
            this.hasButton = true;
            this.hasDoor = true;
            this.hasTrapdoor = true;
            this.hasSign = true;
            this.hasLogs = true;
            this.hasHangingSign = true;
            this.isWoodSet = true;
            return this;
        }

        public Builder fence() {
            this.hasFence = true;
            return this;
        }

        public Builder fenceGate() {
            this.hasFenceGate = true;
            return this;
        }

        public Builder button() {
            this.hasButton = true;
            return this;
        }

        public Builder pressurePlate() {
            this.hasPressurePlate = true;
            return this;
        }

        public Builder trapdoor() {
            this.hasTrapdoor = true;
            return this;
        }

        public Builder slab() {
            this.hasSlab = true;
            return this;
        }

        public Builder wall() {
            this.hasWall = true;
            return this;
        }

        public Builder tag(TagKey<Block> blockTag) {
            blockTags.add(blockTag);
            return this;
        }

        public Builder renderType(String renderType) {
            this.renderType = renderType;
            return this;
        }

        public Builder creativeTab(Item targetItem, CreativeModeTab tab) {
            return creativeTab(targetItem, tab, false);
        }

        public Builder creativeTab(BlockSetCreativeOrder blockSetCreativeOrder) {
            this.creativeOrder = blockSetCreativeOrder;
            return this;
        }

        public Builder creativeTab(CreativeModeTab tab) {
            if (baseBlock != null)
                return creativeTab(baseBlock.asItem(), tab, false);
            return this;
        }

        public Builder creativeTab(CreativeModeTab tab, Boolean before) {
            if (baseBlock != null)
                return creativeTab(baseBlock.asItem(), tab, before);
            return this;
        }

        public Builder creativeTab(Item targetItem, CreativeModeTab tab, Boolean before) {
            this.creativeOrder = new BlockSetCreativeOrder(tab, targetItem, before);
            return this;
        }

        public Builder texture(ResourceLocation resourceLocation) {
            this.texture = resourceLocation;
            return this;
        }

        public Block getBaseBlock() {
            return this.baseBlock;
        }

        public @Nullable String getBlockId() {
            return this.blockId;
        }

        public Builder baseBlock(Block block) {
            this.baseBlock = block;
            return this;
        }

        public Builder registryBlock(String blockId, BlockBehaviour.Properties properties) {
            this.blockId = blockId;
            this.supplier = () -> new Block(properties);
            return this;
        }

        public Builder properties(BlockBehaviour.Properties properties) {
            this.properties = properties;
            this.supplier = () -> new Block(properties);
            return this;
        }

        public Builder baseBlock(RegistryObject<Block> block) {
            this.baseBlockObj = block;
            return this;
        }

        public BlockSet build() {
            return new BlockSet(this);
        }

        public @Nullable String getBaseBlockNamespaceId() {
            return baseBlockNamespaceId;
        }
    }
}
