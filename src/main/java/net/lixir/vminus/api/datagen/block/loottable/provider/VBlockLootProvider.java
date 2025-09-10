package net.lixir.vminus.api.datagen.block.loottable.provider;

import net.lixir.vminus.api.datagen.block.BlockData;
import net.lixir.vminus.api.datagen.block.loottable.BlockLootTableType;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

/**
 * Base class for generating block loot tables in VMinus.
 * <p>
 * Automatically iterates over all blocks in the {@link VRegistry} for the given mod ID
 * and applies {@link BlockLootTableType} behavior defined in each block's {@link BlockDefinition}.
 * Provides utility methods for common loot behaviors (e.g., shears, double plants, petals),
 * but the main customization comes from the block definitions themselves.
 */
public abstract class VBlockLootProvider extends BlockLootSubProvider {
    // Exposed modId instead of private like in BlockLootSubProvider
    protected final String modId;

    public VBlockLootProvider(String modId) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    @Override
    protected void generate() {
        var blocks = VRegistry.fromId(modId).getBlocks();
        for (Block block : blocks) {
            if (block.getLootTable().equals(BuiltInLootTables.EMPTY))
                continue;
            BlockDefinition blockDefinition = BlockDefinition.of(block);
            BlockLootTableType tableType = blockDefinition.getLootTableType();
            if (tableType.isEmpty())
                continue;
            tableType.apply(BlockData.of(block), this);
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return VRegistry.fromId(modId).getBlocks().stream()
                .filter(block -> {
                    if (block.getLootTable().equals(BuiltInLootTables.EMPTY))
                        return false;
                    BlockDefinition blockDefinition = BlockDefinition.of(block);
                    BlockLootTableType lootTable = blockDefinition.getLootTableType();
                    return !lootTable.isEmpty();
                })
                .toList();
    }


    public void doubleFlower(DoublePlantBlock block) {
        EnumProperty<DoubleBlockHalf> half = DoublePlantBlock.HALF;

        this.add(block, LootTable.lootTable().withPool(
                this.applyExplosionDecay(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(half, DoubleBlockHalf.LOWER)))
                        )
                )
        ));
    }

    public void doublePlantShears(Block block) {
        this.add(block, createDoublePlantShearsDrop(block));
    }

    public void shears(Block block) {
        this.add(block, createShearsOnlyDrop(block));
    }

    public void self(Block block) {
        dropSelf(block);
    }
    public void pinkPetalsShears(PinkPetalsBlock block) {
        IntegerProperty amount = PinkPetalsBlock.AMOUNT;

        this.add(block, LootTable.lootTable().withPool(
                this.applyExplosionDecay(block,
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .when(MatchTool.toolMatches(
                                        ItemPredicate.Builder.item()
                                                .of(Items.SHEARS)
                                ))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 1))))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 2))))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 3))))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 4))))
                )
        ));
    }

    public void pinkPetals(PinkPetalsBlock block) {
        IntegerProperty amount = PinkPetalsBlock.AMOUNT;

        this.add(block, LootTable.lootTable().withPool(
                this.applyExplosionDecay(block,
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 1))))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 2))))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 3))))
                                .add(LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(amount, 4))))
                )
        ));
    }


    public <T> Optional<T> as(@NotNull Class<T> clazz) {
        return clazz.isInstance(this) ? Optional.of(clazz.cast(this)) : Optional.empty();
    }
}
