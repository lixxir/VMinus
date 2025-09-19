package net.lixir.vminus.network;

import net.lixir.vminus.world.item.IEquipmentItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ServerboundJumpPacket {
    private final int entityId;
    private final boolean onGround;

    public ServerboundJumpPacket(int entityId, boolean onGround) {
        this.entityId = entityId;
        this.onGround = onGround;
    }

    public static void encode(@NotNull ServerboundJumpPacket pkt, @NotNull FriendlyByteBuf buf) {
        buf.writeInt(pkt.entityId);
        buf.writeBoolean(pkt.onGround);
    }

    @Contract("_ -> new")
    public static @NotNull ServerboundJumpPacket decode(@NotNull FriendlyByteBuf buf) {
        return new ServerboundJumpPacket(buf.readInt(), buf.readBoolean());
    }

    public static void handle(ServerboundJumpPacket pkt, @NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null)
                return;

            ServerLevel level = sender.serverLevel();
            Entity entity = level.getEntity(pkt.entityId);
            if (!(entity instanceof LivingEntity livingEntity))
                return;

            ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
            if (boots.isEmpty())
                return;

            if (boots.getItem() instanceof IEquipmentItem IEquipmentItem) {
                IEquipmentItem.onEntityJump(level, livingEntity, boots, pkt.onGround);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
