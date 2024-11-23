package net.lixir.vminus.mixins.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.entity.animal.Animal.class)
public class AnimalMixin {
    @Inject(method = "spawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
    public void spawnChildFromBreeding(ServerLevel serverLevel, net.minecraft.world.entity.animal.Animal otherParent, CallbackInfo ci) {
        net.minecraft.world.entity.animal.Animal thisParent = (net.minecraft.world.entity.animal.Animal) (Object) this;

        AgeableMob child = otherParent.getBreedOffspring(serverLevel, thisParent);
        if (child != null) {

            child.setBaby(true);
            child.moveTo(otherParent.getX(), otherParent.getY(), otherParent.getZ(), 0.0F, 0.0F);

            String thisParentVariant = thisParent.getPersistentData().getString("variant");
            String otherParentVariant = otherParent.getPersistentData().getString("variant");

            String childVariant = serverLevel.random.nextBoolean() ? thisParentVariant : otherParentVariant;

            child.getPersistentData().putString("variant", childVariant);

            thisParent.finalizeSpawnChildFromBreeding(serverLevel, otherParent, child);
            serverLevel.addFreshEntityWithPassengers(child);
        }
        ci.cancel();
    }
}
