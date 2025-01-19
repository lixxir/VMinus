package net.lixir.vminus.registry.util;

import net.lixir.vminus.block.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class BlockSet {
    public static final ArrayList<BlockSet> BLOCK_SETS = new ArrayList<>();
    public static final ArrayList<String> usingMods = new ArrayList<>();
    private final ArrayList<RegistryObject<Block>> blocks = new ArrayList<>();

    private final String baseName;
    public WoodType woodType = null;
    private Block alternateBaseBlock = null;
    // Block RegistryObjects from the BlockSet.
    private RegistryObject<Block> baseBlock = null;
    private RegistryObject<Block> stairsBlock = null;
    private RegistryObject<Block> slabBlock = null;
    private RegistryObject<Block> wallBlock = null;
    private RegistryObject<Block> crackedBlock = null;
    private RegistryObject<Block> fenceBlock = null;
    private RegistryObject<Block> fenceGateBlock = null;
    private RegistryObject<Block> doorBlock = null;
    private RegistryObject<Block> trapDoorBlock = null;
    private RegistryObject<Block> pressurePlateBlock = null;
    private RegistryObject<Block> buttonBlock = null;
    private RegistryObject<Block> standingSignBlock = null;
    private RegistryObject<Block> wallSignBlock = null;
    private RegistryObject<Block> hangingSignBlock = null;
    private RegistryObject<Block> wallHangingSignBlock = null;
    private RegistryObject<Block> logBlock = null;
    private RegistryObject<Block> strippedLogBlock = null;
    private RegistryObject<Block> woodBlock = null;
    private RegistryObject<Block> strippedWoodBlock = null;
    private RegistryObject<Block> leavesBlock = null;
    //
    private String copyBlock = null;
    private DyeColor dyeColor = null;
    private String alternateBaseName = null;
    private ResourceLocation creativeTabItem = new ResourceLocation("minecraft:bricks");
    private ResourceLocation afterCreativeTabItem = null;
    private ResourceLocation signAfterCreativeTabItem = null;
    private ToolType toolType = ToolType.HAND;
    private ToolStrength toolStrength = ToolStrength.NONE;
    private RenderType renderType = RenderType.NORMAL;
    private float hardness = 1.5f;
    private float resistance = 6.0f;
    private int offset = 1;
    private SoundType soundType = SoundType.STONE;
    // Inclusions of the BlockSet
    private boolean includeWall = false;
    private boolean includeSlab = false;
    private boolean includeStairs = false;
    private boolean includeBaseBlock = true;
    private boolean includeCracked = false;
    private boolean includeFence = false;
    private boolean includeFenceGate = false;
    private boolean includePressurePlate = false;
    private boolean includeButton = false;
    private boolean includeTrapdoor = false;
    private boolean includeDoor = false;
    private boolean includeLog = false;
    private boolean includeSign = false;
    private boolean includeHangingSign = false;
    private boolean includeChiseled = false;
    private boolean includeLeaves = false;
    private boolean leavesColored = false;
    private boolean isWoodSet = false;
    private ResourceLocation leavesAfterCreativeTabItem;
    private String modId;

    public BlockSet(String baseName) {
        this.baseName = baseName;
    }

    public static String correctBaseName(String baseName) {
        if (baseName.endsWith("s")) {
            // accounts for words like 'glass' that have 2 S's
            if (baseName.length() > 1 && baseName.charAt(baseName.length() - 2) == 's') {
                return baseName;
            }
            return baseName.substring(0, baseName.length() - 1);
        }
        return baseName;
    }

    public static String getAlternateBaseName(String inputString) {
        if (inputString != null && !inputString.isEmpty()) {
            String fixedBaseName = inputString;
            if (fixedBaseName.contains(":")) {
                fixedBaseName = fixedBaseName.substring(fixedBaseName.indexOf(":") + 1);
            }
            return fixedBaseName;
        } else {
            return null;
        }
    }

    public static String getAlternateNamespace(String inputString) {
        if (inputString != null && !inputString.isEmpty()) {
            String fixedBaseName = inputString;
            if (fixedBaseName.contains(":")) {
                fixedBaseName = fixedBaseName.substring(0, fixedBaseName.indexOf(":"));
            }
            return fixedBaseName;
        } else {
            return null;
        }
    }

    public static String renderTypeToString(BlockSet.RenderType renderType) {
        String strRenderType = "solid";
        switch (renderType) {
            case GLASS -> strRenderType = "cutout_mipped";
            case NORMAL -> strRenderType = "solid";
            case STAINED_GLASS -> strRenderType = "translucent";
        }
        return strRenderType;
    }

    public BlockSet strength(float hardness, float resistance) {
        this.hardness = hardness;
        this.resistance = resistance;
        return this;
    }

    public BlockSet sound(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public BlockSet withWalls() {
        this.includeWall = true;
        return this;
    }

    public BlockSet inTabItem(ResourceLocation resourceLocation) {
        this.creativeTabItem = resourceLocation;
        return this;
    }

    public BlockSet inTabItem(TabType tabType) {
        ResourceLocation resourceLocation = null;
        switch (tabType) {
            case COLORED -> resourceLocation = new ResourceLocation("minecraft", "cyan_wool");
            case BUILDING -> resourceLocation = new ResourceLocation("minecraft", "bricks");
            case NATURAL -> resourceLocation = new ResourceLocation("minecraft", "grass_block");
            case FUNCTIONAL -> resourceLocation = new ResourceLocation("minecraft", "oak_sign");
        }
        if (resourceLocation != null)
            this.creativeTabItem = resourceLocation;
        return this;
    }

    public BlockSet setCopyBlock(String copyBlock) {
        this.copyBlock = copyBlock;
        return this;
    }

    public BlockSet withSlabs() {
        this.includeSlab = true;
        return this;
    }

    public BlockSet withStairs() {
        this.includeStairs = true;
        return this;
    }

    public BlockSet withStairsSlabWall() {
        this.includeStairs = true;
        this.includeWall = true;
        this.includeSlab = true;
        return this;
    }

    public BlockSet withStairsSlabWallNoBase() {
        this.includeStairs = true;
        this.includeWall = true;
        this.includeSlab = true;
        this.includeBaseBlock = false;
        return this;
    }

    public BlockSet withStairsSlab() {
        this.includeStairs = true;
        this.includeSlab = true;
        return this;
    }

    public BlockSet withStairsSlabNoBase() {
        this.includeStairs = true;
        this.includeSlab = true;
        this.includeBaseBlock = false;
        return this;
    }

    public BlockSet withoutBase() {
        this.includeBaseBlock = false;
        return this;
    }

    public BlockSet withCracked() {
        this.includeCracked = true;
        return this;
    }

    public BlockSet withFence() {
        this.includeFence = true;
        return this;
    }

    public BlockSet withFenceGate() {
        this.includeFenceGate = true;
        return this;
    }

    public BlockSet withPressurePlate() {
        this.includePressurePlate = true;
        return this;
    }

    public BlockSet withButton() {
        this.includeButton = true;
        return this;
    }

    public BlockSet withDoor() {
        this.includeDoor = true;
        return this;
    }

    public BlockSet withTrapDoor() {
        this.includeTrapdoor = true;
        return this;
    }

    public BlockSet withSign() {
        this.includeSign = true;
        return this;
    }

    public BlockSet setWoodSet() {
        this.isWoodSet = true;
        return this;
    }

    public BlockSet withHangingSign() {
        this.includeHangingSign = true;
        return this;
    }

    public BlockSet leavesNotColored() {
        this.leavesColored = false;
        return this;
    }

    public BlockSet woodSet() {
        this.includeBaseBlock = true;
        this.includeStairs = true;
        this.includeSlab = true;
        this.includeFence = true;
        this.includeFenceGate = true;
        this.includeDoor = true;
        this.includeTrapdoor = true;
        this.includePressurePlate = true;
        this.includeButton = true;
        this.includeSign = true;
        this.includeHangingSign = true;
        this.isWoodSet = true;
        this.includeLog = true;
        this.copyBlock = "minecraft:oak_planks";
        this.toolType = ToolType.AXE;
        this.includeLeaves = true;
        this.leavesColored = true;
        this.alternateBaseName = "detour:" + baseName + "_planks";
        this.creativeTabItem = new ResourceLocation("minecraft", "bricks");
        return this;
    }

    public boolean isWoodSet() {
        return this.isWoodSet;
    }

    public boolean hasWall() {
        return this.includeWall;
    }

    public boolean hasFence() {
        return this.includeFence;
    }

    public boolean hasFenceGate() {
        return this.includeFenceGate;
    }

    public boolean hasPressurePlate() {
        return this.includePressurePlate;
    }

    public boolean hasButton() {
        return this.includeButton;
    }

    public boolean hasDoor() {
        return this.includeDoor;
    }

    public boolean hasTrapdoor() {
        return this.includeTrapdoor;
    }

    public boolean areLeavesColored() {
        return this.leavesColored;
    }

    public boolean hasSign() {
        return this.includeSign;
    }

    public boolean hasLog() {
        return this.includeLog;
    }

    public boolean hasLeaves() {
        return this.includeLeaves;
    }

    public boolean hasHangingSign() {
        return this.includeHangingSign;
    }

    public boolean hasChiseled() {
        return this.includeChiseled;
    }

    public boolean hasSlab() {
        return this.includeSlab;
    }

    public boolean hasStairs() {
        return this.includeStairs;
    }

    public boolean hasCracked() {
        return this.includeCracked;
    }

    public boolean hasBase() {
        return this.includeBaseBlock;
    }

    public ArrayList<RegistryObject<Block>> getBlocks() {
        return this.blocks;
    }

    public int getOffset() {
        return this.offset;
    }

    public BlockSet setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public BlockSet setDye(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
        return this;
    }

    public Block getBaseBlock() {
        if (hasBase()) {
            return this.baseBlock.get();
        } else if (alternateBaseName != null && !alternateBaseName.isEmpty()) {
            return this.alternateBaseBlock;
        }
        return null;
    }

    public Block getStairsBlock() {
        if (hasStairs()) {
            return this.stairsBlock.get();
        }
        return null;
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    public Block getSlabBlock() {
        if (hasSlab()) {
            return this.slabBlock.get();
        }
        return null;
    }

    public Block getFenceBlock() {
        if (hasFence()) {
            return this.fenceBlock.get();
        }
        return null;
    }

    public Block getFenceGateBlock() {
        if (hasFenceGate()) {
            return this.fenceGateBlock.get();
        }
        return null;
    }

    public Block getPressurePlateBlock() {
        if (hasPressurePlate()) {
            return this.pressurePlateBlock.get();
        }
        return null;
    }

    public Block getButtonBlock() {
        if (hasButton()) {
            return this.buttonBlock.get();
        }
        return null;
    }

    public Block getWallBlock() {
        if (hasWall()) {
            return this.wallBlock.get();
        }
        return null;
    }

    public Block getDoorBlock() {
        if (hasDoor()) {
            return this.doorBlock.get();
        }
        return null;
    }

    public Block getTrapdoorBlock() {
        if (hasTrapdoor()) {
            return this.trapDoorBlock.get();
        }
        return null;
    }

    public Block getCrackedBlock() {
        if (hasCracked()) {
            return this.crackedBlock.get();
        }
        return null;
    }

    public Block getStandingSignBlock() {
        if (hasSign()) {
            return this.standingSignBlock.get();
        }
        return null;
    }

    public Block getWallSignBlock() {
        if (hasSign()) {
            return this.wallSignBlock.get();
        }
        return null;
    }

    public Block getHangingSignBlock() {
        if (hasHangingSign()) {
            return this.hangingSignBlock.get();
        }
        return null;
    }

    public Block getWallHangingSignBlock() {
        if (hasHangingSign()) {
            return this.wallHangingSignBlock.get();
        }
        return null;
    }

    public Block getLogBlock() {
        if (hasLog()) {
            return this.logBlock.get();
        }
        return null;
    }

    public Block getLeavesBlock() {
        if (hasLeaves()) {
            return this.leavesBlock.get();
        }
        return null;
    }

    public Block getStrippedLogBlock() {
        if (hasLog()) {
            return this.strippedLogBlock.get();
        }
        return null;
    }

    public Block getWoodBlock() {
        if (hasLog()) {
            return this.woodBlock.get();
        }
        return null;
    }

    public Block getStrippedWoodBlock() {
        if (hasLog()) {
            return this.strippedWoodBlock.get();
        }
        return null;
    }

    public ResourceLocation getTabItem() {
        return this.creativeTabItem;
    }

    public String getBaseName() {
        return this.baseName;
    }

    public String getAlternateBaseName() {
        if (alternateBaseName != null && !alternateBaseName.isEmpty()) {
            String fixedBaseName = alternateBaseName;
            if (fixedBaseName.contains(":")) {
                fixedBaseName = fixedBaseName.substring(fixedBaseName.indexOf(":"), fixedBaseName.length() - 1);
            }
            return fixedBaseName;
        } else {
            return null;
        }
    }

    public BlockSet setAlternateBaseName(String alternateBaseName) {
        this.alternateBaseName = alternateBaseName;
        return this;
    }

    public String getAlternateBaseNameRaw() {
        return this.alternateBaseName;
    }

    public ResourceLocation getSignAfterCreativeTabItem() {
        return this.signAfterCreativeTabItem;
    }

    public ResourceLocation getAfterCreativeTabItem() {
        return this.afterCreativeTabItem;
    }

    public ToolType getTooltype() {
        return this.toolType;
    }

    public BlockSet withToolType(ToolType toolType) {
        this.toolType = toolType;
        return this;
    }

    public ToolStrength getToolStrength() {
        return this.toolStrength;
    }

    public RenderType getRenderType() {
        return this.renderType;
    }

    public BlockSet setRenderType(RenderType renderType) {
        this.renderType = renderType;
        return this;
    }

    public BlockSet withToolStrength(ToolStrength toolStrength) {
        this.toolStrength = toolStrength;
        return this;
    }

    public BlockSet afterCreativeItem(ResourceLocation resourceLocation) {
        this.afterCreativeTabItem = resourceLocation;
        return this;
    }

    public BlockSet signAfterCreativeItem(ResourceLocation resourceLocation) {
        this.signAfterCreativeTabItem = resourceLocation;
        return this;
    }

    public BlockSet leavesAfterCreativeItem(ResourceLocation resourceLocation) {
        this.leavesAfterCreativeTabItem = resourceLocation;
        return this;
    }

    public String getAlternateNamespace() {
        if (alternateBaseName != null && !alternateBaseName.isEmpty()) {
            String fixedBaseName = alternateBaseName;
            if (fixedBaseName.contains(":")) {
                fixedBaseName = fixedBaseName.substring(0, fixedBaseName.indexOf(":"));
            }
            return fixedBaseName;
        } else {
            return null;
        }
    }

    public Block getCorrectBlock(RegistryObject<Block> baseBlock, Block block) {
        if (baseBlock != null) {
            baseBlock.get();
            return baseBlock.get();
        } else {
            return (block);
        }
    }

    public String getModId(){
        return this.modId;
    }

    public Block getCopyBlock(RegistryObject<Block> baseBlock, Block block) {
        Block correctedBlock = null;
        if (copyBlock != null && !copyBlock.isEmpty()) {
            ResourceLocation resourceLocation = new ResourceLocation(copyBlock);
            correctedBlock = ForgeRegistries.BLOCKS.getValue(resourceLocation);
        } else if (baseBlock != null) {
            correctedBlock = baseBlock.get();
        } else if (block != null) {
            correctedBlock = block;
        }
        return correctedBlock;
    }

    public Block getBlockWithRendering(BlockType blockType, BlockBehaviour.Properties properties) {
        return getBlockWithRendering(blockType, properties, null);
    }

    public Block getBlockWithRendering(BlockType blockType, BlockBehaviour.Properties properties, @Nullable Block block) {
        Block returnBlock = null;
        if (renderType == RenderType.GLASS) {
            switch (blockType) {
                case BASE -> returnBlock = new GlassBlock(properties);
                case STAIRS ->
                        returnBlock = new GlassStairsBlock(() -> getCopyBlock(baseBlock, block).defaultBlockState(), properties);
                case SLAB -> returnBlock = new GlassSlabBlock(properties);
            }
        } else if (renderType == RenderType.STAINED_GLASS) {
            switch (blockType) {
                case BASE -> returnBlock = new StainedGlassBlock(this.dyeColor, properties);
                case STAIRS -> returnBlock = new StainedGlassStairsBlock(this.dyeColor, properties);
                case SLAB -> returnBlock = new StainedGlassSlabBlock(this.dyeColor, properties);
            }
        } else {
            switch (blockType) {
                case BASE -> returnBlock = new Block(properties);
                case STAIRS ->
                        returnBlock = new StairBlock(() -> getCopyBlock(baseBlock, block).defaultBlockState(), properties);
                case SLAB -> returnBlock = new SlabBlock(properties);
                case WALL -> returnBlock = new WallBlock(properties);
                case FENCE -> returnBlock = new FenceBlock(properties);
                case FENCE_GATE -> returnBlock = new FenceGateBlock(properties, this.woodType);
                case PRESSURE_PLATE ->
                        returnBlock = new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, properties, BlockSetType.OAK);
                case BUTTON -> returnBlock = new ButtonBlock(properties, BlockSetType.OAK, 15, true);
                case DOOR -> returnBlock = new DoorBlock(properties, BlockSetType.OAK);
                case TRAPDOOR -> returnBlock = new TrapDoorBlock(properties, BlockSetType.OAK);
                case LOG -> returnBlock = new ModFlammableRotatedPillarBlock(properties);
            }
        }
        return returnBlock;
    }

    public BlockSet build(String modId) {
        this.modId = modId;
        Block block;
        RegistryObject<Block> baseBlock;

        final DeferredRegister<Item> itemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
        final DeferredRegister<Block> blockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);

        if (isWoodSet)
            this.woodType = WoodType.register(new WoodType(modId + ":" + baseName, BlockSetType.OAK));

        if (includeBaseBlock) {
            block = null;
            if (this.copyBlock != null && !this.copyBlock.isEmpty()) {
                Block copyBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(this.copyBlock));
                baseBlock = blockRegistry.register((isWoodSet ? baseName + "_planks" : baseName),
                        () -> new Block(BlockBehaviour.Properties.copy(copyBlock)));
            } else {
                baseBlock = blockRegistry.register((isWoodSet ? baseName + "_planks" : baseName),
                        () -> new Block(BlockBehaviour.Properties.of().strength(hardness, resistance).sound(soundType)));
            }
            itemRegistry.register((isWoodSet ? baseName + "_planks" : baseName), () -> new BlockItem(baseBlock.get(), new Item.Properties()));
            blocks.add(baseBlock);
            this.baseBlock = baseBlock;
        } else {
            baseBlock = null;
            if (alternateBaseName != null && !alternateBaseName.isEmpty()) {
                ResourceLocation resourceLocation = new ResourceLocation(alternateBaseName.split(":")[0], alternateBaseName.split(":")[1]);
                block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
                this.alternateBaseBlock = block;
            } else {
                block = null;
            }
        }

        if (baseBlock != null || block != null) {
            String fixedBaseBlockId = correctBaseName(baseName);
            Block copyBlock = getCopyBlock(baseBlock, block);

            if (includeCracked) {
                this.crackedBlock = blockRegistry.register("cracked_" + baseName,
                        () -> getBlockWithRendering(BlockType.BASE, BlockBehaviour.Properties.copy(copyBlock)));
                itemRegistry.register("cracked_" + baseName, () -> new BlockItem(crackedBlock.get(), new Item.Properties()));
                blocks.add(crackedBlock);
            }

            if (includeStairs) {
                this.stairsBlock = blockRegistry.register(fixedBaseBlockId + "_stairs",
                        () -> getBlockWithRendering(BlockType.STAIRS, BlockBehaviour.Properties.copy(copyBlock), block));
                itemRegistry.register(fixedBaseBlockId + "_stairs", () -> new BlockItem(stairsBlock.get(), new Item.Properties()));
                blocks.add(stairsBlock);
            }

            if (includeSlab) {
                this.slabBlock = blockRegistry.register(fixedBaseBlockId + "_slab",
                        () -> getBlockWithRendering(BlockType.SLAB, BlockBehaviour.Properties.copy(copyBlock)));
                itemRegistry.register(fixedBaseBlockId + "_slab", () -> new BlockItem(slabBlock.get(), new Item.Properties()));
                blocks.add(slabBlock);
            }

            if (includeWall) {
                this.wallBlock = blockRegistry.register(fixedBaseBlockId + "_wall",
                        () -> getBlockWithRendering(BlockType.WALL, BlockBehaviour.Properties.copy(copyBlock)));
                itemRegistry.register(fixedBaseBlockId + "_wall", () -> new BlockItem(wallBlock.get(), new Item.Properties()));
                blocks.add(wallBlock);
            }

            if (includeFence) {
                this.fenceBlock = blockRegistry.register(fixedBaseBlockId + "_fence",
                        () -> getBlockWithRendering(BlockType.FENCE, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_FENCE : copyBlock)));

                itemRegistry.register(fixedBaseBlockId + "_fence", () -> new BlockItem(fenceBlock.get(), new Item.Properties()));
                blocks.add(fenceBlock);
            }

            if (includeFenceGate) {
                this.fenceGateBlock = blockRegistry.register(fixedBaseBlockId + "_fence_gate",
                        () -> getBlockWithRendering(BlockType.FENCE_GATE, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_FENCE_GATE : copyBlock)));
                itemRegistry.register(fixedBaseBlockId + "_fence_gate", () -> new BlockItem(fenceGateBlock.get(), new Item.Properties()));
                blocks.add(fenceGateBlock);
            }

            if (includePressurePlate) {
                this.pressurePlateBlock = blockRegistry.register(fixedBaseBlockId + "_pressure_plate",
                        () -> getBlockWithRendering(BlockType.PRESSURE_PLATE, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_PRESSURE_PLATE : copyBlock)));
                itemRegistry.register(fixedBaseBlockId + "_pressure_plate", () -> new BlockItem(pressurePlateBlock.get(), new Item.Properties()));
                blocks.add(pressurePlateBlock);
            }

            if (includeButton) {
                this.buttonBlock = blockRegistry.register(fixedBaseBlockId + "_button",
                        () -> getBlockWithRendering(BlockType.BUTTON, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_BUTTON : copyBlock)));
                itemRegistry.register(fixedBaseBlockId + "_button", () -> new BlockItem(buttonBlock.get(), new Item.Properties()));
                blocks.add(buttonBlock);
            }

            if (includeDoor) {
                this.doorBlock = blockRegistry.register(fixedBaseBlockId + "_door",
                        () -> getBlockWithRendering(BlockType.DOOR, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_DOOR : copyBlock)));
                itemRegistry.register(fixedBaseBlockId + "_door", () -> new BlockItem(doorBlock.get(), new Item.Properties()));
                blocks.add(doorBlock);
            }

            if (includeTrapdoor) {
                this.trapDoorBlock = blockRegistry.register(fixedBaseBlockId + "_trapdoor",
                        () -> getBlockWithRendering(BlockType.TRAPDOOR, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_TRAPDOOR : copyBlock)));
                itemRegistry.register(fixedBaseBlockId + "_trapdoor", () -> new BlockItem(trapDoorBlock.get(), new Item.Properties()));
                blocks.add(trapDoorBlock);
            }

            if (isWoodSet) {
                if (includeSign) {
                    this.standingSignBlock = blockRegistry.register(fixedBaseBlockId + "_sign",
                            () -> new ModStandingSignBlock(BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_SIGN : copyBlock), this.woodType));
                    this.wallSignBlock = blockRegistry.register(fixedBaseBlockId + "_wall_sign",
                            () -> new ModWallSignBlock(BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_WALL_SIGN : copyBlock), this.woodType));

                    itemRegistry.register(fixedBaseBlockId + "_sign",
                            () -> new SignItem(new Item.Properties().stacksTo(16), standingSignBlock.get(), wallSignBlock.get()));

                    blocks.add(standingSignBlock);
                    blocks.add(wallSignBlock);
                }

                if (includeHangingSign) {
                    this.hangingSignBlock = blockRegistry.register(fixedBaseBlockId + "_hanging_sign",
                            () -> new ModHangingSignBlock(BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_HANGING_SIGN : copyBlock), this.woodType));
                    this.wallHangingSignBlock = blockRegistry.register(fixedBaseBlockId + "_hanging_wall_sign",
                            () -> new ModWallHangingSignBlock(BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_WALL_HANGING_SIGN : copyBlock), this.woodType));

                    itemRegistry.register(fixedBaseBlockId + "_hanging_sign",
                            () -> new SignItem(new Item.Properties().stacksTo(16), hangingSignBlock.get(), wallHangingSignBlock.get()));

                    blocks.add(hangingSignBlock);
                    blocks.add(wallHangingSignBlock);
                }

                if (includeLeaves) {
                    this.leavesBlock = blockRegistry.register(fixedBaseBlockId + "_leaves",
                            () -> new ModLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
                    itemRegistry.register(fixedBaseBlockId + "_leaves", () -> new BlockItem(leavesBlock.get(), new Item.Properties()));

                    blocks.add(leavesBlock);
                }

                if (includeLog) {
                    // Names
                    final String logId = fixedBaseBlockId + "_log";
                    final String strippedLogId = "stripped_" + logId;
                    final String woodId = fixedBaseBlockId + "_wood";
                    final String strippedWoodId = "stripped_" + woodId;

                    this.logBlock = blockRegistry.register(logId,
                            () -> getBlockWithRendering(BlockType.LOG, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_LOG : copyBlock)));
                    this.strippedLogBlock = blockRegistry.register(strippedLogId,
                            () -> getBlockWithRendering(BlockType.LOG, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.STRIPPED_OAK_LOG : copyBlock)));
                    this.woodBlock = blockRegistry.register(woodId,
                            () -> getBlockWithRendering(BlockType.LOG, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.OAK_WOOD : copyBlock)));
                    this.strippedWoodBlock = blockRegistry.register(strippedWoodId,
                            () -> getBlockWithRendering(BlockType.LOG, BlockBehaviour.Properties.copy(isWoodSet ? Blocks.STRIPPED_OAK_WOOD : copyBlock)));

                    itemRegistry.register(logId, () -> new BlockItem(logBlock.get(), new Item.Properties()));
                    itemRegistry.register(strippedLogId, () -> new BlockItem(strippedLogBlock.get(), new Item.Properties()));
                    itemRegistry.register(woodId, () -> new BlockItem(woodBlock.get(), new Item.Properties()));
                    itemRegistry.register(strippedWoodId, () -> new BlockItem(strippedWoodBlock.get(), new Item.Properties()));

                    blocks.add(logBlock);
                    blocks.add(strippedLogBlock);
                    blocks.add(woodBlock);
                    blocks.add(strippedWoodBlock);
                }
            }
        }

        usingMods.add(this.modId);
        BLOCK_SETS.add(this);
        return this;
    }

    public enum ToolType {
        PICKAXE,
        AXE,
        SHOVEL,
        HAND,
        HOE
    }

    public enum ToolStrength {
        NONE,
        WOODEN,
        STONE,
        IRON,
        DIAMOND,
        NETHERITE
    }

    public enum TabType {
        BUILDING,
        NATURAL,
        COLORED,
        FUNCTIONAL
    }

    public enum RenderType {
        NORMAL,
        GLASS,
        STAINED_GLASS
    }

    public enum BlockType {
        BASE,
        STAIRS,
        SLAB,
        WALL,
        FENCE,
        FENCE_GATE,
        PRESSURE_PLATE,
        DOOR,
        TRAPDOOR,
        SIGN,
        HANGING_SIGN,
        BUTTON,
        LOG
    }

}
