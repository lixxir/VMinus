package net.lixir.vminus.visions.resources.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.registry.VMinusRarities;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisionRarityCodec extends VisionEnumCodec<Rarity> {
    public VisionRarityCodec() {
        super(Rarity.class);
    }

    @Override
    public @Nullable List<VisionValue<Rarity>> decode(JsonObject jsonObject, String key, ICondition.IContext context) throws JsonParseException {
        List<VisionValue<Rarity>> visionValues = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);

        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            String rarityString = arrayObject.getAsJsonPrimitive("value").getAsString();
            Rarity value;
            switch (rarityString.toUpperCase()) {
                case "LEGENDARY" -> value = VMinusRarities.LEGENDARY;
                case "INVERTED" -> value = VMinusRarities.INVERTED;
                case "UNOBTAINABLE" -> value = VMinusRarities.UNOBTAINABLE;
                case "DELICACY" -> value = VMinusRarities.DELICACY;
                default -> {
                    try {
                        value = Rarity.valueOf(rarityString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new JsonParseException(rarityString + " is not a valid rarity for " + key);
                    }
                }
            }
            visionValues.add(VisionValue.create(value, arrayObject, jsonObject, key));
        }

        return visionValues;
    }
}
