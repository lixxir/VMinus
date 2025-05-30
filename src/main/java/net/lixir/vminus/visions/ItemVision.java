package net.lixir.vminus.visions;

import net.lixir.vminus.visions.accessors.ItemVisionAccessor;
import net.lixir.vminus.visions.resources.VisionCodecs;
import net.lixir.vminus.visions.resources.codec.*;
import net.lixir.vminus.visions.util.*;
import net.lixir.vminus.visions.values.VisionProperty;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ItemVision extends Vision {
    public static final ItemVision EMPTY = new ItemVision();

    public final VisionProperty<Integer> max_stack_size;
    public final VisionProperty<Integer> enchantability;
    public final VisionProperty<Integer> use_duration;
    public final VisionProperty<Integer> max_damage;
    public final VisionProperty<Integer> max_duration;
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
    public final VisionProperty<UseAnim> use_animation;
    public final VisionProperty<VisionFoodProperties> food_properties;
    public final VisionProperty<VisionAttribute> attribute;
    public final VisionProperty<VisionTrait> trait;
    public final VisionProperty<VisionItemDecorator> decorator;

    public ItemVision() {
        max_stack_size = create("max_stack_size", VisionCodecs.intCodec());
        enchantability = create("enchantability", VisionCodecs.intCodec());
        use_duration = create("use_duration", VisionCodecs.intCodec());
        max_damage = create("max_damage", VisionCodecs.intCodec());
        max_duration = create("max_duration", VisionCodecs.intCodec());
        fuel_time = create("fuel_time", VisionCodecs.intCodec());
        fire_resistant = create("fire_resistant", VisionCodecs.booleanCodec());
        can_equip = create("can_equip", VisionCodecs.booleanCodec());
        enchantable = create("enchantable", VisionCodecs.booleanCodec());
        damageable = create("damageable", VisionCodecs.booleanCodec());
        glint = create("glint", VisionCodecs.booleanCodec());
        ban = create("ban", VisionCodecs.booleanCodec());
        replace = create("replace", VisionCodecs.itemReplacementCodec());
        rarity = create("rarity", new VisionRarityCodec());
        equip_slot = create("equip_slot", new VisionEnumCodec<>(EquipmentSlot.class));
        use_animation = create("use_animation", new VisionEnumCodec<>(UseAnim.class));
        food_properties = create("food_properties", new VisionFoodPropertiesCodec());
        attribute = create("attribute", new VisionAttributeCodec(), true);
        trait = create("itemTrait", new VisionTraitCodec(), true);
        decorator = create("decorator", new VisionItemDecoratorCodec(), true);

        VisionExtensions.applyExtensions(this);
    }

    public static @NotNull ItemVision of(Item item) {
        if (item instanceof ItemVisionAccessor itemVisionAccessor)
            return itemVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    public static @NotNull ItemVision of(@Nullable ItemStack itemStack) {
        if (itemStack == null)
            return EMPTY;
        if (itemStack.getItem() instanceof ItemVisionAccessor itemVisionAccessor)
            return itemVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    public static @NotNull ItemVision of(@Nullable ItemEntity itemEntity) {
        if (itemEntity == null)
            return EMPTY;
        if (itemEntity.getItem().isEmpty())
            return EMPTY;
        if (itemEntity.getItem().getItem() instanceof ItemVisionAccessor itemVisionAccessor)
            return itemVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    @Override
    public String getEntryListName() {
        return VisionType.ITEM.getListName();
    }
}
