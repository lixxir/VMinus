package net.lixir.vminus.mixins.items;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusConfig;
import net.lixir.vminus.core.Visions;
import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.util.EnchantmentVisionHelper;
import net.lixir.vminus.core.util.VisionFoodProperties;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.visions.visionable.IItemVisionable;
import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.traits.Trait;
import net.lixir.vminus.traits.Traits;
import net.lixir.vminus.util.DurabilityHelper;
import net.lixir.vminus.util.EnchantAndCurseHelper;
import net.lixir.vminus.util.IconHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.extensions.IForgeItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IForgeItemStack {
    @Unique
    private final ItemStack vminus$itemStack = (ItemStack) (Object) this;

    @Inject(method = "shouldShowInTooltip", at = @At(value = "HEAD"), cancellable = true)
    private static void vminus$shouldShowInTooltip(int p_41627_, ItemStack.TooltipPart p_41628_, CallbackInfoReturnable<Boolean> cir) {
        if (!VMinusConfig.TOOLTIP_REWORK.get())
            return;
        cir.setReturnValue(true);
        cir.cancel();
    }

    @Inject(method = "appendEnchantmentNames", at = @At(value = "HEAD"), cancellable = true)
    private static void appendEnchantmentNames(List<Component> enchantmentList, ListTag enchantments, CallbackInfo ci) {
        if (!VMinusConfig.TOOLTIP_REWORK.get())
            return;
        for (int i = 0; i < enchantments.size(); ++i) {
            CompoundTag compoundTag = enchantments.getCompound(i);
            // MutableComponent iconComponent = Component.literal(" " + IconHandler.getIcon("effect")).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE));
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundTag)).ifPresent((enchantment) -> {
                enchantmentList.add(Component.literal(" ").append(enchantment.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundTag))));
            });
        }
        ci.cancel();
    }

    @Unique
    public ItemVision vminus$getVision() {
        if (vminus$itemStack.getItem() instanceof IItemVisionable iVisionable) {
            return iVisionable.vminus$getVision();
        }
        return null;
    }

    @Override
    public boolean makesPiglinsNeutral(LivingEntity wearer) {
        if (Traits.hasTrait(vminus$itemStack, Traits.PIGLIN_CHARM.get()))
            return (Traits.getTrait(vminus$itemStack, Traits.PIGLIN_CHARM.get()));
        return vminus$itemStack.getItem().makesPiglinsNeutral(vminus$itemStack, wearer);
    }

    @Override
    public int getBurnTime(@Nullable RecipeType<?> recipeType) {
        Integer value = vminus$getVision().fuelTime.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) return value;
        return vminus$itemStack.getItem().getBurnTime(vminus$itemStack, recipeType);
    }


    @Override
    public EquipmentSlot getEquipmentSlot() {
        EquipmentSlot value = vminus$getVision().equipSlot.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) return value;
        return vminus$itemStack.getItem().getEquipmentSlot(vminus$itemStack);
    }

    @Override
    public boolean canEquip(EquipmentSlot armorType, Entity entity) {
        Boolean value = vminus$getVision().canEquip.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).passEntity(entity).build());
        if (value != null) return value;
        EquipmentSlot equipValue = vminus$getVision().equipSlot.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).passEntity(entity).build());
        if (equipValue != null) return true;
        return vminus$itemStack.getItem().canEquip(vminus$itemStack, armorType, entity);
    }


    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    public void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            vminus$itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
                int numberOfSlots = capability.getSlots();
                double amount = 0;
                for (int i = 0; i < numberOfSlots; i++) {
                    ItemStack itemStackInSlot = capability.getStackInSlot(i);
                    amount += (double) itemStackInSlot.getCount() / (double) itemStackInSlot.getMaxStackSize();
                }
                float fullness = (float) amount / numberOfSlots;
                int barWidth = (int) Math.floor(13.0F * fullness);
                cir.setReturnValue(Math.min(barWidth, 13));
            });
        }
        /*
        if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
            float durabilityRatio = (float) vminus$itemStack.getTag().getInt("reinforcement") / (float) vminus$itemStack.getTag().getInt("max_reinforcement");
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }
        */
        if (vminus$itemStack.isDamageableItem()) {
            float durabilityRatio = (float) vminus$itemStack.getDamageValue() / vminus$itemStack.getMaxDamage();
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }


    }

    @Inject(method = "getBarColor", at = @At("RETURN"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> cir) {
        /*
        JsonObject itemData = Visions.getData(vminus$itemStack);
        if (itemData != null && itemData.has("bar")) {
            int startColor = 4384126;
            int endColor = 2186818;
            try {
                String startColorString = VisionValueHandler.getFirstValidString(itemData, "bar", vminus$itemStack, "start_color");
                if (startColorString != null)
                    startColor = Integer.decode(startColorString.trim());
            } catch (NumberFormatException e) {
                VMinus.LOGGER.error("Invalid start_color format: " + itemData.get("start_color").getAsString());
            }
            try {
                String endColorString = VisionValueHandler.getFirstValidString(itemData, "bar", vminus$itemStack, "end_color");
                if (endColorString != null)
                    endColor = Integer.decode(endColorString.trim());
            } catch (NumberFormatException e) {
                VMinus.LOGGER.error("Invalid end_color format: " + itemData.get("end_color").getAsString());
            }
            float durabilityRatio = (float) DurabilityHelper.getDurability(vminus$itemStack) / (float) DurabilityHelper.getDurability(true, vminus$itemStack);
            int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
            cir.setReturnValue(transitionColor);
            cir.cancel();
        } else {
            if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
                vminus$itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                    int numberOfSlots = capability.getSlots();
                    int totalItems = 0;
                    int maxCapacity = 0;
                    for (int i = 0; i < numberOfSlots; i++) {
                        ItemStack itemStackInSlot = capability.getStackInSlot(i);
                        totalItems += itemStackInSlot.getCount();
                        maxCapacity += itemStackInSlot.getMaxStackSize();
                    }
                    float fullness = maxCapacity > 0 ? (float) totalItems / maxCapacity : 0;
                    int containerItemColor = vminus$rgbToColor(0.4F, 0.4F, 1.0F);
                    cir.setReturnValue(containerItemColor);
                });
            }
            if (vminus$itemStack.isDamageableItem()) {
                if (vminus$itemStack.getTag().getBoolean("broken")) {
                    cir.setReturnValue(Mth.hsvToRgb(0.01F, 0.0F, 0.35F));
                } else if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
                    int startColor = 0x55FFFF;
                    int endColor = 0x22a53f;
                    float durabilityRatio = (float) vminus$itemStack.getTag().getInt("reinforcement") / (float) vminus$itemStack.getTag().getInt("max_reinforcement");
                    int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
                    cir.setReturnValue(transitionColor);
                } else {
                    int startColor;
                    int endColor;
                    float durabilityRatio = (float) DurabilityHelper.getDurability(vminus$itemStack) / (float) DurabilityHelper.getDurability(true, vminus$itemStack);
                    if (vminus$itemStack.getTag().getBoolean("death_durability")) {
                        startColor = 0xFF00FF;
                        endColor = 0x550055;
                    } else {
                        startColor = 0x69fc2a;
                        endColor = 0xe22626;
                    }
                    int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
                    cir.setReturnValue(transitionColor);
                }
            }
        }

         */
    }

    @Inject(method = "enchant", at = @At("HEAD"), cancellable = true)
    public void enchant(Enchantment enchantment, int level, CallbackInfo ci) {
        if (EnchantmentVisionHelper.isBanned(enchantment)) {
            ci.cancel();
            return;
        }
        ItemStack itemstack = (ItemStack) (Object) this;
        CompoundTag tag = itemstack.getOrCreateTag();
        if (!tag.contains("enchantment_limit"))
            return;
        int enchantmentLimit = tag.getInt("enchantment_limit");
        double currentTotalEnchantmentLevel = 0.0;
        if (itemstack.isEnchanted()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                currentTotalEnchantmentLevel += entry.getValue();
            }
        }
        if (currentTotalEnchantmentLevel + level > enchantmentLimit)
            ci.cancel();
    }

    @Inject(method = "isBarVisible", at = @At("RETURN"), cancellable = true)
    public void isBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            vminus$itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
                boolean hasItems = false;
                for (int i = 0; i < capability.getSlots(); i++) {
                    if (capability.getStackInSlot(i).getCount() > 0) {
                        hasItems = true;
                        break;
                    }
                }
                cir.setReturnValue(hasItems);
            });
        }
        /*
        if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
            if (vminus$itemStack.getTag().getInt("reinforcement") < vminus$itemStack.getTag().getInt("max_reinforcement")) {
                cir.setReturnValue(true);
            }
        } else if (DurabilityHelper.getDurability(vminus$itemStack) < DurabilityHelper.getDurability(true, vminus$itemStack)) {
            cir.setReturnValue(true);
        }

         */
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().maxDamage.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "isDamageableItem", at = @At("RETURN"), cancellable = true)
    public void isDamageableItem(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().damageable.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) cir.setReturnValue(value);
        Integer maxDamageValue = vminus$getVision().maxDamage.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (maxDamageValue != null) cir.setReturnValue(maxDamageValue > 0);
    }

    @Inject(method = "isEnchantable", at = @At("RETURN"), cancellable = true)
    private void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().enchantable.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        VisionFoodProperties value = vminus$getVision().foodProperties.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) cir.setReturnValue(true);
    }

    @Inject(method = "getDrinkingSound", at = @At("RETURN"), cancellable = true)
    private void getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        VisionFoodProperties value = vminus$getVision().foodProperties.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null && value.getEatSound() != null)
            cir.setReturnValue(value.getEatSound());
    }

    @Inject(method = "hasFoil", at = @At("RETURN"), cancellable = true)
    private void hasFoil(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().hasGlint.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getEatingSound", at = @At("RETURN"), cancellable = true)
    private void getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        VisionFoodProperties value = vminus$getVision().foodProperties.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null && value.getEatSound() != null)
            cir.setReturnValue(value.getEatSound());
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().useDuration.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemStack).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getTooltipLines", at = @At(value = "HEAD"), cancellable = true)
    private void getTooltipLines(@Nullable Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
        if (!VMinusConfig.TOOLTIP_REWORK.get())
            return;
        ItemStackAccessor accessor = (ItemStackAccessor) (Object) vminus$itemStack;

        Item item = vminus$itemStack.getItem();

        List<Component> list = cir.getReturnValue() != null ? cir.getReturnValue() : Lists.newArrayList();

        JsonObject itemData = Visions.getData(vminus$itemStack);

        CompoundTag tag = vminus$itemStack.getTag();

        int maxDurability = DurabilityHelper.getDurability(true, vminus$itemStack);
        int durability = DurabilityHelper.getDurability(vminus$itemStack);
        int enchantmentLimit = tag != null ? (int) tag.getDouble("enchantment_limit") : 0;
        int enchants = vminus$itemStack.isEnchanted() ? EnchantAndCurseHelper.getTotalEnchantments(vminus$itemStack) : 0;

        boolean echoed = tag != null && tag.getBoolean("echo");
        boolean deathDurability = tag != null && tag.getBoolean("death_durability");

        MutableComponent mutablecomponent = Component.empty().append(vminus$itemStack.getHoverName()).withStyle(vminus$itemStack.getRarity().color);

        if (vminus$itemStack.hasCustomHoverName()) {
            mutablecomponent.withStyle(ChatFormatting.ITALIC);
        }
        list.add(mutablecomponent);

        if (vminus$itemStack.getCount() >= 1000) {
            list.add(Component.literal(new DecimalFormat("#,###").format(vminus$itemStack.getCount())).withStyle(ChatFormatting.GRAY));
        }


        vminus$itemStack.getItem().appendHoverText(vminus$itemStack, player == null ? null : player.level(), list, flag);
        /*
        if (itemData != null) {
            List<String> tooltips = VisionValueHandler.getTooltips(itemData, vminus$itemStack, true);
            for (String tooltipD : tooltips) {
                list.add(Component.literal((tooltipD)));
            }
        }

         */
        if (!flag.isAdvanced() && !vminus$itemStack.hasCustomHoverName() && vminus$itemStack.is(Items.FILLED_MAP)) {
            Integer integer = MapItem.getMapId(vminus$itemStack);
            if (integer != null) {
                list.add((Component.literal("#" + integer)).withStyle(ChatFormatting.GRAY));
            }
        }
        int j = accessor.invokeGetHideFlags();
        //if (shouldShowInTooltip(j, ItemStack.TooltipPart.ADDITIONAL)) {
        //	stack.getItem().appendHoverText(stack, p_41652_ == null ? null : p_41652_.level(), list, p_41653_);
        //}
        if (vminus$itemStack.hasTag()) {
            ItemStack.appendEnchantmentNames(list, vminus$itemStack.getEnchantmentTags());
            if (vminus$itemStack.getTag().contains("display", 10)) {
                CompoundTag compoundtag = vminus$itemStack.getTag().getCompound("display");
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
            Multimap<Attribute, AttributeModifier> attributeModifiers = vminus$itemStack.getAttributeModifiers(slot);
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
        List<Component> tempList = new ArrayList<>();

        for (Trait trait : Traits.getTraits(vminus$itemStack)) {
            if (!trait.isHidden()) {
                MutableComponent mutableComponent = Component.literal(" ");
                mutableComponent.append(Component.translatable("trait." + trait.getNamespace() + "." + trait.getName()).withStyle(ChatFormatting.BLUE));
                list.add(mutableComponent);
            }
        }


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
                        MutableComponent attackSpeed = Component.literal(attackSpeedType + " ");
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
                        if (attribute == VMinusAttributes.MINING_SPEED.get() && vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:tooltip/hide_mining_speed"))))
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
        if (vminus$itemStack.hasTag()) {
            if (vminus$itemStack.getTag().getBoolean("Unbreakable")) {
                list.add((Component.translatable("item.unbreakable")).withStyle(ChatFormatting.BLUE));
                unbreakable = true;
            }
            if (vminus$itemStack.getTag().contains("CanDestroy", 9)) {
                ListTag listtag1 = vminus$itemStack.getTag().getList("CanDestroy", 8);
                if (!listtag1.isEmpty()) {
                    list.add(Component.empty());
                    list.add((Component.translatable("item.canBreak")).withStyle(ChatFormatting.GRAY));
                    for (int k = 0; k < listtag1.size(); ++k) {
                        list.addAll(accessor.getExpandBlockState(listtag1.getString(k)));
                    }
                }
            }
            if (vminus$itemStack.getTag().contains("CanPlaceOn", 9)) {
                ListTag listtag2 = vminus$itemStack.getTag().getList("CanPlaceOn", 8);
                if (!listtag2.isEmpty()) {
                    list.add(Component.empty());
                    list.add((Component.translatable("item.canPlace")).withStyle(ChatFormatting.GRAY));
                    for (int l = 0; l < listtag2.size(); ++l) {
                        list.addAll(accessor.getExpandBlockState(listtag2.getString(l)));
                    }
                }
            }
        }
        FoodProperties foodProperties = item.getFoodProperties();
        if (foodProperties != null && vminus$itemStack.isEdible()) {
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
                if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("detour:food/fast_healing_speed")))) {
                    healSpeed = IconHandler.getIcon("fast_hunger_shank");
                    healText = "Fast Saturation";
                } else if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("detour:food/slow_healing_speed")))) {
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
            double eatDuration = item.getUseDuration(vminus$itemStack);
            list.add(Component.literal(" " + IconHandler.getIcon("eating_duration") + IconHandler.getIcon("blueColor") + (new java.text.DecimalFormat("#.#").format(eatDuration / 20)) + "s Eating Duration"));
        }
        StringBuilder genericStats = new StringBuilder();
        if (maxDurability >= 1 && (!vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:cosmetic"))) && !echoed) && !unbreakable)
            genericStats.append(deathDurability ? IconHandler.getIcon("death_durability") : IconHandler.getIcon("anvil")).append((tag != null && tag.contains("reinforcement")) ? IconHandler.getIcon("aquaColor") : IconHandler.getIcon("grayColor"))
                    .append(durability).append(IconHandler.getIcon("darkGrayColor")).append("/").append(maxDurability).append(" ");
        if (enchantmentLimit >= 1 && !vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:unenchantable"))) && (vminus$itemStack.isEnchantable() || vminus$itemStack.isEnchanted()))
            genericStats.append(IconHandler.getIcon("rune") + IconHandler.getIcon("grayColor") + enchants + IconHandler.getIcon("darkGrayColor") + "/" + enchantmentLimit + " ");
        if (!genericStats.toString().isEmpty()) {
            String genericStatsString = genericStats.toString().replaceAll("\\s+$", "");
            list.add(Component.literal(genericStatsString));
        }
        /*
        if (itemData != null) {
            List<String> tooltips = VisionValueHandler.getTooltips(itemData, vminus$itemStack, false);
            for (String tooltipD : tooltips) {
                list.add(Component.literal((tooltipD)));
            }
        }

         */
        net.minecraftforge.event.ForgeEventFactory.onItemTooltip(vminus$itemStack, player, list, flag);
        if (flag.isAdvanced()) {
            String rarity = item.getRarity(vminus$itemStack).toString().toLowerCase();

            if (!rarity.equals("common"))
                list.add(Component.translatable("rarity." + rarity).withStyle(ChatFormatting.DARK_GRAY));

            if (player != null && player.getAbilities().instabuild) {
                list.add((Component.literal(ForgeRegistries.ITEMS.getKey(vminus$itemStack.getItem()).toString())).withStyle(ChatFormatting.DARK_GRAY));
                if (vminus$itemStack.hasTag()) {
                    if (tag != null) {
                        String json = tag.toString();
                        Component nbtText = Component.literal(json).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
                        list.add(nbtText);
                    }
                }
            }
        }

        cir.setReturnValue(list);
    }
}
