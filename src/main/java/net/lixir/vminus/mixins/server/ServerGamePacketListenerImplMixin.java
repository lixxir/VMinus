package net.lixir.vminus.mixins.server;

import net.lixir.vminus.roles.Role;
import net.lixir.vminus.roles.RoleManager;
import net.lixir.vminus.roles.RoleSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Inject(method = "performChatCommand", at = @At("HEAD"), cancellable = true)
    private void restrictCommands(@NotNull ServerboundChatCommandPacket packet, LastSeenMessages lastSeenMessages, CallbackInfo ci) {
        ServerGamePacketListenerImpl self = (ServerGamePacketListenerImpl)(Object)this;
        ServerPlayer player = self.player;

        String baseCommand = packet.command().split(" ")[0];
        RoleSavedData data = RoleSavedData.get(player.level());
        String role = data.getRole(player.getUUID());
        Role roleObj = RoleManager.INSTANCE.getRole(role);

        if (roleObj != null && !roleObj.allowedCommands().contains(baseCommand)) {
            player.sendSystemMessage(Component.literal("You do not have permission to use /" + baseCommand).withStyle(ChatFormatting.RED));
            ci.cancel();
        }
    }
}
