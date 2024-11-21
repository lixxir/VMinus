package net.lixir.vminus.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DurabilityCommandProcedureProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments) {
        ItemStack mainhand = ItemStack.EMPTY;
        String operation = "";
        double amount = 0;
        try {
            for (Entity entityiterator : EntityArgument.getEntities(arguments, "entities")) {
                mainhand = (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
                operation = StringArgumentType.getString(arguments, "operation");
                amount = DoubleArgumentType.getDouble(arguments, "amount");
                if ((operation).equals("set")) {
                    mainhand.setDamageValue((int) Math.max(0, mainhand.getMaxDamage() - amount));
                } else if ((operation).equals("decrease")) {
                    mainhand.setDamageValue((int) Math.max(mainhand.getDamageValue() + amount, 0));
                } else if ((operation).equals("increase")) {
                    mainhand.setDamageValue((int) Math.min(mainhand.getDamageValue() - amount, mainhand.getMaxDamage()));
                }
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}
