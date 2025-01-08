package net.lixir.vminus.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber
public class DurabilityCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("durability").requires(s -> s.hasPermission(3))
                .then(Commands.argument("entities", EntityArgument.entities())
                        .then(Commands.argument("operation", StringArgumentType.word())
                                .suggests(DurabilityCommand::getOperationSuggestions)
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                        .executes(arguments -> {
                                            handleDurabilityCommand(arguments);
                                            return 0;
                                        })))));
    }

    private static void handleDurabilityCommand(CommandContext<CommandSourceStack> arguments) {
        try {
            for (Entity entity : EntityArgument.getEntities(arguments, "entities")) {
                if (entity instanceof LivingEntity livingEntity) {
                    ItemStack mainhand = livingEntity.getMainHandItem();
                    String operation = StringArgumentType.getString(arguments, "operation");
                    double amount = DoubleArgumentType.getDouble(arguments, "amount");

                    switch (operation) {
                        case "set" -> mainhand.setDamageValue((int) Math.max(0, mainhand.getMaxDamage() - amount));
                        case "decrease" -> mainhand.setDamageValue((int) Math.max(mainhand.getDamageValue() + amount, 0));
                        case "increase" -> mainhand.setDamageValue((int) Math.min(mainhand.getDamageValue() - amount, mainhand.getMaxDamage()));
                        default -> throw new IllegalArgumentException("Invalid operation: " + operation);
                    }
                }
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }

    private static CompletableFuture<Suggestions> getOperationSuggestions(CommandContext<?> context, SuggestionsBuilder builder) {
        builder.suggest("set");
        builder.suggest("increase");
        builder.suggest("decrease");
        return builder.buildFuture();
    }
}