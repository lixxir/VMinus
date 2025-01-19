package net.lixir.vminus.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.lixir.vminus.capes.Cape;
import net.lixir.vminus.capes.CapeHelper;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber
public class CapeCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("cape")
                .requires(s -> s.hasPermission(1))
                .then(Commands.argument("capeId", StringArgumentType.string())
                        .suggests(CapeCommand::getCapeSuggestions)
                        .executes(arguments -> {
                            Player player = (Player) arguments.getSource().getEntity();
                            String capeId = StringArgumentType.getString(arguments, "capeId");

                            if (player != null) {
                                if (CapeHelper.ownsCape(player, capeId) || capeId.equals("default")) {
                                    player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                                        capability.cape_id = capeId;
                                        capability.syncPlayerVariables(player);
                                    });
                                    player.sendSystemMessage(Component.literal("Cape set to " + capeId).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
                                } else {
                                    player.sendSystemMessage(Component.literal("You do not own this cape or it does not exist.").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
                                }
                            }
                            return 0;
                        })));
    }

    public static CompletableFuture<Suggestions> getCapeSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Player entity = null;


        if (context.getSource().getEntity() instanceof Player) {
            entity = (Player) context.getSource().getEntity();
        }

        if (entity != null) {
            List<Cape> availableCapes = CapeHelper.getAvailableCapes(entity);
            for (Cape cape : availableCapes) {
                builder.suggest(cape.getId());
            }
        }
        builder.suggest("default");

        return builder.buildFuture();
    }
}
