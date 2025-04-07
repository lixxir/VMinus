package net.lixir.vminus.visions.resources.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.registry.Traits;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.util.VisionTrait;
import net.lixir.vminus.visions.values.VisionValue;
import net.lixir.vminus.world.Trait;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VisionTraitParser extends AbstractVisionParser<VisionTrait> {
    @Override
    public @Nullable List<VisionValue<VisionTrait>> parse(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();

            Boolean value = arrayObject.getAsJsonPrimitive("value").getAsBoolean();
            ResourceLocation resourceLocation = parseResourceLocation("id", key, arrayObject);
            Trait trait = Traits.fromId(resourceLocation);
            VisionTrait visionTrait = new VisionTrait(trait, value);

            visionValues.add(VisionValue.create(visionTrait, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
