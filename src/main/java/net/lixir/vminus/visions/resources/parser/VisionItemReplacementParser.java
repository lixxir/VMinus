package net.lixir.vminus.visions.resources.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class VisionItemReplacementParser extends AbstractVisionParser<VisionItemReplacement> {
    @Override
    public @Nullable List<VisionValue<VisionItemReplacement>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            if (!arrayObject.has("value"))
                continue;
            String value = arrayObject.getAsJsonPrimitive("value").getAsString();
            if (value.startsWith("#")) {
                ResourceLocation tagLocation = new ResourceLocation(value.substring(1));
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagLocation);
                Collection<Holder<Item>> tags = context.getTag(tagKey);

                VisionItemReplacement visionItemReplacement;
                if (tags != null && !tags.isEmpty()) {
                    visionItemReplacement = new VisionItemReplacement(tags.stream().findFirst().get().get().getDefaultInstance(), tagKey);
                } else {
                    visionItemReplacement = null;
                }
                visionValues.add(VisionValue.create(visionItemReplacement, arrayObject, jsonObject, key));
            } else {
                ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
                Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
                if (item == null)
                    throw new JsonParseException(resourceLocation + " is not a valid item.");
                ItemStack itemStack = item.getDefaultInstance();
                VisionItemReplacement visionItemReplacement = new VisionItemReplacement(itemStack, null);
                visionValues.add(VisionValue.create(visionItemReplacement, arrayObject, jsonObject, key));
            }
        }
        return visionValues;
    }
}
