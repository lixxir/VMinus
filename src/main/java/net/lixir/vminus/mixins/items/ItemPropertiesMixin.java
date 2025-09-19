package net.lixir.vminus.mixins.items;

import net.lixir.vminus.world.item.IMaxDurationGetter;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemProperties.class)
public class ItemPropertiesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void vminus$classInit(CallbackInfo ci) {
        ItemProperties.register(Items.BOW, new ResourceLocation("pull"), (stack, level, entity, seed) -> {
            if (entity == null) return 0.0F;
            return stack != entity.getUseItem() ? 0.0F :
                    (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / ((IMaxDurationGetter) stack.getItem()).vminus$getMaxDuration();
        });

    }
}

