package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class LivingTickEventHandler {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent event) {
        LevelAccessor world = event.getEntity().level;
        Entity entity = event.getEntity();
        if (entity == null || !(world instanceof ServerLevel serverWorld))
            return;
        if (world.getLevelData().getGameTime() % 4 == 0 && entity instanceof LivingEntity livingEntity) {
            if (!isSpectator(entity)) {
                Collection<MobEffectInstance> activeEffects = livingEntity.getActiveEffects();
                List<String> visibleEffects = getVisibleEffects(activeEffects);
                if (!visibleEffects.isEmpty()) {
                    String currentEffect = getRandomEffect(visibleEffects);
                    double[] spawnCoordinates = getSpawnCoordinates(entity);
                    spawnParticleForEffect(currentEffect, serverWorld, spawnCoordinates, entity);
                }
            }
        }
    }

    private static boolean isSpectator(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            GameType gameMode = serverPlayer.gameMode.getGameModeForPlayer();
            return gameMode == GameType.SPECTATOR;
        } else if (entity.level.isClientSide() && entity instanceof Player player) {
            var connection = Minecraft.getInstance().getConnection();
            if (connection != null) {
                var playerInfo = connection.getPlayerInfo(player.getGameProfile().getId());
                if (playerInfo != null) {
                    GameType gameMode = playerInfo.getGameMode();
                    return gameMode == GameType.SPECTATOR;
                }
            }
        }
        return false;
    }

    private static List<String> getVisibleEffects(Collection<MobEffectInstance> activeEffects) {
        List<String> effects = new ArrayList<>();
        for (MobEffectInstance effectInstance : activeEffects) {
            if (effectInstance.isVisible()) {
                ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effectInstance.getEffect());
                if (effectId != null)
                    effects.add(effectId.toString());
            }
        }
        return effects;
    }

    private static String getRandomEffect(List<String> effects) {
        Random random = new Random();
        return effects.get(random.nextInt(effects.size()));
    }

    private static double[] getSpawnCoordinates(Entity entity) {
        Random random = new Random();
        double spawnX = entity.getX() + Mth.nextDouble(random, (entity.getBbWidth() / 2) * -1 - 0.3, entity.getBbWidth() / 2 + 0.3);
        double spawnY = entity.getY() + Mth.nextDouble(random, 0, entity.getBbHeight());
        double spawnZ = entity.getZ() + Mth.nextDouble(random, (entity.getBbWidth() / 2) * -1 - 0.3, entity.getBbWidth() / 2 + 0.3);
        return new double[]{spawnX, spawnY, spawnZ};
    }

    private static void spawnParticleForEffect(String effect, ServerLevel world, double[] spawnCoords, Entity entity) {
        ResourceLocation effectLocation = new ResourceLocation(effect);
        MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectLocation);
        if (mobEffect == null) {
            return;
        }
        JsonObject visionData = VisionHandler.getVisionData(mobEffect);
        if (visionData != null && visionData.has("particle")) {
            String effectString = VisionValueHandler.getFirstValidString(visionData, "particle");
            if (effectString == null)
                return;
            ResourceLocation particleLocation = new ResourceLocation(effectString);
            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(particleLocation);
            if (particleType instanceof SimpleParticleType simpleParticleType) {
                Random random = new Random();  // Using Java's Random class
                world.sendParticles(simpleParticleType, spawnCoords[0], spawnCoords[1], spawnCoords[2], 1, 0, 0, 0, Mth.nextDouble(random, 0.01, 0.03));  // Replaced Mth.nextDouble with Random
            }
        }
    }
}