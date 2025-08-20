package net.lixir.vminus.mixins.data;

import net.lixir.vminus.resources.data.RegistryAccessHolder;
import net.lixir.vminus.resources.data.bans.BannedRecipeManager;
import net.lixir.vminus.resources.data.vision.VisionManager;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagManager;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {

    @Shadow @Final private ICondition.IContext context;

    @Shadow @Final private TagManager tagManager;
    @Shadow @Final private RecipeManager recipes;

    @Inject(method = "<init>", at = @At("TAIL"))

    private void vminus$attachRegistryAccess(RegistryAccess.Frozen frozen, FeatureFlagSet flags,
                                             Commands.CommandSelection selection, int funcLevel,
                                             CallbackInfo ci) {
        ((RegistryAccessHolder) this.recipes).vMinus$setRegistryAccess(frozen);
    }


    @Inject(method = "listeners", at = @At("RETURN"), cancellable = true)
    private void vMinus$addListeners(@NotNull CallbackInfoReturnable<List<PreparableReloadListener>> cir) {
        List<PreparableReloadListener> vanillaListeners = new ArrayList<>(cir.getReturnValue());
        vanillaListeners.remove(tagManager);
        List<PreparableReloadListener> newListeners = new ArrayList<>();
        newListeners.add(BannedRecipeManager.INSTANCE);
        newListeners.add(tagManager);
        newListeners.add(new VisionManager<>(VisionTypes.ITEM, BuiltInRegistries.ITEM, context));
        newListeners.add(new VisionManager<>(VisionTypes.BLOCK, BuiltInRegistries.BLOCK, context));
        newListeners.add(new VisionManager<>(VisionTypes.ENTITY, BuiltInRegistries.ENTITY_TYPE, context));
        newListeners.add(new VisionManager<>(VisionTypes.TAB, BuiltInRegistries.CREATIVE_MODE_TAB, context));
        newListeners.add(new VisionManager<>(VisionTypes.ENCHANTMENT, BuiltInRegistries.ENCHANTMENT, context));
        newListeners.add(new VisionManager<>(VisionTypes.EFFECT, BuiltInRegistries.MOB_EFFECT, context));
        newListeners.addAll(vanillaListeners);
        cir.setReturnValue(newListeners);
    }

    @Inject(method = "loadResources", at = @At("HEAD"))
    private static void vminus$onReloadStart(ResourceManager resourceManager,
                                             RegistryAccess.Frozen registryAccess,
                                             FeatureFlagSet featureFlags,
                                             Commands.CommandSelection commandSelection,
                                             int functionPermissionLevel,
                                             Executor backgroundExecutor,
                                             Executor gameExecutor,
                                             CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {

        Vision.resetVisions();
    }
}
