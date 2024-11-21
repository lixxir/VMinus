package net.lixir.vminus.mixins;

import net.lixir.vminus.procedures.IsBannedEnchantmentProcedure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(EnchantWithLevelsFunction.class)
public abstract class EnchantWithLevelsMixin extends LootItemConditionalFunction {
    protected EnchantWithLevelsMixin() {
        super(new net.minecraft.world.level.storage.loot.predicates.LootItemCondition[0]);
    }

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void injectRunCheck(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        RandomSource randomSource = context.getRandom();
        EnchantWithLevelsFunction enchantWithLevelsFunction = (EnchantWithLevelsFunction) (Object) this;
        int levels = getLevels(enchantWithLevelsFunction, context);
        boolean treasure = getTreasure(enchantWithLevelsFunction);
        ItemStack enchantedStack = EnchantmentHelper.enchantItem(randomSource, stack, levels, treasure);
        CompoundTag tag = enchantedStack.getOrCreateTag();
        int enchLimit = tag.contains("enchantment_limit") ? tag.getInt("enchantment_limit") : 999;
        double currentTotalEnchantmentLevel = 0.0;
        if (enchantedStack.isEnchanted()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(enchantedStack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                currentTotalEnchantmentLevel += entry.getValue();
            }
        }
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(enchantedStack);
        List<Enchantment> bannedEnchantments = enchantments.keySet().stream().filter(enchantment -> IsBannedEnchantmentProcedure.execute(enchantment.getDescriptionId())).collect(Collectors.toList());
        for (Enchantment bannedEnchantment : bannedEnchantments) {
            enchantments.remove(bannedEnchantment);
        }
        ItemStack finalStack = new ItemStack(stack.getItem(), stack.getCount());
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            int level = entry.getValue();
            if (currentTotalEnchantmentLevel > enchLimit) {
                level = (int) Math.max(0, enchLimit - currentTotalEnchantmentLevel);
                currentTotalEnchantmentLevel = enchLimit;
            }
            finalStack.enchant(entry.getKey(), level);
        }
        cir.setReturnValue(finalStack);
    }

    private int getLevels(EnchantWithLevelsFunction function, LootContext context) {
        try {
            Field levelsField = EnchantWithLevelsFunction.class.getDeclaredField("levels");
            levelsField.setAccessible(true);
            NumberProvider levelsProvider = (NumberProvider) levelsField.get(function);
            return levelsProvider.getInt(context);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return 1; // default level
        }
    }

    private boolean getTreasure(EnchantWithLevelsFunction function) {
        try {
            Field treasureField = EnchantWithLevelsFunction.class.getDeclaredField("treasure");
            treasureField.setAccessible(true);
            return treasureField.getBoolean(function);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false; // default treasure value
        }
    }
}
