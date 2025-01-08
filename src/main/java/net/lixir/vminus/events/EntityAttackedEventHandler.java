package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber
public class EntityAttackedEventHandler {
    private static boolean isModLoaded(String modId) {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            LevelAccessor world = event.getEntity().level;
            DamageSource damagesource = event.getSource();
            Entity entity = event.getEntity();
            Entity sourceentity = event.getSource().getEntity();
            Entity immediatesourceentity = event.getSource().getDirectEntity();
            double amount = event.getAmount();

            if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null)
                return;

            List<ResourceLocation> particles = new ArrayList<ResourceLocation>();
            ItemStack mainhand = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
            double vX = 0;
            double vY = 0;
            double vZ = 0;
            double ranX = 0;
            double ranZ = 0;
            double ranY = 0;
            Entity directEntity = null;

            if (entity.getType().is(TagKey.create(ForgeRegistries.ENTITIES.getRegistryKey(), new ResourceLocation("vminus:invincible")))) {
                entity.invulnerableTime = 20;
                if (event.isCancelable()) {
                    event.setCanceled(true);
                }
            }

            if (entity instanceof Horse) {
                if (entity.isAttackable()) {
                    LivingEntity _entGetArmor = (LivingEntity) entity;
                    ItemStack horseArmor = _entGetArmor.getItemBySlot(EquipmentSlot.CHEST);
                    if (!(horseArmor.getItem() == ItemStack.EMPTY.getItem())) {
                        if (horseArmor.isDamageableItem()) {
                            if (horseArmor.hurt(1, new Random(), null)) {
                                horseArmor.shrink(1);
                                horseArmor.setDamageValue(0);
                            }
                        }
                    }
                }
            }

            if ((sourceentity instanceof Player _plr ? _plr.getAttackStrengthScale(0) : 0) == 1) {
                if (!(mainhand.getItem() == ItemStack.EMPTY.getItem())) {
                    if (mainhand.getOrCreateTag().getBoolean("stun")) {
                        new Object() {
                            void timedLoop(int current, int total, int ticks) {
                                entity.makeStuckInBlock(Blocks.AIR.defaultBlockState(), new Vec3(0.25, 0.05, 0.25));
                                final int tick2 = ticks;
                                VMinusMod.queueServerWork(tick2, () -> {
                                    if (total > current + 1) {
                                        timedLoop(current + 1, total, tick2);
                                    }
                                });
                            }
                        }.timedLoop(0, 10, 1);
                    }
                }
            }

            if (!(entity instanceof Player _plr && _plr.getAbilities().instabuild) && entity.isAlive() && sourceentity.isAlive() && entity instanceof LivingEntity) {
                mainhand = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
                directEntity = damagesource.getDirectEntity();
                if (!mainhand.isEmpty() && mainhand.isEnchanted()) {
                    if (((isModLoaded("detour") && (sourceentity instanceof Player _plr ? _plr.getAttackStrengthScale(0) : 0) >= 0.75) || !isModLoaded("detour"))
                            || !(sourceentity instanceof Player) || directEntity == null
                            && !(directEntity == (damagesource.getEntity())) && entity.isAttackable()) {
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(mainhand);
                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                            Enchantment enchantment = entry.getKey();
                            JsonObject visionData = VisionHandler.getVisionData(enchantment);
                            if (visionData != null) {
                                if (visionData.has("particle")) {
                                    String particleString = VisionValueHandler.getFirstValidString(visionData, "particle");
                                    if (particleString != null && !particleString.isEmpty()) {
                                        ResourceLocation particleLocation = new ResourceLocation(particleString);
                                        particles.add(particleLocation);
                                    }
                                }
                                if (visionData.has("sound")) {
                                    String soundString = VisionValueHandler.getFirstValidString(visionData, "sound");
                                    if (!world.isClientSide() && soundString != null)
                                        world.playSound(null, new BlockPos(sourceentity.getX(),
                                                        sourceentity.getY() + 1,
                                                        sourceentity.getZ()),
                                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundString))),
                                                SoundSource.PLAYERS,
                                                0.8f,
                                                1.0f);
                                }
                            }
                        }
                    }

                    if (!particles.isEmpty()) {
                        Random rand = new Random();
                        for (int index0 = 0; index0 < rand.nextInt(3) + 5; index0++) {
                            ResourceLocation chosenParticle = particles.get(rand.nextInt(particles.size()));
                            ranX = entity.getX() + rand.nextDouble() * (entity.getBbWidth() / 2) * (-1) - 0.3;
                            ranY = entity.getY() + rand.nextDouble() * entity.getBbHeight() + 0.3;
                            ranZ = entity.getZ() + rand.nextDouble() * (entity.getBbWidth() / 2) * (-1) - 0.3;
                            vX = rand.nextDouble() * 0.16 - 0.08;
                            vY = rand.nextDouble() * 0.16 - 0.08;
                            vZ = rand.nextDouble() * 0.16 - 0.08;
                            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(chosenParticle);
                            if (particleType instanceof SimpleParticleType simpleParticleType) {
                                world.addParticle(simpleParticleType, ranX, ranY, ranZ, vX, vY, vZ);
                            }
                        }
                    }
                }
            }
        }
    }
}
