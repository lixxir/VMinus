package net.lixir.vminus.visions.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public enum VisionType {
    ITEM(VisionHandler.getItemVisionKey(), VisionHandler.getItemVisionCache(), (byte) 0),
    BLOCK(VisionHandler.getBlockVisionKey(), VisionHandler.getBlockVisionCache(), (byte) 1),
    ENTITY(VisionHandler.getEntityVisionKey(), VisionHandler.getEntityVisionCache(), (byte) 2),
    EFFECT(VisionHandler.getEffectVisionKey(), VisionHandler.getEffectVisionCache(), (byte) 3),
    ENCHANTMENT(VisionHandler.getEnchantmentVisionKey(), VisionHandler.getEnchantmentVisionCache(), (byte) 4);

    private final ConcurrentHashMap<String, Integer> visionKey;
    private final CopyOnWriteArrayList<JsonObject> visionCache;
    private final byte id;

    VisionType(ConcurrentHashMap<String, Integer> visionKey, CopyOnWriteArrayList<JsonObject> visionCache, byte id) {
        this.visionKey = visionKey;
        this.visionCache = visionCache;
        this.id = id;
    }
    public ConcurrentHashMap<String, Integer> getVisionKey() {
        return this.visionKey;
    }
    public CopyOnWriteArrayList<JsonObject> getVisionCache() {
        return this.visionCache;
    }
    public @Nullable JsonObject getMainVision() {
        switch(id) {
            case 0 -> {
                return VminusModVariables.main_item_vision;
            }
            case 1 -> {
                return VminusModVariables.main_block_vision;
            }
            case 2 -> {
                return VminusModVariables.main_entity_vision;
            }
            case 3 -> {
                return VminusModVariables.main_effect_vision;
            }
            case 4 -> {
                return VminusModVariables.main_enchantment_vision;
            }
        }
        return null;
    }
    public byte getId() {
        return this.id;
    }
}