package net.lixir.vminus.mixins.items;

import com.google.common.collect.Multimap;
import net.lixir.vminus.attribute.VMinusAttributes;
import net.lixir.vminus.sight.resource.SightManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin {
    @Unique
    private final DiggerItem vMinus$self = (DiggerItem) (Object) this;

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    public final void vMinus$getDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> ci) {
        if (!SightManager.get("mining_attributes"))
            return;
        DiggerItemAccessor accessor = (DiggerItemAccessor) vMinus$self;
        float newSpeed = 0;
        EquipmentSlot slot = EquipmentSlot.MAINHAND;
        Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(slot);
        for (Attribute attribute : modifiers.keySet()) {
            Collection<AttributeModifier> attributeModifiers = modifiers.get(attribute);
            for (AttributeModifier modifier : attributeModifiers) {
                if (attribute == VMinusAttributes.MINING_SPEED) {
                    newSpeed += (float) modifier.getAmount();
                }
            }
        }
        if (state.is(accessor.getBlocks())) {
            ci.setReturnValue(newSpeed != 0 ? newSpeed : accessor.getSpeed());
        } else {
            ci.setReturnValue(1.0F);
        }
    }
}
