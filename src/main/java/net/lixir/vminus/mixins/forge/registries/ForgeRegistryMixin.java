package net.lixir.vminus.mixins.forge.registries;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ForgeRegistry.class)
public abstract class ForgeRegistryMixin<V> {
    @Shadow public abstract boolean isEmpty();

    @Inject(method = "getValues", at = @At("RETURN"), cancellable = true, remap = false)
    private void vminus$getValues(@NotNull CallbackInfoReturnable<Collection<V>> cir) {
        Collection<V> original = cir.getReturnValue();
        Collection<V> filtered = original.stream()
                .filter(value -> {
                    if (value instanceof Item item) {
                        Boolean ban = VisionUtils.getOverrideValue((VisionDuck) item, VisionProperties.Items.BAN, new VisionContext(item));
                        return ban == null || !ban;
                    }
                    return true;
                })
                .toList();

        cir.setReturnValue(filtered);
    }
}
