package net.lixir.vminus.registry;

import net.lixir.vminus.block.*;
import net.lixir.vminus.datagen.BlockLootTable;
import net.lixir.vminus.datagen.BlockModel;
import net.lixir.vminus.datagen.ItemModel;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.group.RegistryEntryGroupsProvider;
import net.lixir.vminus.registry.entry.group.RegistryEntryGroup;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;

public class VMinusRegistryEntryGroupsProvider extends RegistryEntryGroupsProvider {
    public static final RegistryEntryGroup<Block> STAINED_GLASS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("translucent")
    );
    public static final RegistryEntryGroup<Block> BLOCK = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.CUBE_ALL)
                    .lootTable(BlockLootTable.SELF)
    );
    public static final RegistryEntryGroup<Block> FIRE = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
    );
    public static final RegistryEntryGroup<Block> MUSHROOM = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tintType(TintType.NONE)
                    .renderType("cutout")
                    .lootTable(BlockLootTable.SELF)
                    .itemEntry(ItemEntry.of().tint(TintType.NONE))
    );
    public static final RegistryEntryGroup<Block> GLASS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(Tags.Blocks.GLASS)
                    .model(BlockModel.CUBE_ALL)
                    .renderType("cutout")
    );
    public static final RegistryEntryGroup<Block> LOG = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(BlockTags.LOGS)
                    .model(BlockModel.AXIS)
                    .renderType("solid")
                    .itemEntry(ItemEntry.of().tags(ItemTags.LOGS))
    );
    public static final RegistryEntryGroup<Block> TORCH_WALL = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.WALL_TORCH)
                    .modelTextureSuffix("_wall")
                    .renderType("cutout")
    );
    public static final RegistryEntryGroup<Block> TORCH = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.TORCH)
                    .renderType("cutout")
    );
    public static final RegistryEntryGroup<Block> LANTERN = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.LANTERN)
                    .renderType("cutout")
    );
    public static final RegistryEntryGroup<Block> HANGING_ROOTS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
                    .model(BlockModel.CROSS)
                    .lootTable(BlockLootTable.SELF)
                    .tintType(TintType.NONE)
                    .itemEntry(ItemEntry.of().tint(TintType.NONE))
    );
    public static final RegistryEntryGroup<Block> PINK_PETALS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tintType(TintType.NONE)
                    .renderType("cutout")
                    .lootTable(BlockLootTable.PINK_PETALS)
                    .model(BlockModel.PINK_PETALS)
    );
    public static final RegistryEntryGroup<Block> HANGING_SIGN = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.NONE)
    );
    public static final RegistryEntryGroup<Block> WALL_HANGING_SIGN = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.NONE)
    );
    public static final RegistryEntryGroup<Block> WALL_SIGN = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.NONE)
    );
    public static final RegistryEntryGroup<Block> IRON_BARS_BLOCK = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.NONE)
    );
    public static final RegistryEntryGroup<Block> SIGN = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.NONE)
    );
    public static final RegistryEntryGroup<Block> WOODEN_DOOR = RegistryEntryGroup.create(
            BlockEntry.of()
                    .modelTextureSuffix("_planks")
    );
    public static final RegistryEntryGroup<Block> WOODEN_TRAPDOOR = RegistryEntryGroup.create(
            BlockEntry.of()
                    .modelTextureSuffix("_planks")
    );
    public static final RegistryEntryGroup<Block> TRAPDOOR = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.TRAPDOOR)
                    .itemEntry(ItemEntry.of().model(ItemModel.BASIC))
                    .renderType("solid")
    );
    public static final RegistryEntryGroup<Block> DOOR = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.DOOR)
                    .itemEntry(ItemEntry.of().model(ItemModel.BASIC))
                    .renderType("solid")
    );
    public static final RegistryEntryGroup<Block> DOUBLE_PLANT = RegistryEntryGroup.create(
            BlockEntry.of()
                    .lootTable(BlockLootTable.DOUBLE_PLANT_SHEARS)
                    .model(BlockModel.DOUBLE_CROSS)
    );
    public static final RegistryEntryGroup<Block> BUSH = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
                    .tintType(TintType.GRASS)
                    .model(BlockModel.CROSS)
                    .lootTable(BlockLootTable.SHEARS)
                    .itemEntry(ItemEntry.of().tint(TintType.GRASS))
    );
    public static final RegistryEntryGroup<Block> FLOWER = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
                    .tags(BlockTags.SMALL_FLOWERS)
                    .model(BlockModel.CROSS)
                    .tintType(TintType.NONE)
                    .lootTable(BlockLootTable.SELF)
                    .itemEntry(ItemEntry.of().tags(ItemTags.SMALL_FLOWERS).tint(TintType.NONE))
    );
    public static final RegistryEntryGroup<Block> ROOTS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
                    .tintType(TintType.NONE)
                    .lootTable(BlockLootTable.SELF)
                    .model(BlockModel.CROSS)
                    .itemEntry(ItemEntry.of().tint(TintType.NONE))
    );
    public static final RegistryEntryGroup<Block> AIR = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
                    .model(BlockModel.AIR)
    );
    public static final RegistryEntryGroup<Block> STAIRS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("solid")
                    .model(BlockModel.STAIRS)
                    .tags(BlockTags.STAIRS)
                    .itemEntry(ItemEntry.of().tags(ItemTags.STAIRS))
    );
    public static final RegistryEntryGroup<Block> WOODEN_STAIRS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("solid")
                    .model(BlockModel.STAIRS)
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_STAIRS)
                    .itemEntry(ItemEntry.of().tags(ItemTags.WOODEN_STAIRS))
    );
    public static final RegistryEntryGroup<Block> SLAB = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("solid")
                    .model(BlockModel.SLAB)
                    .tags(BlockTags.SLABS)
                    .itemEntry(ItemEntry.of().tags(ItemTags.SLABS))
    );
    public static final RegistryEntryGroup<Block> WOODEN_SLAB = RegistryEntryGroup.create(
            BlockEntry.of().
                    renderType("solid")
                    .model(BlockModel.SLAB)
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_SLABS)
                    .itemEntry(ItemEntry.of().tags(ItemTags.WOODEN_SLABS))
    );
    public static final RegistryEntryGroup<Block> WALL = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("solid")
                    .model(BlockModel.WALL)
                    .tags(BlockTags.WALLS)
                    .itemEntry(ItemEntry.of().tags(ItemTags.WALLS))
    );
    public static final RegistryEntryGroup<Block> WOOL_CARPET = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(BlockTags.WOOL_CARPETS)
                    .modelTextureSuffix("_wool")
                    .itemEntry(ItemEntry.of().tags(ItemTags.WOOL_CARPETS))
    );
    public static final RegistryEntryGroup<Block> CARPET = RegistryEntryGroup.create(
            BlockEntry.of()
                    .model(BlockModel.CARPET)
    );
    public static final RegistryEntryGroup<Block> WOODEN_BUTTON = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("solid")
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_BUTTONS)
                    .itemEntry(ItemEntry.of().tags(ItemTags.WOODEN_BUTTONS))
                    .model(BlockModel.BUTTON)
    );
    public static final RegistryEntryGroup<Block> WOODEN_PRESSURE_PLATE = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("solid")
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_PRESSURE_PLATES)
                    .itemEntry(ItemEntry.of().tags(ItemTags.WOODEN_PRESSURE_PLATES))
                    .model(BlockModel.PRESSURE_PLATE)
    );
    public static final RegistryEntryGroup<Item> ITEM = RegistryEntryGroup.create(
            ItemEntry.of()
                    .model(ItemModel.BASIC)
    );
    public static final RegistryEntryGroup<Block> WOODEN_FENCE = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(BlockTags.WOODEN_FENCES)
                    .modelTextureSuffix("_planks")
                    .itemEntry(ItemEntry.of().tags(ItemTags.WOODEN_FENCES))
    );
    public static final RegistryEntryGroup<Block> WOODEN_FENCE_GATE = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(VMinusTags.Blocks.WOODEN_FENCE_GATES)
                    .modelTextureSuffix("_planks")
                    .itemEntry(ItemEntry.of().tags(VMinusTags.Items.WOODEN_FENCE_GATES))
    );
    public static final RegistryEntryGroup<Block> FENCE_GATE = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(BlockTags.FENCE_GATES)
                    .model(BlockModel.FENCE_GATE)
                    .itemEntry(ItemEntry.of().tags(ItemTags.FENCE_GATES))
    );
    public static final RegistryEntryGroup<Block> FENCE = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(BlockTags.FENCES)
                    .model(BlockModel.FENCE)
                    .itemEntry(ItemEntry.of().tags(ItemTags.FENCES))
    );
    public static final RegistryEntryGroup<Block> LEAVES = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(BlockTags.LEAVES)
                    .tintType(TintType.FOLIAGE)
                    .lootTable(BlockLootTable.SHEARS)
                    .renderType("cutout_mipped")
                    .model(BlockModel.TINTED_CUBE_ALL)
                    .itemEntry(ItemEntry.of().tint(TintType.FOLIAGE).tags(ItemTags.LEAVES))
    );
    public static final RegistryEntryGroup<Block> PLANKS = RegistryEntryGroup.create(
            BlockEntry.of()
                    .tags(BlockTags.PLANKS)
                    .itemEntry(ItemEntry.of().tags(ItemTags.PLANKS))
                    .model(BlockModel.CUBE_ALL)
    );

    public static final RegistryEntryGroup<Block> ORE = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
                    .model(BlockModel.NONE)
    );

    public static final RegistryEntryGroup<Block> GRASS_BLOCK = RegistryEntryGroup.create(
            BlockEntry.of()
                    .renderType("cutout")
                    .model(BlockModel.NONE)
    );

    @Override
    public void run() {
        assign(WALL_HANGING_SIGN, WallHangingSignBlock.class);
        assign(HANGING_SIGN, CeilingHangingSignBlock.class);
        assign(WALL_SIGN, WallSignBlock.class);
        assign(SIGN, StandingSignBlock.class);
        assign(WOODEN_DOOR, WoodenDoorBlock.class);
        assign(WOODEN_TRAPDOOR, WoodenTrapdoorBlock.class);
        assign(TRAPDOOR, TrapDoorBlock.class);
        assign(DOOR, DoorBlock.class);
        assign(PINK_PETALS, PinkPetalsBlock.class);
        assign(BLOCK, Block.class);
        assign(WOODEN_FENCE_GATE, WoodenFenceGateBlock.class);
        assign(FENCE_GATE, FenceGateBlock.class);
        assign(FENCE, FenceBlock.class);
        assign(WOODEN_FENCE, WoodenFenceBlock.class);
        assign(GRASS_BLOCK, GrassBlock.class);
        assign(WOOL_CARPET, WoolCarpetBlock.class);
        assign(CARPET, CarpetBlock.class);
        assign(IRON_BARS_BLOCK, IronBarsBlock.class);
        assign(GLASS, AbstractGlassBlock.class);
        assign(STAINED_GLASS, StainedGlassBlock.class);
        assign(LOG, LogBlock.class);
        assign(MUSHROOM, MushroomBlock.class, FungusBlock.class);
        assign(FIRE, BaseFireBlock.class);
        assign(TORCH_WALL, WallTorchBlock.class);
        assign(TORCH, TorchBlock.class);
        assign(LANTERN, LanternBlock.class);
        assign(HANGING_ROOTS, HangingRootsBlock.class);
        assign(DOUBLE_PLANT, DoublePlantBlock.class);
        assign(BUSH, BushBlock.class);
        assign(FLOWER, FlowerBlock.class);
        assign(ROOTS, RootsBlock.class);
        assign(AIR, AirBlock.class);
        assign(STAIRS, StairBlock.class);
        assign(WOODEN_STAIRS, WoodenStairBlock.class);
        assign(SLAB, SlabBlock.class);
        assign(WOODEN_SLAB, WoodenSlabBlock.class);
        assign(WALL, WallBlock.class);
        assign(WOODEN_BUTTON, WoodenButtonBlock.class);
        assign(WOODEN_PRESSURE_PLATE, WoodenPressurePlateBlock.class);
        assign(ITEM, Item.class);
        assign(LEAVES, LeavesBlock.class);
        assign(PLANKS, PlanksBlock.class);
    }
}

