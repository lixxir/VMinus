package net.lixir.vminus;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class Inspecting {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Entity player = event.getEntity();
        if (player != null) {
            LevelAccessor world = player.level();
            if (world != null) {
                execute(event, event.getItemStack(), event.getToolTip(), player, world);
            }
        }
    }

    public static void execute(ItemStack itemstack, List<Component> tooltip, Entity player, LevelAccessor world) {
        //execute(null, itemstack.copy(), tooltip, player, world);
    }

    private static void execute(@Nullable Event event, ItemStack itemstack, List<Component> tooltip, Entity player, LevelAccessor world) {
        if (tooltip == null || player == null || world == null)
            return;
        Item item = itemstack.getItem();
        String itemId = ForgeRegistries.ITEMS.getKey(item).toString();
        CompoundTag tag = itemstack.getTag();
        Map<String, String> icons = new HashMap<>();
        boolean inspectable = false;
        int inspectNum = 1;
        Boolean altDown = Screen.hasAltDown();
        String inspectStart = IconHandler.getIcon("inspect_bauble") + IconHandler.getIcon("darkGreenColor");
        while (true) {
            String validInspect = itemId.replaceAll(":", ".");
            String inspectString = I18n.get("inspection.item." + inspectNum + "." + validInspect);
            if (!(inspectString.equals("inspection.item." + inspectNum + "." + validInspect))) {
                inspectable = true;
                if (altDown) {
                    tooltip.add(Component.literal(inspectStart + inspectString));
                } else {
                    break;
                }
            } else {
                break;
            }
            inspectNum++;
        }
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (inspectable || JsonValueUtil.isBooleanMet(itemData, "inspectable", itemstack)) {
            if (!altDown) {
                tooltip.add(Component.literal("\u00A79[ALT" + IconHandler.getIcon("inspect") + "\u00A79]"));
            } else {
                tooltip.add(Component.literal("\u00A78[ALT" + IconHandler.getIcon("inspect_held") + "\u00A78]"));
            }
        }
    }
}
