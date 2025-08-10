package net.lixir.vminus.mixins.client;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Unique
    private final ClientPacketListener vMinus$self = (ClientPacketListener) (Object) this;

    @Inject(method = "handleTakeItemEntity", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"))
    private void vMinus$pickUpItem(@NotNull ClientboundTakeItemEntityPacket packet, CallbackInfo ci) {
        ClientPacketListenerAccessor accessor = (ClientPacketListenerAccessor) vMinus$self;
        Entity entity = vMinus$self.getLevel().getEntity(packet.getItemId());

        if (entity instanceof ItemEntity itemEntity) {
            SoundEvent soundEvent = VisionUtils.getOverrideValue(((VisionDuck) itemEntity.getItem().getItem()), VisionProperties.Items.COLLECT_SOUND, new VisionContext(itemEntity));
            if (soundEvent != null) {
                vMinus$self.getLevel().playLocalSound(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        soundEvent,
                        SoundSource.PLAYERS, 0.2F,
                        (accessor.getRandom().nextFloat() - accessor.getRandom().nextFloat()) * 1.4F + 2.0F, false);
            }
         }
    }
}
