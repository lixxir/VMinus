package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class VisionBlockCodec extends VisionCodec<Block> {
    @Override
    public Class<Block> getClassType() {
        return Block.class;
    }


    @Override
    public @Nullable List<VisionProperty<Block>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<Block>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            ResourceLocation resourceLocation = parseResourceLocation(key, arrayObject);
            Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
            if (block == null)
                throw new JsonParseException(resourceLocation + " is not a valid block.");
            visionProperties.add(VisionProperty.create(block, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull Block value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", BuiltInRegistries.BLOCK.getKey(value).toString());
        return jsonObject;
    }
}
