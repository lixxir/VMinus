package net.lixir.vminus.events;

import net.lixir.vminus.util.IconHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
@SuppressWarnings("deprecation")
public class ItemTooltipEventHandler {
    private static final String INSPECT_PREFIX = IconHandler.getIcon("inspect_bauble") + " ";
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        if (tooltip == null)

            return;
        ItemStack itemstack = event.getItemStack();
        Item item = itemstack.getItem();

        String rawItemId = BuiltInRegistries.ITEM.getKey(item).toString().replaceAll(":", ".");
        String[] split = rawItemId.split("\\.");
        String modId = split[0];
        String itemId = split[1];

        boolean inspectable = false;
        int inspectIndex = 0;

        boolean altDown = Screen.hasAltDown();
        boolean shiftDown = Screen.hasShiftDown();


        List<MutableComponent> inspections = new ArrayList<>();
        while (true) {
            String inspectKey = "item." + modId + "." + itemId + ".inspection." + inspectIndex;
            String inspectTranslation = I18n.get(inspectKey);
            if (!(inspectTranslation.equals(inspectKey))) {
                inspectable = true;
                inspections.add(Component.literal(inspectTranslation));
            } else {
                break;
            }
            inspectIndex++;
        }
        boolean hasFlavor = false;
        String flavorKey = "item." + modId + "." + itemId + ".flavor";
        String flavorTranslation = I18n.get(flavorKey);
        if (!(flavorTranslation.equals(flavorKey))) {
            hasFlavor = true;
        }
        if (inspectable || hasFlavor) {
            if (!altDown) {
                tooltip.add(Component.literal("§9[ALT" + IconHandler.getIcon("inspect") + "§9]"));
            } else {
                for (MutableComponent mutableComponent : inspections)
                    tooltip.add(Component.literal(INSPECT_PREFIX).append(mutableComponent.withStyle(ChatFormatting.DARK_GREEN)));
                if (hasFlavor)
                    tooltip.add(Component.literal(INSPECT_PREFIX).append(Component.literal(flavorTranslation).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GREEN)));
                tooltip.add(Component.literal("§8[ALT" + IconHandler.getIcon("inspect_held") + "§8]"));
            }
        }

    }
}
