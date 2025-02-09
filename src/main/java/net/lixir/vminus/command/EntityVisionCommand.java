package net.lixir.vminus.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.core.Visions;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityVisionCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("entityvision")
                        .requires(s -> s.hasPermission(3))
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .executes(arguments -> {
                                    Player sender = (Player) arguments.getSource().getEntity();
                                    sender.sendSystemMessage(Component.literal("Entity Visions logged").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
                                   try {
                                        for (Entity entity : EntityArgument.getEntities(arguments, "entities")) {
                                            JsonObject visionData = Visions.getData(entity);

                                            VMinus.LOGGER.info("Visions Data for {}: {}", entity.getName().getString(), visionData);
                                        }
                                    } catch (CommandSyntaxException e) {
                                        VMinus.LOGGER.error("Error retrieving vision data", e);
                                    }
                                    return 0;
                                })));
    }
}
