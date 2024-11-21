package net.lixir.vminus;

import com.google.gson.JsonObject;
import net.lixir.vminus.core.VisionHandler;
import net.lixir.vminus.core.VisionValueHelper;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class EnchantmentAttackParticles {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity(),
                    event.getAmount());
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
        execute(null, world, x, y, z, damagesource, entity, immediatesourceentity, sourceentity, amount);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
        if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null)
            return;
        List<ResourceLocation> particles = new ArrayList<ResourceLocation>();
        ItemStack mainhandItem = ItemStack.EMPTY;
        double vX = 0;
        double vY = 0;
        double vZ = 0;
        double ranX = 0;
        double ranZ = 0;
        double ranY = 0;
        Entity directEntity = null;
        if (sourceentity != null && !(entity instanceof Player _plr && _plr.getAbilities().instabuild) && entity.isAlive() && sourceentity.isAlive() && entity instanceof LivingEntity) {
            mainhandItem = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
            directEntity = damagesource.getDirectEntity();
            System.out.println(mainhandItem);
            if (!mainhandItem.isEmpty() && mainhandItem.isEnchanted()) {
                if ((sourceentity instanceof Player _plr ? _plr.getAttackStrengthScale(0) : 0) >= 0.75 || !(sourceentity instanceof Player) || directEntity == null && !(directEntity == (damagesource.getEntity()))) {
                    // iterating through all of the items enchants
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(mainhandItem);
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        Enchantment enchantment = entry.getKey();
                        System.out.println(enchantment);
                        // getting the vision data from the enchant
                        JsonObject visionData = VisionHandler.getVisionData(enchantment);
                        if (visionData != null && visionData.has("particle")) {
                            // getting the string and resource location to add to the particle list
                            String particleString = VisionValueHelper.getFirstValidString(visionData, "particle");
                            if (!particleString.isEmpty() && particleString != null) {
                                ResourceLocation particleLocation = new ResourceLocation(particleString);
                                if (particleLocation != null)
                                    particles.add(particleLocation);
                            }
                        }
                    }
                }
                // spawning random particles from the created list
                if (particles.size() > 0) {
                    for (int index0 = 0; index0 < Mth.nextInt(RandomSource.create(), 5, 7); index0++) {
                        ResourceLocation chosenParticle = particles.get(Mth.nextInt(RandomSource.create(), 0, (particles.size() - 1)));
                        System.out.println(chosenParticle);
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
