package net.lixir.vminus.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;

public class FreezeProcedureProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        try {
            for (Entity entityiterator : EntityArgument.getEntities(arguments, "entities")) {
                entity.setTicksFrozen((int) DoubleArgumentType.getDouble(arguments, "ticks"));
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}
