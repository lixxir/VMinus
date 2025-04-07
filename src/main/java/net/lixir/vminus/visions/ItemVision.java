package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.parser.*;
import net.lixir.vminus.visions.util.*;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.accessors.IItemVisionAccessor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ItemVision extends Vision {
    public static final ItemVision EMPTY = new ItemVision();

    public final VisionProperty<Integer> max_stack_size;
    public final VisionProperty<Integer> enchantability;
    public final VisionProperty<Integer> use_duration;
    public final VisionProperty<Integer> max_damage;
    public final VisionProperty<Integer> fuel_time;

    public final VisionProperty<Boolean> fire_resistant;
    public final VisionProperty<Boolean> can_equip;
    public final VisionProperty<Boolean> damageable;
    public final VisionProperty<Boolean> enchantable;
    public final VisionProperty<Boolean> glint;
    public final VisionProperty<Boolean> ban;

    public final VisionProperty<VisionItemReplacement> replace;
    public final VisionProperty<Rarity> rarity;
    public final VisionProperty<EquipmentSlot> equip_slot;
    public final VisionProperty<VisionFoodProperties> food_properties;
    public final VisionProperty<VisionAttribute> attribute;
    public final VisionProperty<VisionTrait> trait;
    public final VisionProperty<VisionItemDecorator> decorator;

    public ItemVision() {
        max_stack_size = create("max_stack_size", new VisionIntParser());
        enchantability = create("enchantability", new VisionIntParser());
        use_duration = create("use_duration", new VisionIntParser());
        max_damage = create("max_damage", new VisionIntParser());
        fuel_time = create("fuel_time", new VisionIntParser());
        fire_resistant = create("fire_resistant", new VisionBooleanParser());
        can_equip = create("can_equip", new VisionBooleanParser());
        enchantable = create("enchantable", new VisionBooleanParser());
        damageable = create("damageable", new VisionBooleanParser());
        glint = create("glint", new VisionBooleanParser());
        ban = create("ban", new VisionBooleanParser());
        replace = create("replace", new VisionItemReplacementParser());
        rarity = create("rarity", new VisionRarityParser());
        equip_slot = create("equip_slot", new VisionEnumParser<>(EquipmentSlot.class));
        food_properties = create("food_properties", new VisionFoodPropertiesParser());
        attribute = create("attribute", new VisionAttributeParser());
        trait = create("trait", new VisionTraitParser());
        decorator = create("decorator", new VisionItemDecoratorParser());

        VisionExtensions.applyExtensions(this);
    }

    public static ItemVision of(Item item) {
        if (item instanceof IItemVisionAccessor iItemVisionAccessor)
            return iItemVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    public static @NotNull ItemVision of(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return EMPTY;
        if (itemStack.getItem() instanceof IItemVisionAccessor iItemVisionAccessor)
            return iItemVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    public static @NotNull ItemVision of(@Nullable ItemEntity itemEntity) {
        if (itemEntity == null)
            return EMPTY;
        if (itemEntity.getItem().isEmpty())
            return EMPTY;
        if (itemEntity.getItem().getItem() instanceof IItemVisionAccessor iItemVisionAccessor)
            return iItemVisionAccessor.vminus$getVision();
        return EMPTY;
    }
}
