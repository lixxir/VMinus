package net.lixir.vminus.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.core.util.ItemTabData;
import net.lixir.vminus.core.Visions;
import net.lixir.vminus.core.VisionProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.Map;

@Mod.EventBusSubscriber
public class LevelLoadedEventHandler {
    public volatile static boolean debounce = true;

    @SubscribeEvent
    public synchronized static void onWorldLoad(LevelEvent.Load event) {
        if (debounce) return;

        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            Item item = entry.getValue();
            JsonObject visionData = Visions.getData(item);

            if (VisionProperties.isHiddenInCreative(item, visionData) && !VisionProperties.isUnalteredHidden(item, visionData))
                continue;

            int index = 0;
            while (true) {
                JsonElement creativeElement = VisionProperties.searchElement(VisionProperties.Names.CREATIVE_ORDER, visionData, item, index);
                if (creativeElement == null)
                    break;
                JsonObject creativeObject;
                if (creativeElement.isJsonObject())
                    creativeObject = creativeElement.getAsJsonObject();
                else
                    break;


                index++;
                String tabId = VisionProperties.getString(creativeObject, visionData, VisionProperties.Names.TAB, item);

                if (tabId == null || tabId.isEmpty())
                    continue;

                String matchItemId = VisionProperties.getString(creativeObject, visionData, VisionProperties.Names.TARGET, item);
                Item matchItem;
                if (matchItemId == null || matchItemId.isEmpty()) {
                    matchItem = null;
                } else {
                    matchItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(matchItemId));
                    if (matchItem == null)
                        continue;
                }

                boolean before = VisionProperties.getBoolean(creativeObject, visionData, VisionProperties.Names.BEFORE, item, false);

                Visions.ITEM_TAB_DATA.add(new ItemTabData(item, tabId, matchItem, before));
            }

        }
        Collections.reverse(Visions.ITEM_TAB_DATA);
        debounce = true;

    }
}