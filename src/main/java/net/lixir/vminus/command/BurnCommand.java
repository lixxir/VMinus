package net.lixir.vminus.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BurnCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("fire").requires(s -> s.hasPermission(3)).then(Commands.argument("entities", EntityArgument.entities()).then(Commands.argument("seconds", DoubleArgumentType.doubleArg(0)).executes(arguments -> {
            Level world = arguments.getSource().getUnsidedLevel();
            double x = arguments.getSource().getPosition().x();
            double y = arguments.getSource().getPosition().y();
            double z = arguments.getSource().getPosition().z();
            Entity sourceEntity = arguments.getSource().getEntity();
            if (sourceEntity == null && world instanceof ServerLevel _servLevel)
                sourceEntity = FakePlayerFactory.getMinecraft(_servLevel);
            Direction direction = Direction.DOWN;
            if (sourceEntity != null)
                direction = sourceEntity.getDirection();
            try {
                for (Entity entity : EntityArgument.getEntities(arguments, "entities")) {
                    entity.setSecondsOnFire((int) DoubleArgumentType.getDouble(arguments, "seconds"));
                }
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
            return 0;
        }))));
    }
}
