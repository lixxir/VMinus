package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class StunHitProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity(), event.getSource().getEntity());
        }
    }

    public static void execute(Entity entity, Entity sourceentity) {
        execute(null, entity, sourceentity);
    }

    private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        ItemStack mainhand = ItemStack.EMPTY;
        mainhand = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
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
    }
}
