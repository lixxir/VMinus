package net.lixir.vminus.mixins;

import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;

@Mixin(ForgeRegistry.class)
public class ForgeRegistryMixin<V> {
    @Inject(method = "getValues", at = @At("RETURN"), cancellable = true, remap = false)
    private void vminus$getValues(CallbackInfoReturnable<Collection<V>> cir) {
        Collection<V> original = cir.getReturnValue();

        Collection<V> filtered = original.stream()
                .filter(value -> {
                    if (value instanceof Item item) {
                        Boolean ban = ItemVision.of(item).ban.value(new VisionConditionArguments(item));
                        return ban == null || !ban;
                    }
                    return true;
                })
                .toList();

        cir.setReturnValue(filtered);
    }
}
