package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
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
            DamageSource damagesource = event.getSource();
            LivingEntity entity = event.getEntity();
            Entity sourceentity = event.getSource().getEntity();
            Entity immediatesourceentity = event.getSource().getDirectEntity();
            if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null)
                return;
            ItemStack mainHand = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);

            // Prevent invincible tagged entities from being hurt
            if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("vminus:invincible")))) {
                entity.invulnerableTime = 20;
                if (event.isCancelable()) {
                    event.setCanceled(true);
                } else if (event.hasResult()) {
                    event.setResult(Event.Result.DENY);
                }
            }

            // Damage horse armor when attacked if it has durability
            if (entity instanceof Horse) {
                if (entity.isAttackable()) {
                    ItemStack horseArmor = entity.getItemBySlot(EquipmentSlot.CHEST);
                    if (!(horseArmor.getItem() == ItemStack.EMPTY.getItem())) {
                        if (horseArmor.isDamageableItem()) {
                            if (horseArmor.hurt(1, RandomSource.create(), null)) {
                                    horseArmor.shrink(1);
                                    horseArmor.setDamageValue(0);
                            }
                        }
                    }
                }
            }
        }
    }
}
