package net.lixir.vminus.core.resources.managers;

import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.visions.visionable.IItemVisionable;
import net.lixir.vminus.events.LevelLoadedEventHandler;
import net.lixir.vminus.core.*;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.resources.deserializers.ItemVisionDeserializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ItemVisionManager extends SimpleJsonResourceReloadListener {
    private final ICondition.IContext context;

    static public final String CONDITION_PATH = "/conditions";
    static public final String PRIORITY_PATH = "/priority";
    static private final String DIRECTORY = "visions/items";

    public ItemVisionManager(ICondition.IContext context) {
        super(VisionType.ITEM.getGSON(), DIRECTORY);
        this.context = context;
    }

    @Override
    protected void apply(
            @NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profilerFiller) {
        ArrayList<ItemVision> itemVisions = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> jsonFile : resourceLocationJsonElementMap.entrySet()) {
            try {
                JsonObject jsonObject = VisionProcessor.processJson(DIRECTORY, jsonFile.getValue());
                Gson gson = new GsonBuilder().registerTypeAdapter(ItemVision.class, new ItemVisionDeserializer()).setPrettyPrinting().disableHtmlEscaping().create();
                itemVisions.add(gson.fromJson(jsonObject, ItemVision.class));
            } catch (Exception e) {
                VMinus.LOGGER.error("Error processing JSON file for ItemVision: {}", jsonFile.getKey(), e);
            }
        }

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            final String id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
            ItemVision copyVision = new ItemVision();
            for (ItemVision vision : itemVisions) {
                if (VisionProcessor.visionApplies(item, id, vision.getEntries(), context)) {
                    copyVision.merge(vision);
                }
            }
            if (item instanceof IItemVisionable itemVisionable) {
                itemVisionable.vminus$setVision(copyVision);
            }
        }


        //Visions.processAllVisionDataForType(this.visionType,context)
        Visions.ITEM_TAB_DATA.clear();
        LevelLoadedEventHandler.debounce = false;
    }
}