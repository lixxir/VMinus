package net.lixir.vminus.core.visions;

import net.lixir.vminus.core.util.VisionItemDecorator;
import net.lixir.vminus.core.util.VisionTrait;
import net.lixir.vminus.core.values.VisionProperty;
import net.lixir.vminus.core.util.VisionAttribute;
import net.lixir.vminus.core.util.VisionFoodProperties;
import net.lixir.vminus.core.values.BasicVisionValue;
import net.lixir.vminus.core.visions.accessors.IItemVisionAccessor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;

public class ItemVision extends Vision<ItemVision> {
    private static final ItemVision emptyVision = new ItemVision();

    public final VisionProperty<BasicVisionValue<Integer>, Integer> maxStackSize = new VisionProperty<>("max_stack_size");
    public final VisionProperty<BasicVisionValue<Integer>, Integer> enchantability = new VisionProperty<>("enchantability");
    public final VisionProperty<BasicVisionValue<Integer>, Integer> useDuration = new VisionProperty<>("use_duration");
    public final VisionProperty<BasicVisionValue<Integer>, Integer> maxDamage = new VisionProperty<>("max_damage");
    public final VisionProperty<BasicVisionValue<Integer>, Integer> fuelTime = new VisionProperty<>("fuel_time");

    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> fireResistant = new VisionProperty<>("fire_resistant");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> canEquip = new VisionProperty<>("can_equip");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> damageable = new VisionProperty<>("damageable");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> enchantable = new VisionProperty<>("enchantable");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> hasGlint = new VisionProperty<>("has_glint");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> ban = new VisionProperty<>("ban");

    public final VisionProperty<BasicVisionValue<ItemStack>, ItemStack> replace = new VisionProperty<>("replace");

    public final VisionProperty<BasicVisionValue<Rarity>, Rarity> rarity = new VisionProperty<>("rarity");
    public final VisionProperty<BasicVisionValue<UseAnim>, UseAnim> useAnimation = new VisionProperty<>("use_animation");
    public final VisionProperty<BasicVisionValue<EquipmentSlot>, EquipmentSlot> equipSlot = new VisionProperty<>("equip_slot");
    public final VisionProperty<BasicVisionValue<VisionFoodProperties>, VisionFoodProperties> foodProperties = new VisionProperty<>("food");
    public final VisionProperty<BasicVisionValue<VisionAttribute>, VisionAttribute> attribute = new VisionProperty<>("attribute");
    public final VisionProperty<BasicVisionValue<VisionTrait>, VisionTrait> trait = new VisionProperty<>("trait");
    public final VisionProperty<BasicVisionValue<VisionItemDecorator>, VisionItemDecorator> decorator = new VisionProperty<>("decorator");


    @Override
    public void merge(ItemVision vision) {
        maxStackSize.merge(vision.maxStackSize);
        enchantability.merge(vision.enchantability);
        useDuration.merge(vision.useDuration);
        maxDamage.merge(vision.maxDamage);
        fuelTime.merge(vision.fuelTime);

        fireResistant.merge(vision.fireResistant);
        canEquip.merge(vision.canEquip);
        damageable.merge(vision.damageable);
        enchantable.merge(vision.enchantable);
        hasGlint.merge(vision.hasGlint);
        ban.merge(vision.ban);

        replace.merge(vision.replace);
        rarity.merge(vision.rarity);
        foodProperties.merge(vision.foodProperties);
        useAnimation.merge(vision.useAnimation);
        equipSlot.merge(vision.equipSlot);
        attribute.merge(vision.attribute);
        trait.merge(vision.trait);
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Vision Value(s): [");

        appendProperty(stringBuilder, maxStackSize);
        appendProperty(stringBuilder, enchantability);
        appendProperty(stringBuilder, useDuration);
        appendProperty(stringBuilder, maxDamage);
        appendProperty(stringBuilder, fuelTime);

        appendProperty(stringBuilder, fireResistant);
        appendProperty(stringBuilder, canEquip);
        appendProperty(stringBuilder, damageable);
        appendProperty(stringBuilder, enchantable);
        appendProperty(stringBuilder, hasGlint);

        appendProperty(stringBuilder, rarity);
        appendProperty(stringBuilder, foodProperties);
        appendProperty(stringBuilder, useAnimation);
        appendProperty(stringBuilder, equipSlot);

        if (stringBuilder.charAt(stringBuilder.length()-1) != '[')
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    public static ItemVision getVision(Item item) {
        if (item instanceof IItemVisionAccessor iItemVisionAccessor)
            return iItemVisionAccessor.vminus$getVision();
        return emptyVision;
    }

    public static ItemVision getVision(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IItemVisionAccessor iItemVisionAccessor)
            return iItemVisionAccessor.vminus$getVision();
        return emptyVision;
    }
}
