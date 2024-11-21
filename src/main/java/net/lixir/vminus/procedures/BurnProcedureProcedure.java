package net.lixir.vminus.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;

public class BurnProcedureProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments) {
        try {
            for (Entity entityiterator : EntityArgument.getEntities(arguments, "entities")) {
                entityiterator.setSecondsOnFire((int) DoubleArgumentType.getDouble(arguments, "seconds"));
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}
