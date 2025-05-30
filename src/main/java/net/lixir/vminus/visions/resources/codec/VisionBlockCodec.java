package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionBlockCodec extends AbstractVisionCodec<Block> {
    @Override
    public @Nullable List<VisionValue<Block>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<Block>> visionValues = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
            Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
            if (block == null)
                throw new JsonParseException(resourceLocation + " is not a valid block.");
            visionValues.add(VisionValue.create(block, arrayObject, jsonObject, key));
        }
        return visionValues;
    }
}
