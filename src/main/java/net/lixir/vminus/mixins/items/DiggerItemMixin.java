package net.lixir.vminus.mixins.items;

import com.google.common.collect.Multimap;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin {
    @Shadow
    private TagKey<Block> blocks;
    @Shadow
    private float speed;

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    public void getDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> ci) {
        DiggerItem item = (DiggerItem) (Object) this;
        float baseSpeed = item.getTier().getSpeed();
        float newSpeed = 0;
        EquipmentSlot slot = EquipmentSlot.MAINHAND;
        Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(slot);
        for (Attribute attribute : modifiers.keySet()) {
            Collection<AttributeModifier> attributeModifiers = modifiers.get(attribute);
            for (AttributeModifier modifier : attributeModifiers) {
                if (attribute == VMinusAttributes.MININGSPEED.get()) {
                    newSpeed += (float) modifier.getAmount();
                }
            }
        }
        if (state.is(blocks)) {
            ci.setReturnValue(newSpeed != 0 ? newSpeed : speed);
        } else {
            ci.setReturnValue(1.0F);
        }
    }
}
