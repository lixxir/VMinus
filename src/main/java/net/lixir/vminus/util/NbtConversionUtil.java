package net.lixir.vminus.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class NbtConversionUtil {

    public static @NotNull JsonObject compoundToJson(@NotNull CompoundTag tag) {
        JsonObject json = new JsonObject();
        for (String key : tag.getAllKeys()) {
            Tag nbt = tag.get(key);
            if (nbt instanceof CompoundTag nested) {
                json.add(key, compoundToJson(nested));
            } else if (nbt instanceof ListTag list) {
                JsonArray array = new JsonArray();
                for (Tag item : list) {
                    if (item instanceof CompoundTag ct) {
                        array.add(compoundToJson(ct));
                    } else if (item instanceof StringTag st) {
                        array.add(new JsonPrimitive(st.getAsString()));
                    }
                }
                json.add(key, array);
            } else if (nbt instanceof IntTag i) {
                json.addProperty(key, i.getAsInt());
            } else if (nbt instanceof ByteTag b) {
                byte val = b.getAsByte();
                if (val == 0 || val == 1) {
                    json.addProperty(key, val == 1);
                } else {
                    json.addProperty(key, val);
                }
            } else if (nbt instanceof ShortTag s) {
                json.addProperty(key, s.getAsShort());
            } else if (nbt instanceof LongTag l) {
                json.addProperty(key, l.getAsLong());
            } else if (nbt instanceof FloatTag f) {
                json.addProperty(key, f.getAsFloat());
            } else if (nbt instanceof DoubleTag d) {
                json.addProperty(key, d.getAsDouble());
            } else if (nbt instanceof StringTag st) {
                json.addProperty(key, st.getAsString());
            } else {
                json.addProperty(key, nbt.getAsString());
            }
        }
        return json;
    }

    public static @NotNull CompoundTag jsonToCompound(@NotNull JsonObject json) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (primitive.isBoolean()) {
                    tag.putBoolean(key, primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    Number num = primitive.getAsNumber();
                    if (primitive.getAsString().endsWith("b")) {
                        tag.putByte(key, num.byteValue());
                    } else if (primitive.getAsString().endsWith("s")) {
                        tag.putShort(key, num.shortValue());
                    } else if (primitive.getAsString().endsWith("f")) {
                        tag.putFloat(key, num.floatValue());
                    } else if (primitive.getAsString().endsWith("L") || primitive.getAsString().endsWith("l")) {
                        tag.putLong(key, num.longValue());
                    } else if (num.doubleValue() == num.intValue()) {
                        tag.putInt(key, num.intValue());
                    } else {
                        tag.putDouble(key, num.doubleValue());
                    }
                } else {
                    tag.putString(key, primitive.getAsString());
                }
            } else if (value.isJsonObject()) {
                tag.put(key, jsonToCompound(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                JsonArray array = value.getAsJsonArray();
                ListTag list = new ListTag();
                for (JsonElement e : array) {
                    if (e.isJsonPrimitive()) {
                        list.add(StringTag.valueOf(e.getAsJsonPrimitive().getAsString()));
                    } else if (e.isJsonObject()) {
                        list.add(jsonToCompound(e.getAsJsonObject()));
                    }
                }
                tag.put(key, list);
            }
        }
        return tag;
    }


}
