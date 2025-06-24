package net.lixir.vminus.vision.resource.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.lixir.vminus.vision.util.VisionAttribute;
import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VisionAttributeCodec extends VisionCodec<VisionAttribute> {
    @Override
    public Class<VisionAttribute> getClassType() {
        return VisionAttribute.class;
    }

    @Override
    public @Nullable List<VisionProperty<VisionAttribute>> decode(@NotNull JsonObject jsonObject, String key) throws JsonParseException {
        List<VisionProperty<VisionAttribute>> visionProperties = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonArrayElement : jsonArray) {
            JsonObject arrayObject = jsonArrayElement.getAsJsonObject();
            double value;
            if (arrayObject.has("value")) {
                if (!arrayObject.get("value").isJsonPrimitive()) {
                    throw new JsonParseException(arrayObject.get("value") + " is not a valid double.");
                }
                value = arrayObject.get("value").getAsJsonPrimitive().getAsDouble();
            } else {
                value = 0;
            }
            Boolean replace = arrayObject.has("replace") && arrayObject.get("replace").isJsonPrimitive() && arrayObject.getAsJsonPrimitive("replace").getAsBoolean();
            Boolean remove = arrayObject.has("remove") && arrayObject.get("remove").isJsonPrimitive() && arrayObject.getAsJsonPrimitive("remove").getAsBoolean();

            String uuidString = arrayObject.has("uuid") ? arrayObject.getAsJsonPrimitive("uuid").getAsString() : null;
            UUID uuid;
            if (uuidString == null) {
                uuid = UUID.randomUUID();
            } else {
                try {
                    uuid = UUID.fromString(uuidString);
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException(uuidString + " is not a valid UUID.");
                }
            }

            String id = arrayObject.getAsJsonPrimitive("id").getAsString();
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(id));
            if (attribute == null)
                throw new JsonParseException(id + " is not a valid attribute.");

            String operationString = arrayObject.has("operation") ? arrayObject.getAsJsonPrimitive("operation").getAsString() : "addition";
            AttributeModifier.Operation operation;
            if (operationString != null) {
                try {
                    operation = AttributeModifier.Operation.valueOf(operationString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException(operationString + " is not a valid operation.");
                }
            } else {
                operation = AttributeModifier.Operation.ADDITION;
            }

            String slotString = arrayObject.has("slot") ? arrayObject.getAsJsonPrimitive("slot").getAsString() : null;
            EquipmentSlot equipmentSlot = null;
            if (slotString != null) {
                try {
                    equipmentSlot = EquipmentSlot.valueOf(slotString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException(slotString + " is not a valid equipment slot.");
                }
            }

            String name = arrayObject.has("id") ? arrayObject.getAsJsonPrimitive("id").getAsString() : null;
            if (name == null) {
                if (id.contains(".")) {
                    name = id.substring(id.indexOf(".") + 1);
                } else if (id.contains(":")) {
                    name = id.substring(id.indexOf(":") + 1);
                }
                if (name != null)
                    name = name.replaceAll("_", " ");
            }
            if (name == null)
                throw new JsonParseException("Name is null.");

            AttributeModifier attributeModifier = new AttributeModifier(uuid, name, value, operation);
            VisionAttribute visionAttribute = new VisionAttribute(remove, replace, attributeModifier, attribute, equipmentSlot, id);

            visionProperties.add(VisionProperty.create(visionAttribute, arrayObject, jsonObject, key));
        }
        return visionProperties;
    }

    @Override
    public @Nullable JsonObject encode(@NotNull VisionAttribute visionAttribute) {
        JsonObject jsonObject = new JsonObject();

        AttributeModifier modifier = visionAttribute.attributeModifier();
        jsonObject.addProperty("uuid", modifier.getId().toString());
        jsonObject.addProperty("id", visionAttribute.id());
        jsonObject.addProperty("value", modifier.getAmount());
        jsonObject.addProperty("operation", modifier.getOperation().name().toLowerCase());
        if (visionAttribute.replace())
            jsonObject.addProperty("replace", true);
        if (visionAttribute.remove())
            jsonObject.addProperty("remove", true);

        EquipmentSlot slot = visionAttribute.equipmentSlot();
        if (slot != null) {
            jsonObject.addProperty("slot", slot.name().toLowerCase());
        }

        return jsonObject;
    }
}
