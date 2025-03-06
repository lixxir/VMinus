package net.lixir.vminus.mixins.client;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.core.visions.CreativeTabVision;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.gui.CreativeTabsScreenPage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeTabsScreenPage.class)
public abstract class CreativeTabsScreenPageMixin {
    @Inject(method = "getVisibleTabs", at = @At(value = "RETURN"), cancellable = true,  remap = false)
    private void getVisibleTabs(CallbackInfoReturnable<List<CreativeModeTab>> cir) {
        List<CreativeModeTab> originalTabs = cir.getReturnValue();

        List<CreativeModeTab> filteredTabs = new ArrayList<>();
        for (CreativeModeTab tab : originalTabs) {
            Boolean hide = CreativeTabVision.getVision(tab).hide.value();
            if (hide != null && hide)
                continue;
            filteredTabs.add(tab);
        }
        cir.setReturnValue(filteredTabs);
    }
}
