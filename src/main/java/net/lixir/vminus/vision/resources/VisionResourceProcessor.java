package net.lixir.vminus.vision.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.util.VisionType;

import java.util.Map;
import java.util.UUID;

public class VisionResourceProcessor {
    private final static int DEFAULT_PRIORITY = 500;

    public static JsonObject processJsonObject(String folderName, JsonElement jsonFile) {
        VisionType visionType = VisionType.getFromDirectory(folderName);
        String listType = visionType.getListType();
        JsonObject jsonFileObject = jsonFile.getAsJsonObject();
        VMinus.LOGGER.debug("Testing process: {} ", jsonFile);
        String visionSign = UUID.randomUUID().toString().replace("-", "").substring(0,8) + "-";

        // Gives the json object a key to refer to.


        // Giving every primitive property a random name so they don't overlap.
        JsonObject updatedJsonFileObject = new JsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonFileObject.entrySet()) {
            String originalKey = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            VMinus.LOGGER.info("Checking: {}", jsonElement);
            if (originalKey.endsWith(VisionManager.CONDITION_PATH) || originalKey.endsWith( VisionManager.PRIORITY_PATH))
                continue;

            if (jsonElement.isJsonPrimitive()) {
                // Generate a random key for this primitive value.

                String randomSign = generateRandomSign(originalKey, jsonFileObject, visionSign);

                // Also give conditions the same name.
                if (jsonFileObject.has(originalKey + VisionManager.CONDITION_PATH)) {
                    updatedJsonFileObject.add(randomSign +  VisionManager.CONDITION_PATH, jsonFileObject.get(originalKey +  VisionManager.CONDITION_PATH));
                    VMinus.LOGGER.info("New conditions: {}", randomSign + VisionManager.CONDITION_PATH);
                }
                // Also give priority the same name
                if (jsonFileObject.has(originalKey + VisionManager.PRIORITY_PATH)) {
                    updatedJsonFileObject.add(randomSign + VisionManager.PRIORITY_PATH, jsonFileObject.get(originalKey + VisionManager.PRIORITY_PATH));
                    VMinus.LOGGER.info("New priority: {}", randomSign + VisionManager.PRIORITY_PATH);
                }

                // Add the primitive value with the new random key.
                updatedJsonFileObject.add(randomSign, jsonElement);
                VMinus.LOGGER.info("New name: {}", randomSign);


            } else if (originalKey.equals("conditions")) {
                updatedJsonFileObject.add(visionSign.substring(0, visionSign.indexOf('-')) + "$" + originalKey, jsonElement);
            } else {
                updatedJsonFileObject.add(originalKey, jsonElement);
            }
        }


        if (!listType.isEmpty()) {
            updatedJsonFileObject = transformArrayKeyJson(updatedJsonFileObject, listType);
        }

        VMinus.LOGGER.info("Processed JSON: {}", updatedJsonFileObject);

        return updatedJsonFileObject;
    }

    private static String generateRandomSign(String originalKey, JsonObject jsonObject, String visionSign) {
        // Ignore whatever the user has set in case of accidental duplicate variables.
        if (originalKey.contains("$")) {
            originalKey = originalKey.substring(originalKey.indexOf('$')+1);
        }
        String randomSign;
        do {
            randomSign = visionSign + (UUID.randomUUID().toString().replace("-", "").substring(0,8) + "$" + originalKey);
        } while (jsonObject.has(randomSign));
        return randomSign;
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

                    VMinus.LOGGER.warn("Type mismatch for key: {}. Overwriting with new value.", key);
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
