package net.lixir.vminus.mixins.enchantments.functions;

import net.lixir.vminus.procedures.IsBannedEnchantmentProcedure;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"), cancellable = true)
    private static void getAvailableEnchantmentResults(int p_44818_, ItemStack stack, boolean p_44820_, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> enchantmentResults = cir.getReturnValue();
        enchantmentResults = enchantmentResults.stream().filter(instance -> !IsBannedEnchantmentProcedure.execute(ForgeRegistries.ENCHANTMENTS.getKey(instance.enchantment).toString())).collect(Collectors.toList());
        cir.setReturnValue(enchantmentResults);
    }

    /**
     * @author lixir
     * @reason To make getBlockEfficiency always return 0
     * in favor of adding attribute modifiers
     */
    @Overwrite
    public static int getBlockEfficiency(LivingEntity entity) {
        return 0;
    }
}
