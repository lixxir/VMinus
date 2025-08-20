package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.resources.data.sight.SightManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Animal.class)
public class AnimalMixin {
    @Inject(method = "finalizeSpawnChildFromBreeding", at = @At("HEAD"))
    private void vMinus$finalizeSpawnChildFromBreeding(ServerLevel level, Animal otherParent, @Nullable AgeableMob child, CallbackInfo ci) {
        if (!SightManager.get("variant_breeding"))
            return;
        if (child != null) {
            child.getPersistentData();
            String thisVariant = ((Animal) (Object) this).getPersistentData().getString("variant");
            String otherVariant = otherParent.getPersistentData().getString("variant");

            String childVariant = level.random.nextBoolean() ? thisVariant : otherVariant;
            child.getPersistentData().putString("variant", childVariant);
        }
    }
}
