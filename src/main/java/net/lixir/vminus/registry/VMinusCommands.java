package net.lixir.vminus.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lixir.vminus.cape.Cape;
import net.lixir.vminus.command.VMinusCommandSuggestionProviders;
import net.lixir.vminus.network.SyncCapePacket;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber
public class VMinusCommands {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SubscribeEvent
    public static void registerCommand(@NotNull RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var context = event.getBuildContext();

        dispatcher.register(Commands.literal("dimension")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("dimension", ResourceLocationArgument.id())
                            .suggests(VMinusCommandSuggestionProviders.DIMENSIONS)
                            .executes(VMinusCommands::teleportToDimension)
                    )
            );
        dispatcher.register(Commands.literal("fire").requires(s -> s.hasPermission(3)).then(Commands.argument("entities", EntityArgument.entities()).then(Commands.argument("seconds", DoubleArgumentType.doubleArg(0)).executes(arguments -> {
            for (Entity entity : EntityArgument.getEntities(arguments, "entities")) {
                entity.setSecondsOnFire((int) DoubleArgumentType.getDouble(arguments, "seconds"));
            }
            return 0;
        }))));
        dispatcher.register(Commands.literal("heal").requires(s -> s.hasPermission(3)).then(Commands.argument("entities", EntityArgument.entities()).then(Commands.argument("health", DoubleArgumentType.doubleArg(0)).executes(arguments -> {
            Level world = arguments.getSource().getUnsidedLevel();
            Entity entity = arguments.getSource().getEntity();
            if (entity == null && world instanceof ServerLevel _servLevel)
                entity = FakePlayerFactory.getMinecraft(_servLevel);
            String currentDimension = "";
            currentDimension = entity.level().dimension().location().toString();
            try {
                for (Entity entityiterator : EntityArgument.getEntities(arguments, "entities")) {
                    if (world instanceof ServerLevel _origLevel) {
                        world = _origLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(currentDimension)));
                        if (world != null) {
                            if (entityiterator instanceof LivingEntity _entity)
                                _entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + DoubleArgumentType.getDouble(arguments, "health")));
                        }
                    }
                }
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
            return 0;
        }))));
        dispatcher.register(Commands.literal("freeze").requires(s -> s.hasPermission(3)).then(Commands.argument("entities", EntityArgument.entities()).then(Commands.argument("ticks", DoubleArgumentType.doubleArg(0)).executes(arguments -> {
            Level world = arguments.getSource().getUnsidedLevel();
            Entity entity = arguments.getSource().getEntity();
            if (entity == null && world instanceof ServerLevel _servLevel)
                entity = FakePlayerFactory.getMinecraft(_servLevel);
            entity.setTicksFrozen((int) DoubleArgumentType.getDouble(arguments, "ticks"));
            return 0;
        }))));
        dispatcher.register(Commands.literal("cape")
                .requires(s -> s.hasPermission(1))
                .then(Commands.argument("capeId", StringArgumentType.string())
                        .suggests(VMinusCommands::getCapeSuggestions)
                        .executes(arguments -> {
                            Player player = (Player) arguments.getSource().getEntity();
                            String capeId = StringArgumentType.getString(arguments, "capeId");

                            if (player != null) {
                                if (Cape.ownsCape(player, capeId) || capeId.equals("default")) {
                                    player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                                        capability.cape_id = capeId;
                                        capability.syncPlayerVariables(player);

                                        if (player instanceof ServerPlayer serverPlayer) {
                                            SyncCapePacket.sendToAll(serverPlayer, capeId);
                                        }
                                    });

                                    player.sendSystemMessage(Component.literal("Cape set to " + capeId)
                                            .withStyle(ChatFormatting.ITALIC)
                                            .withStyle(ChatFormatting.GRAY));
                                } else {
                                    player.sendSystemMessage(Component.literal("You do not own this cape or it does not exist.")
                                            .withStyle(ChatFormatting.ITALIC)
                                            .withStyle(ChatFormatting.GRAY));
                                }
                            }
                            return 0;
                        })));

    }

    public static int teleportToDimension(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();
        ResourceLocation dimensionId = ResourceLocationArgument.getId(context, "dimension");
        MinecraftServer server = source.getServer();

        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, dimensionId);
        ServerLevel targetLevel = server.getLevel(dimensionKey);

        if (targetLevel == null) {
            source.sendFailure(Component.literal("Dimension not found: " + dimensionId));
            return 0;
        }
        if (player == null)
            return 0;
        if (player.level() == targetLevel) {
            source.sendFailure(Component.literal("You are already in that dimension."));
            return 0;
        }

        player.teleportTo(targetLevel, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        source.sendSuccess(() -> Component.literal("Teleported to " + dimensionId), false);
        return 1;
    }

    public static CompletableFuture<Suggestions> getCapeSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Player entity = null;

        if (context.getSource().getEntity() instanceof Player) {
            entity = (Player) context.getSource().getEntity();
        }

        if (entity != null) {
            String input = builder.getRemaining().toLowerCase();
            List<Cape> availableCapes = Cape.getAvailableCapes(entity);

            for (Cape cape : availableCapes) {
                if (cape.getId().toLowerCase().contains(input)) {
                    builder.suggest(cape.getId());
                }
            }
        }

        builder.suggest("default");

        return builder.buildFuture();
    }
}
