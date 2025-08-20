package net.lixir.vminus.mixins.forge.registries;

import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Optional;

@Mixin(ForgeRegistry.class)
public abstract class ForgeRegistryMixin<V> {
    @Shadow public abstract boolean isEmpty();

    @Shadow
    public abstract ResourceLocation getKey(V value);



    @Inject(method = "getValues", at = @At("RETURN"), cancellable = true, remap = false)
    private void vMinus$getValues(@NotNull CallbackInfoReturnable<Collection<V>> cir) {
        Collection<V> original = cir.getReturnValue();
        Collection<V> filtered = original.stream()
                .filter(value -> {
                    if (value instanceof Item item) {
                        return VisionUtils.isBanned(item);
                    }
                    return true;
                })
                .toList();

        if (!filtered.equals(original))
            cir.setReturnValue(filtered);
    }

}
