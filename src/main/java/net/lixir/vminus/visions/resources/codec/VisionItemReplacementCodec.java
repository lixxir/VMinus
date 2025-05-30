package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionEntityVariant;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VisionItemReplacementCodec extends AbstractVisionCodec<VisionItemReplacement> {
    @Override
    public @Nullable List<VisionValue<VisionItemReplacement>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<VisionItemReplacement>> visionValues = new ArrayList<>();

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

    @Override
    public @Nullable JsonObject encode(VisionItemReplacement value) {
        if (value == null)
            return null;

        JsonObject jsonObject = new JsonObject();

        if (value.tag() != null) {
            jsonObject.addProperty("value", "#" + value.tag().location());
        } else {

            ItemStack itemStack = value.itemStack();
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
            if (id == null)
                throw new JsonParseException("Cannot encode ItemStack with unregistered item: " + itemStack);

            jsonObject.addProperty("value", id.toString());
        }

        return jsonObject;
    }

}
