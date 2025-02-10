package net.lixir.vminus.core.conditions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class VisionConditionArguments {
    private final Item item;
    private final ItemStack itemStack;
    private final Entity entity;
    private final Block block;
    private final BlockBehaviour.BlockStateBase blockStateBase;
    private final BlockState blockState;

    private VisionConditionArguments(Builder builder) {
        this.item = builder.item;
        this.entity = builder.entity;
        this.itemStack = builder.itemStack;
        this.block = builder.block;
        this.blockStateBase = builder.blockStateBase;
        this.blockState = builder.blockState;
    }

    public @Nullable Item getItem() {
        return item;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable Entity getEntity() {
        return entity;
    }

    public boolean hasItem() {
        return item != null;
    }

    public boolean hasItemStack() {
        return item != null;
    }

    public boolean hasEntity() {
        return entity != null;
    }

    public @Nullable Block getBlock() {
        return block;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public BlockBehaviour.BlockStateBase getBlockStateBase() {
        return blockStateBase;
    }

    public static class Builder {
        private Item item = null;
        private Block block = null;
        private Entity entity = null;
        private ItemStack itemStack = null;
        private BlockBehaviour.BlockStateBase blockStateBase = null;
        private BlockState blockState = null;

        public Builder passItem(Item item) {
            this.item = item;
            return this;
        }

        public Builder passItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            if (this.item == null)
                this.item = itemStack.getItem();
            return this;
        }

        public Builder passEntity(Entity entity) {
            this.entity = entity;
            return this;
        }

        public Builder passBlock(Block block) {
            this.block = block;
            if (this.item == null)
                this.item = block.asItem();
            return this;
        }

        public Builder passBlockState(BlockState blockState) {
            this.blockState = blockState;
            if (this.block == null)
                this.block = blockState.getBlock();
            if (this.item == null)
                this.item = block.asItem();
            return this;
        }

        public Builder passBlockStateBase(BlockBehaviour.BlockStateBase blockStateBase) {
            this.blockStateBase = blockStateBase;
            if (this.block == null)
                this.block = blockStateBase.getBlock();
            if (this.item == null)
                this.item = block.asItem();
            return this;
        }

        public VisionConditionArguments build() {
            return new VisionConditionArguments(this);
        }
    }
}
