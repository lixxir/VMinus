package net.lixir.vminus;

import com.google.gson.JsonObject;
import net.lixir.vminus.network.resource.RequestFileGenerationPacket;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class VMinusClientEvents {
    // Requesting to accumulate all the stored jsons on the server-side for visions.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && entity == Minecraft.getInstance().player) {
            VMinusMod.PACKET_HANDLER.sendToServer(new RequestFileGenerationPacket());
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Entity player = event.getEntity();
        if (player != null) {
            // LevelAccessor world = player.level();
            List<Component> tooltip = event.getToolTip();
            ItemStack itemstack = event.getItemStack();
            if (tooltip == null)
                return;
            Item item = itemstack.getItem();
            String itemId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
            boolean inspectable = false;
            int inspectNum = 1;
            boolean altDown = Screen.hasAltDown();
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
            if (inspectable || VisionValueHelper.isBooleanMet(itemData, "inspectable", itemstack)) {
                if (!altDown) {
                    tooltip.add(Component.literal("§9[ALT" + IconHandler.getIcon("inspect") + "§9]"));
                } else {
                    tooltip.add(Component.literal("§8[ALT" + IconHandler.getIcon("inspect_held") + "§8]"));
                }
            }
        }
    }
}
