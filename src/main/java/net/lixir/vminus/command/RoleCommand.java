package net.lixir.vminus.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.lixir.vminus.roles.RoleManager;
import net.lixir.vminus.roles.RoleSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Mod.EventBusSubscriber
public class RoleCommand {
    private static final SuggestionProvider<CommandSourceStack> ROLE_SUGGESTIONS = (ctx, builder) -> {
        Set<String> roles = RoleManager.INSTANCE.getAllRoleNames();
        roles.add("none");
        return SharedSuggestionProvider.suggest(roles, builder);
    };

    @SubscribeEvent
    public static void register(@NotNull RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("role")
                .requires(source -> source.hasPermission(4))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("role", StringArgumentType.word())
                                .suggests(ROLE_SUGGESTIONS)
                                .executes(ctx -> {
                                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                    String role = StringArgumentType.getString(ctx, "role");

                                    RoleSavedData data = RoleSavedData.get(player.level());

                                    if (role.equalsIgnoreCase("none")) {
                                        data.removeRole(player);
                                        ctx.getSource().sendSuccess(() -> Component.literal("Removed role from " + player.getName().getString()), true);
                                        return 1;
                                    }

                                    if (RoleManager.INSTANCE.getRole(role) == null) {
                                        ctx.getSource().sendFailure(Component.literal("Unknown role: " + role));
                                        return 0;
                                    }

                                    data.setRole(player, role);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Assigned role '" + role + "' to " + player.getName().getString()), true);
                                    return 1;
                                }))));
    }
}
