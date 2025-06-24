package net.lixir.vminus.mixins.server;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.resource.manager.VisionManager;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {

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
