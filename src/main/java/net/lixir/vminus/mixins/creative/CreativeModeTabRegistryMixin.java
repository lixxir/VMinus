package net.lixir.vminus.mixins.creative;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.CreativeModeTabRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeModeTabRegistry.class)
public abstract class CreativeModeTabRegistryMixin {
    @Inject(method = "getSortedCreativeModeTabs", at = @At(value = "RETURN"), cancellable = true,  remap = false)
    private static void getSortedCreativeModeTabs(CallbackInfoReturnable<List<CreativeModeTab>> cir) {
        List<CreativeModeTab> originalTabs = cir.getReturnValue();
        ArrayList<CreativeModeTab> newTabs = new ArrayList<>();
        for (CreativeModeTab tab : originalTabs) {
            Boolean hide = VisionUtils.getOverrideValue(((VisionDuck) tab), VisionProperties.Tabs.HIDE, null);
            if (hide != null && hide)
                continue;
            newTabs.add(tab);
        }
        if (!newTabs.equals(cir.getReturnValue()))
            cir.setReturnValue(new ArrayList<>(newTabs));
    }
}
