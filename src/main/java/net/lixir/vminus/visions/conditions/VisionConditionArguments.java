package net.lixir.vminus.visions.conditions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class VisionConditionArguments {
    private Item item = null;
    private ItemStack itemStack = null;
    private Entity entity = null;
    private Block block = null;
    private BlockBehaviour.BlockStateBase blockStateBase = null;
    private BlockState blockState = null;
    private MobEffect mobEffect = null;
    private EntityType<?> entityType = null;

    private VisionConditionArguments(Builder builder) {
        this.item = builder.item;
        this.entity = builder.entity;
        this.itemStack = builder.itemStack;
        this.block = builder.block;
        this.blockStateBase = builder.blockStateBase;
        this.blockState = builder.blockState;
    }

    public VisionConditionArguments(@Nullable ItemEntity itemEntity) {
        if (itemEntity == null)
            return;
        this.itemStack = itemEntity.getItem();
        this.item = itemStack.getItem();
    }

    public VisionConditionArguments(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return;
        this.item = itemStack.getItem();
        this.itemStack = itemStack;
    }

    public VisionConditionArguments(Item item) {
        this.item = item;
    }


    public VisionConditionArguments(MobEffect mobEffect) {
        this.mobEffect = mobEffect;
    }

    public VisionConditionArguments(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public VisionConditionArguments(Entity entity) {
        this.entity = entity;
        this.entityType = entity.getType();
    }

    public VisionConditionArguments(Block block) {
        this.block = block;
    }

    public VisionConditionArguments(BlockBehaviour.BlockStateBase blockStateBase) {
        this.blockStateBase = blockStateBase;
    }


    public boolean hasItem() {
        return item != null;
    }

    public boolean hasItemStack() {
        return itemStack != null;
    }

    public boolean hasEntity() {
        return entity != null;
    }

    public @Nullable Item getItem() {
        return item;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable EntityType<?> getEntityType() {
        return entityType;
    }

    public @Nullable Entity getEntity() {
        return entity;
    }

    public @Nullable Block getBlock() {
        return block;
    }

    public @Nullable BlockState getBlockState() {
        return blockState;
    }

    public @Nullable BlockBehaviour.BlockStateBase getBlockStateBase() {
        return blockStateBase;
    }


    public static class Builder {
        private Item item = null;
        private Block block = null;
        private Entity entity = null;
        private ItemStack itemStack = null;
        private BlockBehaviour.BlockStateBase blockStateBase = null;
        private BlockState blockState = null;

        public Builder pass(Item item) {
            this.item = item;
            return this;
        }

        public Builder pass(ItemStack itemStack) {
            this.itemStack = itemStack;
            if (this.item == null)
                this.item = itemStack.getItem();
            return this;
        }

        public Builder pass(Entity entity) {
            this.entity = entity;
            return this;
        }

        public Builder pass(Block block) {
            this.block = block;
            if (this.item == null)
                this.item = block.asItem();
            return this;
        }

        public Builder pass(BlockState blockState) {
            this.blockState = blockState;
            if (this.block == null)
                this.block = blockState.getBlock();
            if (this.item == null)
                this.item = block.asItem();
            return this;
        }

        public Builder pass(BlockBehaviour.BlockStateBase blockStateBase) {
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
