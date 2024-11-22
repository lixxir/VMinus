package net.lixir.vminus.mixins.items;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.lixir.vminus.*;
import net.lixir.vminus.helpers.DurabilityHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.lixir.vminus.init.VminusModAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(value = ItemStack.class, priority = 15000)
public abstract class ItemstackTooltipMixin {
    @Unique
    private final ItemStack itemstack = (ItemStack) (Object) this;
    @Shadow
    @Nullable
    private CompoundTag tag;
    @Shadow
    @Nullable
    private Entity entityRepresentation;

    @Shadow
    protected static Collection<Component> expandBlockState(String string) {
        return null;
    }

    //@Shadow
    //public static boolean shouldShowInTooltip(int p_41627_, ItemStack.TooltipPart p_41628_) {
    //	return false;
    //}
    @Inject(method = "shouldShowInTooltip", at = @At(value = "HEAD"), cancellable = true)
    private static void modifiyShouldShowInTooltip(int p_41627_, ItemStack.TooltipPart p_41628_, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        cir.cancel();
    }

    /**
     * @author lixir
     * @reason To make awesomer enchant names
     */
    @Overwrite
    public static void appendEnchantmentNames(List<Component> enchantmentList, ListTag enchantments) {
        for (int i = 0; i < enchantments.size(); ++i) {
            CompoundTag compoundTag = enchantments.getCompound(i);
            MutableComponent iconComponent = Component.literal(" " + IconHandler.getIcon("effect")).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE));
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundTag)).ifPresent((enchantment) -> {
                enchantmentList.add(enchantment.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundTag)));
            });
        }
    }

    @Shadow
    public abstract Item getItem();

    @Shadow
    protected abstract int getHideFlags();

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract int getBarColor();

    @Shadow
    public abstract Rarity getRarity();

    @Shadow
    public abstract Component getHoverName();

    @Shadow
    public abstract boolean hasCustomHoverName();

    @Shadow
    public abstract boolean isDamageableItem();

    @Inject(method = "getTooltipLines", at = @At(value = "HEAD"), cancellable = true)
    private void getTooltipLines(@Nullable Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
        ItemStack stack = ((ItemStack) (Object) this);
        List<Component> list = cir.getReturnValue() != null ? cir.getReturnValue() : Lists.newArrayList();
        Item item = itemstack.getItem();
        Rarity rarity = item.getRarity(itemstack);
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        String rarityColor = rarity.color.toString();
        Component hoverName = itemstack.getHoverName();
        MutableComponent mutableComponent = Component.empty().append(hoverName).withStyle(Style.EMPTY.withColor(rarity.color));
        String itemId = ForgeRegistries.ITEMS.getKey(item).toString();
        CompoundTag tag = itemstack.getTag();
        int maxDurability = DurabilityHelper.getDurability(true, itemstack);
        int durability = DurabilityHelper.getDurability(itemstack);
        int enchantmentLimit = tag != null ? (int) tag.getDouble("enchantment_limit") : 0;
        boolean echoed = tag != null && tag.getBoolean("echo");
        boolean broken = tag != null && tag.getBoolean("broken");
        String mint = tag != null ? tag.getString("mint") : "";
        boolean deathDurability = tag != null && tag.getBoolean("death_durability");
        int curses = itemstack.isEnchanted() ? EnchantAndCurseHelper.getTotalCurses(itemstack) : 0;
        int enchants = itemstack.isEnchanted() ? EnchantAndCurseHelper.getTotalEnchantments(itemstack) : 0;
        MutableComponent mutablecomponent = Component.empty().append(this.getHoverName()).withStyle(this.getRarity().color);
        boolean inspectable = false;
        if (this.hasCustomHoverName()) {
            mutablecomponent.withStyle(ChatFormatting.ITALIC);
        }
        list.add(mutablecomponent);
        stack.getItem().appendHoverText(itemstack, player == null ? null : player.level(), list, flag);
        if (itemData != null) {
            List<String> tooltips = VisionValueHelper.getTooltips(itemData, itemstack, true);
            for (String tooltipD : tooltips) {
                list.add(Component.literal((tooltipD)));
            }
        }
        if (!flag.isAdvanced() && !stack.hasCustomHoverName() && stack.is(Items.FILLED_MAP)) {
            Integer integer = MapItem.getMapId(stack);
            if (integer != null) {
                list.add((Component.literal("#" + integer)).withStyle(ChatFormatting.GRAY));
            }
        }
        int j = this.getHideFlags();
        //if (shouldShowInTooltip(j, ItemStack.TooltipPart.ADDITIONAL)) {
        //	stack.getItem().appendHoverText(stack, p_41652_ == null ? null : p_41652_.level(), list, p_41653_);
        //}
        if (stack.hasTag()) {
            ItemStack.appendEnchantmentNames(list, stack.getEnchantmentTags());
            if (this.tag.contains("display", 10)) {
                CompoundTag compoundtag = this.tag.getCompound("display");
                if (compoundtag.contains("color", 99)) {
                    if (flag.isAdvanced()) {
                        list.add((Component.translatable("item.color", String.format("#%06X", compoundtag.getInt("color")))).withStyle(ChatFormatting.GRAY));
                    } else {
                        list.add((Component.translatable("item.dyed")).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    }
                }
                if (compoundtag.getTagType("Lore") == 9) {
                    ListTag listtag = compoundtag.getList("Lore", 8);
                    for (int i = 0; i < listtag.size(); ++i) {
                        String s = listtag.getString(i);
                        try {
                            MutableComponent mutablecomponent1 = Component.Serializer.fromJson(s);
                            if (mutablecomponent1 != null) {
                                list.add(ComponentUtils.mergeStyles(mutablecomponent1, Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true)));
                            }
                        } catch (Exception exception) {
                            compoundtag.remove("Lore");
                        }
                    }
                }
            }
        }
        List<Component> attributeTooltips = new ArrayList<>();
        Map<EquipmentSlot, Map<Attribute, AttributeModifier>> mergedAttributes = new HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> attributeModifiers = stack.getAttributeModifiers(slot);
            for (Map.Entry<Attribute, AttributeModifier> entry : attributeModifiers.entries()) {
                Attribute attribute = entry.getKey();
                AttributeModifier modifier = entry.getValue();
                mergedAttributes.computeIfAbsent(slot, k -> new HashMap<>()).merge(attribute, modifier, (existing, newModifier) -> {
                    double combinedAmount;
                    if (existing.getOperation() == newModifier.getOperation()) {
                        if (newModifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                            combinedAmount = existing.getAmount() + newModifier.getAmount();
                        } else {
                            combinedAmount = existing.getAmount() * newModifier.getAmount();
                        }
                    } else {
                        combinedAmount = existing.getAmount();
                    }
                    return new AttributeModifier(newModifier.getName(), combinedAmount, newModifier.getOperation());
                });
            }
        }
        if (itemstack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) itemstack.getItem()).getBlock();
            BlockState blockState = block.defaultBlockState();
            int light = blockState.getLightEmission();
            if (light > 0)
                list.add(Component.literal(" " + IconHandler.getIcon("luminance") + IconHandler.getIcon("blueColor") + " " + light + " Luminance"));
        }
        List<Component> tempList = new ArrayList<>();
        for (Map.Entry<EquipmentSlot, Map<Attribute, AttributeModifier>> entry : mergedAttributes.entrySet()) {
            EquipmentSlot slot = entry.getKey();
            Map<Attribute, AttributeModifier> attributes = entry.getValue();
            if (!attributes.isEmpty()) {
                if (mergedAttributes.size() > 1) {
                    tempList.add(Component.translatable("item.modifiers." + slot.getName()).withStyle(ChatFormatting.GRAY));
                }
                for (Map.Entry<Attribute, AttributeModifier> attributeEntry : attributes.entrySet()) {
                    Attribute attribute = attributeEntry.getKey();
                    AttributeModifier attributeModifier = attributeEntry.getValue();
                    double d0 = attributeModifier.getAmount();
                    double d1;
                    if (attributeModifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributeModifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                        if (attribute == Attributes.KNOCKBACK_RESISTANCE) {
                            d1 = d0 * 10.0D;
                        } else {
                            d1 = d0;
                        }
                    } else {
                        d1 = d0 * 100.0D;
                    }
                    ResourceLocation attributeResourceLocation = ForgeRegistries.ATTRIBUTES.getKey(attribute);
                    String attributeId = attributeResourceLocation != null ? attributeResourceLocation.toString() : "";
                    String icon = IconHandler.getIconForAttribute(attributeId);
                    if (icon == null) {
                        icon = "";
                    }
                    int colorOverride = 0;
                    if (attributeId.equals("vminus:mob_detection_range")) {
                        colorOverride = 1;
                    }
                    MutableComponent iconComponent = Component.literal(" " + icon).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE));
                    MutableComponent attributeComponent = Component.translatable(attribute.getDescriptionId());
                    MutableComponent modifierComponent = null;
                    if (attribute == Attributes.ATTACK_SPEED) {
                        String attackSpeedType = null;
                        if (d1 != 0 && attributeModifier.getOperation() != AttributeModifier.Operation.ADDITION) {
                            if (d1 < -80) {
                                attackSpeedType = "Slow";
                            } else if (d1 <= -65) {
                                attackSpeedType = "Medium";
                            } else if (d1 <= -50) {
                                attackSpeedType = "Fast";
                            } else {
                                attackSpeedType = "Very Fast";
                            }
                        } else {
                            if (d1 >= -1.5) {
                                attackSpeedType = "Very Fast";
                            } else if (d1 >= -2.0) {
                                attackSpeedType = "Fast";
                            } else if (d1 >= -2.5) {
                                attackSpeedType = "Medium";
                            } else if (d1 <= -3) {
                                attackSpeedType = "Very Slow";
                            } else {
                                attackSpeedType = "Slow";
                            }
                        }
                        modifierComponent = Component.literal("");
                        MutableComponent attackSpeed = Component.literal("" + attackSpeedType + " ");
                        modifierComponent.append(attackSpeed);
                        modifierComponent.append(attributeComponent).withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
                    } else {
                        if (d0 > 0.0D) {
                            modifierComponent = Component.translatable("attribute.modifier.plus." + attributeModifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), attributeComponent)
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
                        } else if (colorOverride == 1) {
                            modifierComponent = Component.translatable("attribute.modifier.take." + attributeModifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1 * -1), attributeComponent)
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));
                        } else if (d0 < 0.0D) {
                            d1 *= -1.0D;
                            modifierComponent = Component.translatable("attribute.modifier.take." + attributeModifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), attributeComponent)
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
                        }
                    }
                    if (modifierComponent != null) {
                        Boolean dontRenderAttribute = false;
                        if (attribute == VminusModAttributes.MININGSPEED.get() && itemstack.is(ItemTags.create(new ResourceLocation("vminus:tooltip/hide_mining_speed"))))
                            dontRenderAttribute = true;
                        if (!dontRenderAttribute) {
                            tempList.add(iconComponent.append(modifierComponent));
                        }
                    }
                }
            }
            Collections.sort(tempList, new Comparator<Component>() {
                @Override
                public int compare(Component o1, Component o2) {
                    return o2.getString().compareTo(o1.getString());
                }
            });
            attributeTooltips.addAll(tempList);
        }
        list.addAll(attributeTooltips);
        Boolean unbreakable = false;
        if (stack.hasTag()) {
            if (this.tag.getBoolean("Unbreakable")) {
                list.add((Component.translatable("item.unbreakable")).withStyle(ChatFormatting.BLUE));
                unbreakable = true;
            }
            if (this.tag.contains("CanDestroy", 9)) {
                ListTag listtag1 = this.tag.getList("CanDestroy", 8);
                if (!listtag1.isEmpty()) {
                    list.add(Component.empty());
                    list.add((Component.translatable("item.canBreak")).withStyle(ChatFormatting.GRAY));
                    for (int k = 0; k < listtag1.size(); ++k) {
                        list.addAll(expandBlockState(listtag1.getString(k)));
                    }
                }
            }
            if (this.tag.contains("CanPlaceOn", 9)) {
                ListTag listtag2 = this.tag.getList("CanPlaceOn", 8);
                if (!listtag2.isEmpty()) {
                    list.add(Component.empty());
                    list.add((Component.translatable("item.canPlace")).withStyle(ChatFormatting.GRAY));
                    for (int l = 0; l < listtag2.size(); ++l) {
                        list.addAll(expandBlockState(listtag2.getString(l)));
                    }
                }
            }
        }
        if (item != null) {
            FoodProperties foodProperties = item.getFoodProperties();
            if (foodProperties != null && itemstack.isEdible()) {
                String healSpeed = "";
                String healText = "";
                if (!foodProperties.getEffects().isEmpty()) {
                    foodProperties.getEffects().forEach(effectPair -> {
                        String effectName = effectPair.getFirst().getEffect().getDisplayName().getString();
                        int duration = effectPair.getFirst().getDuration();
                        int amplifier = effectPair.getFirst().getAmplifier();
                        float chance = effectPair.getSecond();
                        int durationSeconds = duration / 20;
                        boolean isBadEffect = !effectPair.getFirst().getEffect().isBeneficial();
                        String icon = !isBadEffect ? IconHandler.getIcon("effect") + IconHandler.getIcon("grayColor") : IconHandler.getIcon("bad_effect") + IconHandler.getIcon("redColor");
                        Component effectLevel = Component.translatable("potion.potency." + amplifier);
                        String durationMinutes = String.format("%02d", durationSeconds / 60);
                        String durationSecondsString = String.format("%02d", durationSeconds % 60);
                        String durationString = durationMinutes + ":" + durationSecondsString;
                        String chanceString = chance < 1 ? " [" + (int) (chance * 100) + "%]" : "";
                        MutableComponent finalString = Component.literal(" " + icon + effectName + (!effectLevel.toString().isEmpty() ? " " : ""));
                        list.add(finalString.append(effectLevel).append(Component.literal((!isBadEffect ? IconHandler.getIcon("grayColor") : IconHandler.getIcon("redColor")) + " (" + durationString + ")" + chanceString)));
                    });
                }
                if (ModList.get().isLoaded("detour")) {
                    if (itemstack.is(ItemTags.create(new ResourceLocation("detour:food/fast_healing_speed")))) {
                        healSpeed = IconHandler.getIcon("fast_hunger_shank");
                        healText = "Fast Saturation";
                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("detour:food/slow_healing_speed")))) {
                        healSpeed = IconHandler.getIcon("slow_hunger_shank");
                        healText = "Slow Saturation";
                    } else {
                        healSpeed = IconHandler.getIcon("hunger_shank");
                        healText = "Saturation";
                    }
                } else {
                    healSpeed = IconHandler.getIcon("hunger_shank");
                    healText = "Nutrition";
                }
                list.add(Component.literal(" " + healSpeed + IconHandler.getIcon("blueColor") + foodProperties.getNutrition() + " " + healText));
                if (!ModList.get().isLoaded("detour")) {
                    double saturation = foodProperties.getSaturationModifier();
                    String saturationString = "";
                    if (saturation <= 0.2) {
                        saturationString = "Poor";
                    } else if (saturation <= 0.6 && saturation > 0.2) {
                        saturationString = "Low";
                    } else if (saturation <= 1.2 && saturation > 0.6) {
                        saturationString = "Normal";
                    } else if (saturation <= 1.6 && saturation > 1.2) {
                        saturationString = "Good";
                    } else if (saturation > 1.6) {
                        saturationString = "Supernatural";
                    }
                    list.add(Component.literal(" " + IconHandler.getIcon("saturation") + IconHandler.getIcon("blueColor") + saturationString + " Saturation"));
                }
                double eatDuration = item.getUseDuration(itemstack);
                list.add(Component.literal(" " + IconHandler.getIcon("eating_duration") + IconHandler.getIcon("blueColor") + (new java.text.DecimalFormat("#.#").format(eatDuration / 20)) + "s Eating Duration"));
            }
        }
        StringBuilder genericStats = new StringBuilder();
        if (maxDurability >= 1 && (!itemstack.is(ItemTags.create(new ResourceLocation("vminus:cosmetic"))) && !echoed) && !unbreakable)
            genericStats.append(deathDurability ? IconHandler.getIcon("death_durability") : IconHandler.getIcon("anvil")).append((tag != null && tag.contains("reinforcement")) ? IconHandler.getIcon("aquaColor") : IconHandler.getIcon("grayColor"))
                    .append(durability).append(IconHandler.getIcon("darkGrayColor")).append("/").append(maxDurability).append(" ");
        if (enchantmentLimit >= 1 && !itemstack.is(ItemTags.create(new ResourceLocation("vminus:unenchantable"))) && (itemstack.isEnchantable() || itemstack.isEnchanted()))
            genericStats.append(IconHandler.getIcon("rune") + IconHandler.getIcon("grayColor") + enchants + IconHandler.getIcon("darkGrayColor") + "/" + enchantmentLimit + " ");
        if (!genericStats.toString().isEmpty()) {
            String genericStatsString = genericStats.toString().replaceAll("\\s+$", "");
            list.add(Component.literal(genericStatsString));
        }
        if (itemData != null) {
            List<String> tooltips = VisionValueHelper.getTooltips(itemData, itemstack, false);
            for (String tooltipD : tooltips) {
                list.add(Component.literal((tooltipD)));
            }
        }
        net.minecraftforge.event.ForgeEventFactory.onItemTooltip(stack, player, list, flag);
        if (flag.isAdvanced()) {
            if (player.getAbilities().instabuild) {
                list.add((Component.literal(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (stack.hasTag()) {
                list.add(Component.translatable("item.nbt_tags", stack.getTag().getAllKeys().size()).withStyle(ChatFormatting.DARK_GRAY));
                if (tag != null) {
                    String json = tag.toString();
                    Component nbtText = Component.literal(json).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
                    list.add(nbtText);
                }
            }
        }
        cir.setReturnValue(list);
    }
}
