package net.lixir.vminus.network.capes;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.procedures.*;
import net.lixir.vminus.world.inventory.CapesMenuMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapesMenuButtonMessage {
    private final int buttonID, x, y, z;

    public CapesMenuButtonMessage(FriendlyByteBuf buffer) {
        this.buttonID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    public CapesMenuButtonMessage(int buttonID, int x, int y, int z) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void buffer(CapesMenuButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.buttonID);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static void handler(CapesMenuButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player entity = context.getSender();
            int buttonID = message.buttonID;
            int x = message.x;
            int y = message.y;
            int z = message.z;
            handleButtonAction(entity, buttonID, x, y, z);
        });
        context.setPacketHandled(true);
    }

    public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
        Level world = entity.level();
        HashMap guistate = CapesMenuMenu.guistate;
        // security measure to prevent arbitrary chunk generation
        if (!world.hasChunkAt(new BlockPos(x, y, z)))
            return;
        if (buttonID == 0) {

            SetBeeperCapeProcedure.execute(entity);
        }
        if (buttonID == 1) {

            SetEmptyCapeProcedure.execute(entity);
        }
        if (buttonID == 3) {

            SetEmptyCapeProcedure.execute(entity);
        }
        if (buttonID == 5) {

            SetGhostCapeProcedure.execute(entity);
        }
        if (buttonID == 6) {

            SetEmptyCapeProcedure.execute(entity);
        }
        if (buttonID == 8) {

            SetMarrowCapeProcedure.execute(entity);
        }
        if (buttonID == 9) {

            SetEmptyCapeProcedure.execute(entity);
        }
        if (buttonID == 11) {

            SetShroudCapeProcedure.execute(entity);
        }
        if (buttonID == 12) {

            SetEmptyCapeProcedure.execute(entity);
        }
        if (buttonID == 14) {

            SetTrollCapeProcedure.execute(entity);
        }
        if (buttonID == 15) {

            SetEmptyCapeProcedure.execute(entity);
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        VMinusMod.addNetworkMessage(CapesMenuButtonMessage.class, CapesMenuButtonMessage::buffer, CapesMenuButtonMessage::new, CapesMenuButtonMessage::handler);
    }
}
