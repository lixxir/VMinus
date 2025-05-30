package net.lixir.vminus.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

import java.util.Map;

public class VNBTUtils {

    public static JsonObject compoundToJson(CompoundTag tag) {
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
                json.addProperty(key, b.getAsByte());
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

    public static CompoundTag jsonToCompound(JsonObject json) {
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
                    if (num instanceof Byte)
                        tag.putByte(key, num.byteValue());
                    else if (num instanceof Short)
                        tag.putShort(key, num.shortValue());
                    else if (num instanceof Integer)
                        tag.putInt(key, num.intValue());
                    else if (num instanceof Long)
                        tag.putLong(key, num.longValue());
                    else if (num instanceof Float)
                        tag.putFloat(key, num.floatValue());
                    else tag.putDouble(key, num.doubleValue());
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
