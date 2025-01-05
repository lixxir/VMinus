package net.lixir.vminus.mixins.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.common.MinecraftForge;


@Mixin(Animal.class)
public class AnimalMixin {
    @Unique
    private final Animal vminus$animal = (Animal) (Object) this;

    @Inject(method = "spawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
    public void spawnChildFromBreeding(ServerLevel p_27564_, Animal p_27565_, CallbackInfo ci) {
        AgeableMob ageablemob = vminus$animal.getBreedOffspring(p_27564_, p_27565_);

        BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(vminus$animal, p_27565_, ageablemob);
        boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
        ageablemob = event.getChild();

        if (cancelled) {
            vminus$animal.setAge(6000);
            p_27565_.setAge(6000);
            vminus$animal.resetLove();
            p_27565_.resetLove();
        } else {
            if (ageablemob != null) {
                ServerPlayer serverplayer = vminus$animal.getLoveCause();
                if (serverplayer == null && p_27565_.getLoveCause() != null) {
                    serverplayer = p_27565_.getLoveCause();
                }

                String thisParentVariant = vminus$animal.getPersistentData().getString("variant");
                String otherParentVariant = p_27565_.getPersistentData().getString("variant");
                String childVariant = p_27564_.random.nextBoolean() ? thisParentVariant : otherParentVariant;
                ageablemob.getPersistentData().putString("variant", childVariant);

                if (serverplayer != null) {
                    serverplayer.awardStat(Stats.ANIMALS_BRED);
                    CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, vminus$animal, p_27565_, ageablemob);
                }

                vminus$animal.setAge(6000);
                p_27565_.setAge(6000);
                vminus$animal.resetLove();
                p_27565_.resetLove();

                ageablemob.setBaby(true);
                ageablemob.moveTo(vminus$animal.getX(), vminus$animal.getY(), vminus$animal.getZ(), 0.0F, 0.0F);

                p_27564_.addFreshEntityWithPassengers(ageablemob);
                p_27564_.broadcastEntityEvent(vminus$animal, (byte) 18);

                if (p_27564_.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    p_27564_.addFreshEntity(new ExperienceOrb(p_27564_, vminus$animal.getX(), vminus$animal.getY(), vminus$animal.getZ(), vminus$animal.getRandom().nextInt(7) + 1));
                }
            }
        }
        ci.cancel();
    }
}


