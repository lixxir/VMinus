package net.lixir.vminus.events;

import net.lixir.vminus.item.trait.ItemTraits;
import net.lixir.vminus.item.trait.ItemTrait;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class LivingTickEventHandler {
    private static final UUID MOMENTUM_SPEED_MODIFIER_UUID = UUID.fromString("12345678-1234-1234-1234-123456789abc");
    private static final float EPSILON = 1.0E-6F;

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;
        LevelAccessor world = event.getEntity().level();
        CompoundTag nbt = entity.getPersistentData();
        if (world instanceof ServerLevel serverWorld) {
            if (world.getLevelData().getGameTime() % 4 == 0) {
                if (!isSpectator(entity)) {
                    Collection<MobEffectInstance> activeEffects = entity.getActiveEffects();
                    List<String> visibleEffects = getVisibleEffects(activeEffects);
                    if (!visibleEffects.isEmpty()) {
                        String currentEffect = getRandomEffect(visibleEffects);
                        double[] spawnCoordinates = getSpawnCoordinates(entity);
                        spawnParticleForEffect(currentEffect, serverWorld, spawnCoordinates, entity);
                    }
                }
            }
        }
        for (ItemStack armorStack : entity.getArmorSlots()) {
            for (ItemTrait itemTrait : ItemTraits.getTraits(armorStack)) {
                if (itemTrait.armorTick(armorStack, entity, world))
                    if (event.isCancelable())
                        event.setCanceled(true);
            }
        }

    }



    private static boolean isSpectator(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            GameType gameMode = serverPlayer.gameMode.getGameModeForPlayer();
            return gameMode == GameType.SPECTATOR;
        } else if (entity.level().isClientSide() && entity instanceof Player player) {
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
                effects.add(effectId.toString());
            }
        }
        return effects;
    }

    private static String getRandomEffect(List<String> effects) {
        return effects.get(Mth.nextInt(RandomSource.create(), 0, effects.size() - 1));
    }

    private static double[] getSpawnCoordinates(Entity entity) {
        double spawnX = entity.getX() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * -1 - 0.3, entity.getBbWidth() / 2 + 0.3);
        double spawnY = entity.getY() + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight());
        double spawnZ = entity.getZ() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * -1 - 0.3, entity.getBbWidth() / 2 + 0.3);
        return new double[]{spawnX, spawnY, spawnZ};
    }

    private static void spawnParticleForEffect(String effect, ServerLevel world, double[] spawnCoords, Entity entity) {
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
                world.sendParticles(simpleParticleType, spawnCoords[0], spawnCoords[1], spawnCoords[2], 1, 0, 0, 0, Mth.nextDouble(RandomSource.create(), 0.01, 0.03));
            }
        }

         */
    }
}

