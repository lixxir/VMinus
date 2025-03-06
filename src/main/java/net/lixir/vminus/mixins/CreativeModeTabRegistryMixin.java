package net.lixir.vminus.mixins;

import net.lixir.vminus.core.visions.CreativeTabVision;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.CreativeModeTabRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(CreativeModeTabRegistry.class)
public abstract class CreativeModeTabRegistryMixin {

    @Inject(method = "getSortedCreativeModeTabs", at = @At(value = "RETURN"), cancellable = true,  remap = false)
    private static void getSortedCreativeModeTabs(CallbackInfoReturnable<List<CreativeModeTab>> cir) {
        List<CreativeModeTab> originalTabs = cir.getReturnValue();
        ArrayList<CreativeModeTab> newTabs = new ArrayList<>();
        for (CreativeModeTab tab : originalTabs) {
            Boolean hide = CreativeTabVision.getVision(tab).hide.value();
            if (hide != null && hide)
                continue;
            newTabs.add(tab);
        }
        cir.setReturnValue(new ArrayList<>(newTabs));
    }
}
