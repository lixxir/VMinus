package net.lixir.vminus.resources.data.vision;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class VisionFormatter {
    public static @NotNull JsonObject processJson(String singleListName, String multiListName, @NotNull JsonElement jsonFile) throws JsonParseException {
        JsonObject jsonFileObject = jsonFile.getAsJsonObject();

        if (!jsonFileObject.has(multiListName))
            jsonFileObject.add(multiListName, new JsonArray());
        // Wrap single keys in a list
        keyToArray(jsonFileObject, singleListName, multiListName);
        keyToArray(jsonFileObject, "tag", multiListName);

        JsonObject processedJsonObject = new JsonObject();

        // Add the list back if it existed.
        if (jsonFileObject.has(multiListName) && jsonFileObject.get(multiListName).isJsonArray())
            processedJsonObject.add(multiListName, jsonFileObject.get(multiListName));

        for (Map.Entry<String, JsonElement> entry : jsonFileObject.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonElement = entry.getValue();

            JsonArray mergedConditions = new JsonArray();

            if (jsonElement.isJsonPrimitive() && !key.endsWith("/priority")) { // Wrap primitives in arrays
                JsonObject wrappedObject = new JsonObject();
                wrappedObject.add("value", jsonElement);
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(wrappedObject);

                processedJsonObject.add(key, jsonArray);
            } else if (jsonElement.isJsonArray() && !key.equals(multiListName)) { // Wrap primitives in arrays in objects
                JsonArray newArray = new JsonArray();
                for (JsonElement arrayElement : jsonElement.getAsJsonArray()) {
                    if (arrayElement.isJsonPrimitive()) {
                        JsonObject wrappedObject = new JsonObject();
                        wrappedObject.add("value", arrayElement);
                        newArray.add(wrappedObject);
                    } else {
                        newArray.add(arrayElement);
                    }
                }
                processedJsonObject.add(key, newArray);

            } else if (jsonElement.isJsonObject()) { // Wrap objects in arrays
                JsonArray newArray = wrapObjectInArray(jsonElement, mergedConditions);

                processedJsonObject.add(key, newArray);
            }
        }

        return processedJsonObject;
    }

    private static @NotNull JsonArray wrapObjectInArray(@NotNull JsonElement jsonElement, JsonArray mergedConditions) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray newArray = new JsonArray();
        JsonObject newObject = new JsonObject();
        for (Map.Entry<String, JsonElement> objectEntry : jsonObject.entrySet()) {
            String entryKey = objectEntry.getKey();
            JsonElement entryElement = objectEntry.getValue();

            newObject.add(entryKey, entryElement);
        }
        newObject.add("conditions", mergedConditions);
        newArray.add(newObject);
        return newArray;
    }

    public static void keyToArray(JsonObject jsonFileObject, String singleName, String listType) {
        if (!jsonFileObject.has(singleName))
            return;

        JsonElement singleElement = jsonFileObject.get(singleName);

        if (!singleElement.isJsonPrimitive())
            return;

        String singleValue = singleElement.getAsString();
        if (singleName.equals("tag")) {
            if (!singleValue.startsWith("#")) {
                singleValue = "#" + singleValue;
            }
        }

        JsonArray jsonArray;

        if (jsonFileObject.has(listType) && jsonFileObject.get(listType).isJsonArray()) {
            jsonArray = jsonFileObject.get(listType).getAsJsonArray();
        } else {
            jsonArray = new JsonArray();
            jsonFileObject.add(listType, jsonArray);
        }

        jsonFileObject.remove(singleName);
        jsonArray.add(singleValue);
    }


    public static ArrayList<String> getEntries(String listName, JsonObject jsonObject) throws JsonParseException {
        JsonArray listArray;
        if (jsonObject.has(listName)) {
            if (jsonObject.get(listName).isJsonArray()) {
                listArray = jsonObject.getAsJsonArray(listName);
            } else {
                throw new JsonParseException(listName + " is not a JsonArray.");
            }
        } else {
            throw new JsonParseException(listName + " not found. " + jsonObject);
        }
        return new ArrayList<>(listArray.asList().stream()
                .map(JsonElement::getAsString)
                .toList());
    }
}
