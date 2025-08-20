package net.lixir.vminus.events.entity.living.mob;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.entity.VariantEntity;
import net.lixir.vminus.network.VMinusNetwork;
import net.lixir.vminus.network.VariantSyncPacket;
import net.lixir.vminus.resources.data.sight.SightManager;
import net.lixir.vminus.vision.util.VisionEntityVariant;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class MobSpawnEventHandler {
    @SubscribeEvent
    public static void onMobSpawnEvent(MobSpawnEvent.@NotNull FinalizeSpawn event) {
        LivingEntity entity = event.getEntity();
        if (entity == null)
            return;
        ServerLevelAccessor level = event.getLevel();

        if (SightManager.get("adjust_spawn_health")) {
            float value = (float) entity.getAttributeBaseValue(Attributes.MAX_HEALTH);
            if (value > 0) {
                entity.setHealth(value);
            }
        }

        if (entity instanceof VariantEntity variantEntity) {
            VisionEntityVariant visionEntityVariant = null;
            if (variantEntity.vMinus$getVariantName() == null && variantEntity.vMinus$getVariantTexture() == null) {
                if (!level.isClientSide())
                    visionEntityVariant = VariantEntity.setFromWeightedList(entity);
            }
            if (visionEntityVariant != null && visionEntityVariant.texture() != null && visionEntityVariant.name() != null) {
                VMinus.queueServerWork(1, () -> VMinusNetwork.CHANNEL.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                        new VariantSyncPacket(entity.getId(), variantEntity.vMinus$getVariantName(), variantEntity.vMinus$getVariantTexture())
                ));
            }
        }
    }
}
