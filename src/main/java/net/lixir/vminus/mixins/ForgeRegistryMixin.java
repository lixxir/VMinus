package net.lixir.vminus.mixins;

import net.minecraftforge.registries.ForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ForgeRegistry.class)
public class ForgeRegistryMixin<V> {
    /*
    @Inject(method = "getValues", at = @At("RETURN"), cancellable = true, remap = false)
    private void vminus$getValues(CallbackInfoReturnable<Collection<V>> cir) {
        Collection<V> original = cir.getReturnValue();

        Collection<V> filtered = original.stream()
                .filter(value -> {
                    if (value instanceof Item item) {
                        Boolean ban = ItemVision.of(item).ban.value(new VisionContext(item));
                        return ban == null || !ban;
                    }
                    return true;
                })
                .toList();

        cir.setReturnValue(filtered);
    }

     */
}
