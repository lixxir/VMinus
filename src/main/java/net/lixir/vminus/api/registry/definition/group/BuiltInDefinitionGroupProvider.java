package net.lixir.vminus.api.registry.definition.group;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.api.block.wooden.*;

import net.lixir.vminus.api.datagen.block.loottable.BuiltInBlockLootTableTypes;
import net.lixir.vminus.api.datagen.block.model.BuiltInBlockModelTypes;
import net.lixir.vminus.api.datagen.item.model.BuiltInItemModelTypes;
import net.lixir.vminus.api.rendertype.RenderTypeKey;
import net.lixir.vminus.api.tint.BuiltInTintTypes;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;

/**
 * Default definition groups provided by VMinus.
 * <p>
 * Groups can be replaced with the
 * {@link DefinitionGroup#replace(net.lixir.vminus.api.registry.definition.RegistryDefinition) replace}
 * method during {@link #run()}.
 * </p>
 */
public class BuiltInDefinitionGroupProvider extends DefinitionGroupProvider {
    public static final DefinitionGroup<Block> STAINED_GLASS = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.TRANSLUCENT)
    );
    public static final DefinitionGroup<Block> BLOCK = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.CUBE_ALL)
                    .lootTableType(BuiltInBlockLootTableTypes.SELF)
    );
    public static final DefinitionGroup<Block> FIRE = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
    );
    public static final DefinitionGroup<Block> MUSHROOM = DefinitionGroup.of(
            BlockDefinition.of()
                    .tintType(BuiltInTintTypes.NONE)
                    .renderType(RenderTypeKey.CUTOUT)
                    .lootTableType(BuiltInBlockLootTableTypes.SELF)
                    .itemDefinition(ItemDefinition.of().tint(BuiltInTintTypes.NONE))
    );
    public static final DefinitionGroup<Block> GLASS = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(Tags.Blocks.GLASS)
                    .modelType(BuiltInBlockModelTypes.CUBE_ALL)
                    .renderType(RenderTypeKey.CUTOUT)
    );
    public static final DefinitionGroup<Block> LOG = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.LOGS)
                    .modelType(BuiltInBlockModelTypes.AXIS)
                    .renderType(RenderTypeKey.SOLID)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.LOGS))
    );
    public static final DefinitionGroup<Block> TORCH_WALL = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.WALL_TORCH)
                    .modelTextureSuffix("_wall")
                    .renderType(RenderTypeKey.CUTOUT)
    );
    public static final DefinitionGroup<Block> TORCH = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.TORCH)
                    .renderType(RenderTypeKey.CUTOUT)
    );
    public static final DefinitionGroup<Block> LANTERN = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.LANTERN)
                    .renderType(RenderTypeKey.CUTOUT)
    );
    public static final DefinitionGroup<Block> HANGING_ROOTS = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
                    .modelType(BuiltInBlockModelTypes.CROSS)
                    .lootTableType(BuiltInBlockLootTableTypes.SELF)
                    .tintType(BuiltInTintTypes.NONE)
                    .itemDefinition(ItemDefinition.of().tint(BuiltInTintTypes.NONE))
    );
    public static final DefinitionGroup<Block> PINK_PETALS = DefinitionGroup.of(
            BlockDefinition.of()
                    .tintType(BuiltInTintTypes.NONE)
                    .renderType(RenderTypeKey.CUTOUT)
                    .lootTableType(BuiltInBlockLootTableTypes.PINK_PETALS)
                    .modelType(BuiltInBlockModelTypes.PINK_PETALS)
    );
    public static final DefinitionGroup<Block> HANGING_SIGN = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.NONE)
    );
    public static final DefinitionGroup<Block> WALL_HANGING_SIGN = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.NONE)
    );
    public static final DefinitionGroup<Block> WALL_SIGN = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.NONE)
    );
    public static final DefinitionGroup<Block> IRON_BARS_BLOCK = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.NONE)
    );
    public static final DefinitionGroup<Block> SIGN = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.NONE)
    );
    public static final DefinitionGroup<Block> WOODEN_DOOR = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.WOODEN_DOORS)
                    .modelTextureSuffix("_planks")
    );
    public static final DefinitionGroup<Block> WOODEN_TRAPDOOR = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.WOODEN_TRAPDOORS)
                    .modelTextureSuffix("_planks")
    );
    public static final DefinitionGroup<Block> TRAPDOOR = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.TRAPDOOR)
                    .tags(BlockTags.TRAPDOORS)
                    .itemDefinition(ItemDefinition.of().modelType(BuiltInItemModelTypes.BASIC))
                    .renderType(RenderTypeKey.SOLID)
    );
    public static final DefinitionGroup<Block> DOOR = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.DOOR)
                    .tags(BlockTags.DOORS)
                    .itemDefinition(ItemDefinition.of().modelType(BuiltInItemModelTypes.BASIC))
                    .renderType(RenderTypeKey.SOLID)
    );
    public static final DefinitionGroup<Block> DOUBLE_PLANT = DefinitionGroup.of(
            BlockDefinition.of()
                    .lootTableType(BuiltInBlockLootTableTypes.DOUBLE_PLANT_SHEARS)
                    .modelType(BuiltInBlockModelTypes.DOUBLE_CROSS)
    );
    public static final DefinitionGroup<Block> BUSH = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
                    .tintType(BuiltInTintTypes.GRASS)
                    .modelType(BuiltInBlockModelTypes.CROSS)
                    .lootTableType(BuiltInBlockLootTableTypes.SHEARS)
                    .itemDefinition(ItemDefinition.of().tint(BuiltInTintTypes.GRASS))
    );
    public static final DefinitionGroup<Block> FLOWER = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
                    .tags(BlockTags.SMALL_FLOWERS)
                    .modelType(BuiltInBlockModelTypes.CROSS)
                    .tintType(BuiltInTintTypes.NONE)
                    .lootTableType(BuiltInBlockLootTableTypes.SELF)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.SMALL_FLOWERS).tint(BuiltInTintTypes.NONE))
    );
    public static final DefinitionGroup<Block> ROOTS = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
                    .tintType(BuiltInTintTypes.NONE)
                    .lootTableType(BuiltInBlockLootTableTypes.SELF)
                    .modelType(BuiltInBlockModelTypes.CROSS)
                    .itemDefinition(ItemDefinition.of().tint(BuiltInTintTypes.NONE))
    );
    public static final DefinitionGroup<Block> AIR = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
                    .modelType(BuiltInBlockModelTypes.AIR)
    );
    public static final DefinitionGroup<Block> STAIRS = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.SOLID)
                    .modelType(BuiltInBlockModelTypes.STAIRS)
                    .tags(BlockTags.STAIRS)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.STAIRS))
    );
    public static final DefinitionGroup<Block> WOODEN_STAIRS = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.SOLID)
                    .modelType(BuiltInBlockModelTypes.STAIRS)
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_STAIRS)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.WOODEN_STAIRS))
    );
    public static final DefinitionGroup<Block> SLAB = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.SOLID)
                    .modelType(BuiltInBlockModelTypes.SLAB)
                    .tags(BlockTags.SLABS)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.SLABS))
    );
    public static final DefinitionGroup<Block> WOODEN_SLAB = DefinitionGroup.of(
            BlockDefinition.of().
                    renderType(RenderTypeKey.SOLID)
                    .modelType(BuiltInBlockModelTypes.SLAB)
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_SLABS)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.WOODEN_SLABS))
    );
    public static final DefinitionGroup<Block> WALL = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.SOLID)
                    .modelType(BuiltInBlockModelTypes.WALL)
                    .tags(BlockTags.WALLS)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.WALLS))
    );
    public static final DefinitionGroup<Block> WOOL_CARPET = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.WOOL_CARPETS)
                    .modelTextureSuffix("_wool")
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.WOOL_CARPETS))
    );
    public static final DefinitionGroup<Block> CARPET = DefinitionGroup.of(
            BlockDefinition.of()
                    .modelType(BuiltInBlockModelTypes.CARPET)
    );
    public static final DefinitionGroup<Block> WOODEN_BUTTON = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.SOLID)
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_BUTTONS)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.WOODEN_BUTTONS))
                    .modelType(BuiltInBlockModelTypes.BUTTON)
    );
    public static final DefinitionGroup<Block> WOODEN_PRESSURE_PLATE = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.SOLID)
                    .modelTextureSuffix("_planks")
                    .tags(BlockTags.WOODEN_PRESSURE_PLATES)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.WOODEN_PRESSURE_PLATES))
                    .modelType(BuiltInBlockModelTypes.PRESSURE_PLATE)
    );
    public static final DefinitionGroup<Item> ITEM = DefinitionGroup.of(
            ItemDefinition.of()
                    .modelType(BuiltInItemModelTypes.BASIC)
    );
    public static final DefinitionGroup<Block> WOODEN_FENCE = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.WOODEN_FENCES)
                    .modelTextureSuffix("_planks")
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.WOODEN_FENCES))
    );
    public static final DefinitionGroup<Block> WOODEN_FENCE_GATE = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(VMinusTags.Blocks.WOODEN_FENCE_GATES)
                    .modelTextureSuffix("_planks")
                    .itemDefinition(ItemDefinition.of().tags(VMinusTags.Items.WOODEN_FENCE_GATES))
    );
    public static final DefinitionGroup<Block> FENCE_GATE = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.FENCE_GATES)
                    .modelType(BuiltInBlockModelTypes.FENCE_GATE)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.FENCE_GATES))
    );
    public static final DefinitionGroup<Block> FENCE = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.FENCES)
                    .modelType(BuiltInBlockModelTypes.FENCE)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.FENCES))
    );
    public static final DefinitionGroup<Block> LEAVES = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.LEAVES)
                    .tintType(BuiltInTintTypes.FOLIAGE)
                    .lootTableType(BuiltInBlockLootTableTypes.SHEARS)
                    .renderType(RenderTypeKey.CUTOUT_MIPPED)
                    .modelType(BuiltInBlockModelTypes.LEAVES)
                    .itemDefinition(ItemDefinition.of().tint(BuiltInTintTypes.FOLIAGE).tags(ItemTags.LEAVES))
    );
    public static final DefinitionGroup<Block> PLANKS = DefinitionGroup.of(
            BlockDefinition.of()
                    .tags(BlockTags.PLANKS)
                    .itemDefinition(ItemDefinition.of().tags(ItemTags.PLANKS))
                    .modelType(BuiltInBlockModelTypes.CUBE_ALL)
    );

    public static final DefinitionGroup<Block> ORE = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
                    .modelType(BuiltInBlockModelTypes.NONE)
    );

    public static final DefinitionGroup<Block> GRASS_BLOCK = DefinitionGroup.of(
            BlockDefinition.of()
                    .renderType(RenderTypeKey.CUTOUT)
                    .modelType(BuiltInBlockModelTypes.NONE)
    );

    @Override
    public void run() {
        VMinus.LOGGER.info("IM RUNNING BUILT IN DEFINITION GROUP PROCVIEDER");
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

