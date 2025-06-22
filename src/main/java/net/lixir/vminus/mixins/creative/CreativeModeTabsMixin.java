package net.lixir.vminus.mixins.creative;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    /*
    @Inject(method = "allTabs", at = @At(value = "RETURN"), cancellable = true)
    private static void allTabs(CallbackInfoReturnable<List<CreativeModeTab>> cir) {

        List<CreativeModeTab> originalTabs = cir.getReturnValue();
        ArrayList<CreativeModeTab> newTabs = new ArrayList<>();
        for (CreativeModeTab tab : originalTabs) {
            Boolean hide = CreativeTabVision.of(tab).hide.value();
            if (hide != null && hide)
                continue;
            newTabs.add(tab);
        }
        cir.setReturnValue(new ArrayList<>(newTabs));
    }

    @Inject(method = "tabs", at = @At(value = "RETURN"), cancellable = true)
    private static void tabs(CallbackInfoReturnable<List<CreativeModeTab>> cir) {
        List<CreativeModeTab> originalTabs = cir.getReturnValue();
        ArrayList<CreativeModeTab> newTabs = new ArrayList<>();
        for (CreativeModeTab tab : originalTabs) {
            Boolean hide = CreativeTabVision.of(tab).hide.value();
            if (hide != null && hide)
                continue;
            newTabs.add(tab);
        }
        cir.setReturnValue(new ArrayList<>(newTabs));
    }

    @Inject(method = "streamAllTabs", at = @At(value = "RETURN"), cancellable = true)
    private static void streamAllTabs(CallbackInfoReturnable<Stream<CreativeModeTab>> cir) {
        List<CreativeModeTab> originalTabs = cir.getReturnValue().toList();
        ArrayList<CreativeModeTab> newTabs = new ArrayList<>();
        for (CreativeModeTab tab : originalTabs) {
            Boolean hide = CreativeTabVision.of(tab).hide.value();
            if (hide != null && hide)
                continue;
            newTabs.add(tab);
        }
        cir.setReturnValue(newTabs.stream());
    }


     */
}
