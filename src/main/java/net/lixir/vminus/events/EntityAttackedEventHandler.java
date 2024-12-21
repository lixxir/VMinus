package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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

@Mod.EventBusSubscriber
public class EntityAttackedEventHandler {
    private static boolean isModLoaded(String modId) {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {

            LevelAccessor world = event.getEntity().level();
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

            // Prevent invincible tagged entities from being hurt
            if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("vminus:invincible")))) {
                entity.invulnerableTime = 20;
                if (event != null && event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event != null && event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }

            // Damage horse armor when attacked if it has durability
            if (entity instanceof Horse) {
                if (entity.isAttackable()) {
                    ItemStack horseArmor = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY);
                    if (!(horseArmor.getItem() == ItemStack.EMPTY.getItem())) {
                        if (horseArmor.isDamageableItem()) {
                            {
                                ItemStack _ist = horseArmor;
                                if (_ist.hurt(1, RandomSource.create(), null)) {
                                    _ist.shrink(1);
                                    _ist.setDamageValue(0);
                                }
                            }
                        }
                    }
                }
            }
            // Stun hit entities if they have a certain nbt.
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
            // Enchantment particles
            if (sourceentity != null && !(entity instanceof Player _plr && _plr.getAbilities().instabuild) && entity.isAlive() && sourceentity.isAlive() && entity instanceof LivingEntity) {
                mainhand = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
                directEntity = damagesource.getDirectEntity();
                if (!mainhand.isEmpty() && mainhand.isEnchanted()) {
                    if (((isModLoaded("detour") && (sourceentity instanceof Player _plr ? _plr.getAttackStrengthScale(0) : 0) >= 0.75) || !isModLoaded("detour"))
                            || !(sourceentity instanceof Player) || directEntity == null
                            && !(directEntity == (damagesource.getEntity())) && entity.isAttackable()) {
                        // iterating through all of the items enchants
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(mainhand);
                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                            Enchantment enchantment = entry.getKey();
                            // getting the vision data from the enchantment
                            JsonObject visionData = VisionHandler.getVisionData(enchantment);
                            if (visionData != null) {
                                if (visionData.has("particle")) {
                                    // getting the string and resource location to add to the particle list
                                    String particleString = VisionValueHelper.getFirstValidString(visionData, "particle");
                                    if (!particleString.isEmpty() && particleString != null) {
                                        ResourceLocation particleLocation = new ResourceLocation(particleString);
                                        if (particleLocation != null)
                                            particles.add(particleLocation);
                                    }
                                }
                                if (visionData.has("sound")) {
                                    String soundString = VisionValueHelper.getFirstValidString(visionData, "sound");
                                    if (!world.isClientSide())
                                        world.playSound(null, BlockPos.containing(sourceentity.getX(),
                                                        sourceentity.getY() + 1,
                                                        sourceentity.getZ()),
                                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundString))),
                                                SoundSource.PLAYERS,
                                                (float) 0.8,
                                                (float) 1);
                                }
                            }
                        }
                    }
                    // spawning random particles from the created list
                    if (particles.size() > 0) {
                        for (int index0 = 0; index0 < Mth.nextInt(RandomSource.create(), 5, 7); index0++) {
                            ResourceLocation chosenParticle = particles.get(Mth.nextInt(RandomSource.create(), 0, (particles.size() - 1)));
                            // getting random positions
                            ranX = entity.getX() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * (-1) - 0.3, entity.getBbWidth() / 2 + 0.3);
                            ranY = entity.getY() + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight() + 0.3);
                            ranZ = entity.getZ() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * (-1) - 0.3, entity.getBbWidth() / 2 + 0.3);
                            // getting random velocities
                            vX = Mth.nextDouble(RandomSource.create(), -0.08, 0.08);
                            vY = Mth.nextDouble(RandomSource.create(), -0.08, 0.08);
                            vZ = Mth.nextDouble(RandomSource.create(), -0.08, 0.08);
                            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(chosenParticle);
                            if (particleType instanceof SimpleParticleType simpleParticleType && simpleParticleType != null) {
                                //world.sendParticles(simpleParticleType, ranX, ranY, ranZ, 1, 0, 0, 0, Mth.nextDouble(RandomSource.create(), 0.01, 0.03));
                                world.addParticle(simpleParticleType, ranX, ranY, ranZ, vX, vY, vZ);
                            }
                        }
                    }


                }
            }
        }
    }
}
