package net.lixir.vminus.procedures;

import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemVisionProcedureProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        String itemId = "";
        ItemStack theitem = ItemStack.EMPTY;
        itemId = (new Object() {
            public String getMessage() {
                try {
                    return MessageArgument.getMessage(arguments, "item_id").getString();
                } catch (CommandSyntaxException ignored) {
                    return "";
                }
            }
        }).getMessage();
        theitem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation((itemId).toLowerCase(java.util.Locale.ENGLISH))));
        JsonObject itemData = VisionHandler.getVisionData(theitem, true);
        if (itemData != null) {
            if (entity instanceof Player _player && !_player.level.isClientSide())
                _player.displayClientMessage(Component.literal(("Item Vision found for " + itemId + ", " + itemData)), false);
        } else {
            if (entity instanceof Player _player && !_player.level.isClientSide())
                _player.displayClientMessage(Component.literal("Item Vision not found."), false);
        }
    }
}
