package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.registry.util.VMinusTags;
import net.lixir.vminus.util.AttributeHelper;
import net.lixir.vminus.util.ISpeedGetter;
import net.lixir.vminus.core.Visions;
import net.lixir.vminus.core.VisionProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
        handleTranslucency(entity, nbt);
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

            handleSprintMomentum(entity, nbt);
        }
    }

    private static void handleSprintMomentum(LivingEntity entity, CompoundTag nbt) {
        AttributeInstance speedAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute == null) return;

        if (entity instanceof ISpeedGetter speedGetter) {
            double speed = Math.sqrt(speedGetter.vminus$getSpeed());
            double momentumCap = getMomentumCap(entity);

            double storedMomentum = nbt.getDouble(VMinusAttributes.MOMENTUM_NBT_KEY);
            double newMomentum = getNewMomentum(storedMomentum, speed, momentumCap);

            if (Math.abs(storedMomentum - newMomentum) > EPSILON) {
                nbt.putDouble(VMinusAttributes.MOMENTUM_NBT_KEY, newMomentum);
            }

            speedAttribute.removeModifier(MOMENTUM_SPEED_MODIFIER_UUID);
            if (newMomentum > 0) {
                AttributeModifier speedModifier = new AttributeModifier(
                        MOMENTUM_SPEED_MODIFIER_UUID, VMinusAttributes.MOMENTUM_SPEED_NAME,
                        newMomentum, AttributeModifier.Operation.ADDITION
                );
                speedAttribute.addTransientModifier(speedModifier);
            }
        }
    }

    private static double getNewMomentum(double storedMomentum, double speed, double momentumCap) {
        double newMomentum;

        if (speed > 0.005f) {
            newMomentum = Math.min(storedMomentum + VMinusAttributes.MOMENTUM_BUILDUP_RATE, momentumCap);
        } else {
            newMomentum = Math.max(0, storedMomentum - VMinusAttributes.MOMENTUM_DECAY_RATE);
        }

        newMomentum = Math.round(newMomentum * 1_000_000) / 1_000_000.0;

        if (Math.abs(newMomentum) < EPSILON) {
            newMomentum = 0;
        }
        return newMomentum;
    }


    private static void handleTranslucency(LivingEntity entity, CompoundTag nbt) {
        if (entity.getType().is(VMinusTags.Entities.IGNORES_TRANSLUCENCE))
            return;
        float translucency = AttributeHelper.getAttributesFromArmor(entity, VMinusAttributes.TRANSLUCENCE.get());

        if (entity.isSprinting())
            translucency *= 0.75f;
        if (entity.isCrouching())
            translucency *= 2f;
        if (!entity.onGround())
            translucency *= 0.9f;

        double speed = 0;
        if (entity instanceof ISpeedGetter speedGetter) {
            speed = Math.max(0, Math.sqrt(speedGetter.vminus$getSpeed()) - 0.02f);
        }

        if (speed < 0.1f) {
            translucency *= 1.3f;
        }

        translucency = Mth.clamp(translucency, 0f, 1f);

        if (translucency > 0.999f) {
            translucency = 1.0f;
        } else if (translucency < 0.01f) {
            translucency = 0.0f;
        }

        float storedTranslucency = nbt.getFloat(VMinusAttributes.TRANSLUCENCE_KEY);
        float changedTranslucency;

        if (Math.abs(storedTranslucency - translucency) > EPSILON) {
            if (storedTranslucency > translucency) {
                changedTranslucency = Math.max(translucency, storedTranslucency - VMinusAttributes.TRANSLUCENCY_RATE);
            } else {
                changedTranslucency = Math.min(translucency, storedTranslucency + VMinusAttributes.TRANSLUCENCY_RATE);
            }

            changedTranslucency = Mth.clamp(changedTranslucency, 0f, 1f);

            if (changedTranslucency > 0.999f) {
                changedTranslucency = 1.0f;
            } else if (changedTranslucency < 0.01f) {
                changedTranslucency = 0.0f;
            }

            nbt.putFloat(VMinusAttributes.TRANSLUCENCE_KEY, changedTranslucency);
        }
    }

    private static double getMomentumCap(LivingEntity entity) {
        double totalMomentum = 0.0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (ItemStack stack : entity.getArmorSlots()) {
                if (stack.getAttributeModifiers(slot).containsKey(VMinusAttributes.MOMENTUM.get())) {
                    totalMomentum += stack.getAttributeModifiers(slot)
                            .get(VMinusAttributes.MOMENTUM.get())
                            .stream()
                            .mapToDouble(AttributeModifier::getAmount)
                            .sum();
                }
            }
        }
        return totalMomentum * 0.08;
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
            if (particleType instanceof SimpleParticleType simpleParticleType && simpleParticleType != null) {
                world.sendParticles(simpleParticleType, spawnCoords[0], spawnCoords[1], spawnCoords[2], 1, 0, 0, 0, Mth.nextDouble(RandomSource.create(), 0.01, 0.03));
            }
        }
    }// else {
    //	int color = effectInstance.getEffect().getColor();
    //	boolean isAmbient = effectInstance.isAmbient();
    ////	spawnEffectParticles(color, isAmbient, world, x, y, z);
    //}
}

