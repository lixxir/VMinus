package net.lixir.vminus.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;

public class VMinusCommandSuggestionProviders {
    public static final SuggestionProvider<CommandSourceStack> DIMENSIONS = (context, builder) -> {
        MinecraftServer server = context.getSource().getServer();
        for (ServerLevel level : server.getAllLevels()) {
            ResourceKey<Level> key = level.dimension();
            builder.suggest(key.location().toString());
        }
        return builder.buildFuture();
    };
}
