package net.lixir.vminus.network;

import net.lixir.vminus.world.entity.VariantEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VariantSyncPacket {
    private final int entityId;
    private final ResourceLocation variantName;
    private final ResourceLocation variantTexture;

    public VariantSyncPacket(int entityId, ResourceLocation variantName, ResourceLocation variantTexture) {
        this.entityId = entityId;
        this.variantName = variantName;
        this.variantTexture = variantTexture;
    }

    public VariantSyncPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.variantName = buf.readResourceLocation();
        this.variantTexture = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeResourceLocation(variantName);
        buf.writeResourceLocation(variantTexture);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Entity entity = world.getEntity(entityId);
                if (entity instanceof VariantEntity variantEntity) {
                    variantEntity.vMinus$setVariant(variantName, variantTexture);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}