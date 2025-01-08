package net.lixir.vminus.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HealCommandCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("heal").requires(s -> s.hasPermission(3)).then(Commands.argument("entities", EntityArgument.entities()).then(Commands.argument("health", DoubleArgumentType.doubleArg(0)).executes(arguments -> {
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
    }
}
