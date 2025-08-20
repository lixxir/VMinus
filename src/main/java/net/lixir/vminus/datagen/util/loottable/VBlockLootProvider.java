package net.lixir.vminus.datagen.util.loottable;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.datagen.BlockLootTable;
import net.lixir.vminus.registry.VRegistry;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.accessor.BlockEntryAccessor;
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

public abstract class VBlockLootProvider extends BlockLootSubProvider {
    private final String modId;

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
            BlockEntryAccessor accessor = (BlockEntryAccessor) block;
            BlockEntry blockEntry = accessor.vminus$getEntry();
            if (blockEntry == null)
                continue;
            BlockLootTable lootTable = blockEntry.getLootTable();
            if (lootTable.isEmpty())
                continue;
            lootTable.apply(block, blockEntry, this);
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return VRegistry.fromId(modId).getBlocks().stream()
                .filter(block -> {
                    if (block.getLootTable().equals(BuiltInLootTables.EMPTY))
                        return false;
                    BlockEntry blockEntry = ((BlockEntryAccessor) block).vminus$getEntry();
                    if (blockEntry == null)
                        return false;
                    BlockLootTable lootTable = blockEntry.getLootTable();
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
