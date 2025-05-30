package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.item.trait.ItemTraits;
import net.lixir.vminus.visions.util.VisionTrait;
import net.lixir.vminus.visions.values.VisionValue;
import net.lixir.vminus.item.trait.ItemTrait;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionTraitCodec extends AbstractVisionCodec<VisionTrait> {
    @Override
    public @Nullable List<VisionValue<VisionTrait>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<VisionTrait>> visionValues = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Boolean value = arrayObject.getAsJsonPrimitive("value").getAsBoolean();
            ResourceLocation resourceLocation = parseResourceLocation("id", key, arrayObject);
            ItemTrait itemTrait = ItemTraits.fromId(resourceLocation);
            VisionTrait visionTrait = new VisionTrait(itemTrait, value);

            visionValues.add(VisionValue.create(visionTrait, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
