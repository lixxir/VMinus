package net.lixir.vminus.events.entity.living;

import net.lixir.vminus.world.item.IEquipmentItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber
public class LivingTickEventHandler {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.@NotNull LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;
        Level level = entity.level();
        if (entity.isSpectator())
            return;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack itemStack = entity.getItemBySlot(slot);
                if (itemStack.isEmpty() || !(itemStack.getItem() instanceof IEquipmentItem IEquipmentItem))
                    continue;
                IEquipmentItem.onEquipmentTick(level, entity, itemStack);
            }
        }




        if (level instanceof ServerLevel serverlevel) {
            if (level.getLevelData().getGameTime() % 4 == 0) {
                if (!entity.isSpectator()) {
                    Collection<MobEffectInstance> activeEffects = entity.getActiveEffects();
                    List<String> visibleEffects = getVisibleEffects(activeEffects);
                    if (!visibleEffects.isEmpty()) {
                        String currentEffect = getRandomEffect(visibleEffects);
                        double[] spawnCoordinates = getSpawnCoordinates(entity);
                        spawnParticleForEffect(currentEffect, serverlevel, spawnCoordinates, entity);
                    }
                }
            }
        }
    }

    private static @NotNull List<String> getVisibleEffects(Collection<MobEffectInstance> activeEffects) {
        List<String> effects = new ArrayList<>();
        for (MobEffectInstance effectInstance : activeEffects) {
            if (effectInstance.isVisible()) {
                ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effectInstance.getEffect());
                effects.add(effectId.toString());
            }
        }
        return effects;
    }

    private static String getRandomEffect(@NotNull List<String> effects) {
        return effects.get(Mth.nextInt(RandomSource.create(), 0, effects.size() - 1));
    }

    private static double @NotNull [] getSpawnCoordinates(@NotNull Entity entity) {
        double spawnX = entity.getX() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * -1 - 0.3, entity.getBbWidth() / 2 + 0.3);
        double spawnY = entity.getY() + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight());
        double spawnZ = entity.getZ() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * -1 - 0.3, entity.getBbWidth() / 2 + 0.3);
        return new double[]{spawnX, spawnY, spawnZ};
    }

    private static void spawnParticleForEffect(String effect, ServerLevel level, double[] spawnCoords, Entity entity) {
        /*
        ResourceLocation effectLocation = new ResourceLocation(effect);
        MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectLocation);
        if (mobEffect == null) {
            return;
        }
        JsonObject visionData = Visions.getData(mobEffect);
        String effectString = VisionProperties.getString(visionData, VisionProperties.Names.PARTICLE, mobEffect);

        if (effectString != null) {
            ResourceLocation particleLocation = new ResourceLocation(effectString);
            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(particleLocation);
            if (particleType instanceof SimpleParticleType simpleParticleType) {
                level.sendParticles(simpleParticleType, spawnCoords[0], spawnCoords[1], spawnCoords[2], 1, 0, 0, 0, Mth.nextDouble(RandomSource.create(), 0.01, 0.03));
            }
        }

         */
    }
}

