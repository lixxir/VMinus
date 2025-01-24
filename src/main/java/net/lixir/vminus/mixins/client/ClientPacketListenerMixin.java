package net.lixir.vminus.mixins.client;

import com.google.gson.JsonObject;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionValueHandler;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Unique
    private final ClientPacketListener vminus$clientPacketListener = (ClientPacketListener) (Object) this;

    @Inject(method = "handleTakeItemEntity", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"), cancellable = true)
    private void onHandleTakeItemEntity(ClientboundTakeItemEntityPacket packet, CallbackInfo ci) {
        ClientPacketListenerAccessor accessor = (ClientPacketListenerAccessor) vminus$clientPacketListener;
        Entity entity = vminus$clientPacketListener.getLevel().getEntity(packet.getItemId());

        if (entity instanceof ItemEntity itemEntity) {
            ItemStack itemStack = itemEntity.getItem();
            JsonObject visionData = Vision.getData(itemStack);
            String soundString = VisionProperties.getString(visionData, "pick_up_sound", itemStack);
            if (soundString != null && !soundString.isEmpty()) {
                ResourceLocation resourceLocation = new ResourceLocation(soundString);
                vminus$clientPacketListener.getLevel().playLocalSound(entity.getX(), entity.getY(), entity.getZ(),
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(resourceLocation)),
                        SoundSource.PLAYERS, 0.2F,
                        (accessor.getRandom().nextFloat() - accessor.getRandom().nextFloat()) * 1.4F + 2.0F, false);
        }
            }
    }
}
