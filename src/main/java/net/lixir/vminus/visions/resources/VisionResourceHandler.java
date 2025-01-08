package net.lixir.vminus.visions.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.util.VisionType;

import javax.annotation.Nullable;
import java.util.Map;

public class VisionResourceHandler {
    private final static int DEFAULT_PRIORITY = 500;

    public static JsonObject processJsonObject(String folderName, JsonElement jsonElement) {
        VisionType visionType = VisionType.getFromDirectory(folderName);
        String listType = visionType.getListType();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!listType.isEmpty()) {
            jsonObject = transformArrayKeyJson(jsonObject, listType);
        }

        for (String key : jsonObject.keySet()) {
            JsonElement newElement = jsonObject.get(key);
            JsonElement wrappedElement = wrapPrimitive(key, newElement);

            if (jsonObject.has(key)) {
                JsonObject existingObject = jsonObject.getAsJsonObject(key);
                int existingPriority = getPriorityFromWrapped(existingObject);
                int newPriority = getPriorityFromWrapped(wrappedElement.getAsJsonObject());

                if (newPriority > existingPriority) {
                    jsonObject.remove(key);
                    jsonObject.add(key, wrappedElement);
                } else if (newPriority == existingPriority) {
                    mergeJsonObjects(existingObject, wrappedElement.getAsJsonObject(), 0);
                }
            } else {
                jsonObject.add(key, wrappedElement);
            }
        }
        return jsonObject;
    }

    public static JsonObject transformArrayKeyJson(JsonObject inputJson, String listType) {
        if (inputJson.has(listType) && inputJson.get(listType).isJsonArray()) {
            JsonArray listArray = inputJson.getAsJsonArray(listType);
            StringBuilder combinedKey = new StringBuilder();

            for (int i = 0; i < listArray.size(); i++) {
                combinedKey.append(listArray.get(i).getAsString());
                if (i < listArray.size() - 1) {
                    combinedKey.append(",");
                }
            }
            JsonObject wrappedContent = new JsonObject();
            for (Map.Entry<String, JsonElement> entry : inputJson.entrySet()) {
                if (!entry.getKey().equals(listType)) {
                    wrappedContent.add(entry.getKey(), entry.getValue());
                }
            }

            JsonObject resultObject = new JsonObject();
            resultObject.add(combinedKey.toString(), wrappedContent);
            return resultObject;
        }
        return inputJson;
    }

    private static int getPriorityFromWrapped(JsonObject wrappedObject) {
        if (wrappedObject.has("priority")) {
            JsonElement priorityElement = wrappedObject.get("priority");

            if (priorityElement.isJsonObject()) {
                JsonObject priorityObject = priorityElement.getAsJsonObject();
                if (priorityObject.has("value")) {
                    return priorityObject.get("value").getAsInt();
                }
            } else if (priorityElement.isJsonArray()) {
                JsonArray priorityArray = priorityElement.getAsJsonArray();
                if (!priorityArray.isEmpty() && priorityArray.get(0).isJsonObject()) {
                    JsonObject firstPriorityObject = priorityArray.get(0).getAsJsonObject();
                    if (firstPriorityObject.has("value")) {
                        return firstPriorityObject.get("value").getAsInt();
                    }
                }
            }
        }
        return 0;
    }


    public static JsonElement wrapPrimitive(String key, @Nullable JsonElement element) {
        if (element != null) {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                JsonObject wrappedObject = new JsonObject();
                for (String innerKey : obj.keySet()) {
                    JsonElement innerElement = obj.get(innerKey);
                    if (innerElement.isJsonPrimitive()) {
                        JsonObject valueObject = new JsonObject();
                        valueObject.add("value", innerElement);
                        JsonArray valueArray = new JsonArray();
                        valueArray.add(valueObject);
                        wrappedObject.add(innerKey, valueArray);
                    } else {
                        wrappedObject.add(innerKey, innerElement);
                    }
                }
                return wrappedObject;
            }
        }
        return element;
    }

    public static void mergeJsonObjects(JsonObject existingJsonObject, JsonObject newJsonObject, int depth) {
        for (String key : newJsonObject.keySet()) {
            JsonElement newElement = newJsonObject.get(key);
            if (existingJsonObject.has(key)) {
                JsonElement existingElement = existingJsonObject.get(key);
                if (existingElement.isJsonObject() && newElement.isJsonObject()) {
                    int existingPriority = getPriority(existingElement.getAsJsonObject());
                    int newPriority = getPriority(newElement.getAsJsonObject());

                    if (newPriority > existingPriority) {
                        existingJsonObject.add(key, newElement);
                    } else if (newPriority == existingPriority) {
                        mergeJsonObjects(existingElement.getAsJsonObject(), newElement.getAsJsonObject(), depth + 1);
                    }
                } else if (!existingElement.isJsonObject() && !newElement.isJsonObject()) {

                    existingJsonObject.add(key, newElement);
                } else {

                    VMinusMod.LOGGER.warn("Type mismatch for key: {}. Overwriting with new value.", key);
                    existingJsonObject.add(key, newElement);
                }
            } else {

                existingJsonObject.add(key, newElement);
            }
        }
    }

    private static int getPriority(JsonObject jsonObject) {
        if (jsonObject.has("value")) {
            JsonElement valueElement = jsonObject.get("value");
            if (valueElement.isJsonArray() && !valueElement.getAsJsonArray().isEmpty()) {
                JsonElement innerElement = valueElement.getAsJsonArray().get(0);
                if (innerElement.isJsonObject() && innerElement.getAsJsonObject().has("priority")) {
                    return innerElement.getAsJsonObject().get("priority").getAsInt();
                }
            }
        }
        return DEFAULT_PRIORITY;
    }


}
