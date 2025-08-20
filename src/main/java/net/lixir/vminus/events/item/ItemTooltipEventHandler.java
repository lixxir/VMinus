package net.lixir.vminus.events.item;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.VMinusChatFormatting;
import net.lixir.vminus.resources.asset.VMinusFonts;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
@SuppressWarnings("deprecation")
public class ItemTooltipEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(@NotNull final ItemTooltipEvent event) {
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
        if (!(flavorTranslation.equals(flavorKey)))
            hasFlavor = true;

        if (inspectable || hasFlavor) {
            if (!altDown) {
                MutableComponent mutableComponent = Component.literal("[ALT")
                        .withStyle(ChatFormatting.BLUE)
                        .append(Component.literal("0")
                                .withStyle(Style.EMPTY.withFont(VMinusFonts.ICONS).withColor(ChatFormatting.WHITE)))
                        .append(Component.literal("]")
                                .withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(ChatFormatting.BLUE)));
                tooltip.add(mutableComponent);
            } else {
                // Inspections
                for (MutableComponent inspection : inspections) {
                    MutableComponent inspectionText = inspection.copy()
                            .withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(ChatFormatting.DARK_GREEN));

                    MutableComponent mutableComponent = Component.literal("2")
                            .withStyle(Style.EMPTY.withFont(VMinusFonts.ICONS).withColor(ChatFormatting.WHITE))
                            .append(inspectionText);
                    tooltip.add(mutableComponent);
                }

                // Flavor text
                if (hasFlavor) {
                    MutableComponent flavorText = Component.literal(flavorTranslation)
                            .withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(VMinusChatFormatting.INDIGO).withItalic(true)); // default font

                    MutableComponent mutableComponent = Component.literal("3")
                            .withStyle(Style.EMPTY.withFont(VMinusFonts.ICONS).withColor(ChatFormatting.WHITE))
                            .append(flavorText);
                    tooltip.add(mutableComponent);
                }

                MutableComponent mutableComponent = Component.literal("[ALT")
                        .withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal("1")
                                .withStyle(Style.EMPTY.withFont(VMinusFonts.ICONS).withColor(ChatFormatting.WHITE)))
                        .append(Component.literal("]")
                                .withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(ChatFormatting.DARK_GRAY))); // reset font
                tooltip.add(mutableComponent);
            }
        }



    }
}
