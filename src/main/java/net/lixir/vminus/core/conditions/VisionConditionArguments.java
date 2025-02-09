package net.lixir.vminus.core.conditions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class VisionConditionArguments {
    private final Item item;
    private final ItemStack itemStack;
    private final Entity entity;

    private VisionConditionArguments(Builder builder) {
        this.item = builder.item;
        this.entity = builder.entity;
        this.itemStack = builder.itemStack;
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

    public static class Builder {
        private Item item = null;
        private Entity entity = null;
        private ItemStack itemStack = null;

        public Builder passItem(Item item) {
            this.item = item;
            return this;
        }

        public Builder passItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            this.item = itemStack.getItem();
            return this;
        }

        public Builder passEntity(Entity entity) {
            this.entity = entity;
            return this;
        }

        public VisionConditionArguments build() {
            return new VisionConditionArguments(this);
        }
    }
}
