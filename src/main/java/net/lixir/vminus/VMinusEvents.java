package net.lixir.vminus;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.core.ResourceVisionHelper;
import net.lixir.vminus.core.VisionHandler;
import net.lixir.vminus.core.VisionValueHelper;
import net.lixir.vminus.init.VminusModAttributes;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.network.capes.SetCapePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ISystemReportExtender;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class VMinusEvents {
    private static final ConcurrentHashMap<String, UUID> UUID_CACHE = new ConcurrentHashMap<>();
    @SubscribeEvent
    public static void onNonClientWorldLoad(LevelEvent.Load event) {
        LevelAccessor world = event.getLevel();
        if (world.isClientSide())
            return;
        ResourceVisionHelper.generateItemVisionsFile(world);
        ResourceVisionHelper.generateBlockVisionsFile(world);
        ResourceVisionHelper.generateEntityVisionsFile(world);
        ResourceVisionHelper.generateEffectVisionsFile(world);
        ResourceVisionHelper.generateEnchantmentVisionsFile(world);
    }
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        // Loading all visions for optimization
        VisionHandler.clearCaches();
        VisionHandler.loadVisions();
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerLoggedInEvent event) {
        ServerPlayer newPlayer = (ServerPlayer) event.getEntity();
        for (ServerPlayer otherPlayer : newPlayer.server.getPlayerList().getPlayers()) {
            otherPlayer.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                VMinusMod.PACKET_HANDLER.sendTo(new SetCapePacket(capability.cape_id, otherPlayer.getUUID()), newPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }


    // Setting attributes, removing nbt, and some other things.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack itemstack = event.getItemStack();
        String itemId = ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString();
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        CompoundTag tag = itemstack.getTag();
        EquipmentSlot eventSlot = (event != null) ? event.getSlotType() : null;
        Boolean miningFlag = false;
        if (itemData != null && (itemData.has("attributes") || itemData.has("tag") || itemData.has("remove_attributes"))) {
            if (itemData.has("remove_attributes")) {
                JsonArray removeAttributesArray = itemData.getAsJsonArray("remove_attributes");
                Set<String> attributesToRemove = new HashSet<>();
                for (JsonElement element : removeAttributesArray) {
                    JsonObject removeData = element.getAsJsonObject();
                    if (removeData.has("id")) {
                        attributesToRemove.add(removeData.get("id").getAsString());
                    }
                }
                if (event != null) {
                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                    for (Attribute attribute : originalModifiers.keySet()) {
                        for (AttributeModifier modifier : originalModifiers.get(attribute)) {
                            String modifierId = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();
                            if (attributesToRemove.contains(modifierId)) {
                                event.removeModifier(attribute, modifier);
                            }
                        }
                    }
                }
            }
            if (itemData.has("attributes")) {
                if (tag == null || !tag.contains("broken")) {
                    JsonArray attributesArray = itemData.getAsJsonArray("attributes");
                    for (JsonElement element : attributesArray) {
                        JsonObject attributeData = element.getAsJsonObject();
                        if (attributeData.has("id") && attributeData.has("value")) {
                            String attributeId = attributeData.get("id").getAsString();
                            String attributeName = "";
                            if (attributeData.has("name")) {
                                attributeName = attributeData.get("name").getAsString();
                            } else {
                                if (attributeId.indexOf(".") != -1) {
                                    attributeName = attributeId.substring(attributeId.indexOf(".") + 1);
                                } else  if (attributeId.indexOf(":") != -1) {
                                    attributeName = attributeId.substring(attributeId.indexOf(":") + 1);
                                }
                                attributeName = attributeName.replaceAll("_", " ");
                            }

                            String operationString = attributeData.has("operation") ? attributeData.get("operation").getAsString().toLowerCase() : "addition";
                            double attributeValue = attributeData.get("value").getAsDouble();
                            boolean replace = attributeData.has("replace") ? attributeData.get("replace").getAsBoolean() : false;
                            if (replace) {
                                if (event != null) {
                                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                                    for (Attribute attribute : originalModifiers.keySet()) {
                                        for (AttributeModifier modifier : originalModifiers.get(attribute)) {
                                            String modifierId = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();
                                            if (modifierId.equals(attributeId)) {
                                                event.removeModifier(attribute, modifier);
                                            }
                                        }
                                    }
                                }
                            }

                            AttributeModifier.Operation operation;
                            switch (operationString) {
                                case "multiply_base":
                                    operation = AttributeModifier.Operation.MULTIPLY_BASE;
                                    break;
                                case "multiply_total":
                                    operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
                                    break;
                                default:
                                    operation = AttributeModifier.Operation.ADDITION;
                                    break;
                            }
                            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeId));
                            if (attribute != null) {
                                UUID attributeUUID;
                                String compositeId = itemId + "|" + attributeId;
                                // caching & getting uuids
                                if (attributeData.has("uuid") ) {
                                    attributeUUID = UUID.fromString(attributeData.get("uuid").getAsString());
                                } else  if (UUID_CACHE.get(compositeId) != null) {
                                    attributeUUID = UUID_CACHE.get(compositeId);
                                } else {
                                    UUID randomUUID = UUID.randomUUID();
                                    UUID_CACHE.put(compositeId, randomUUID);
                                    attributeUUID = randomUUID;
                                }
                                EquipmentSlot slot = EquipmentSlot.MAINHAND;
                                if (attributeData.has("slot")) {
                                    slot = EquipmentSlot.valueOf(attributeData.get("slot").getAsString().toUpperCase());
                                } else {
                                    // attempted fallback to get the equipment slot if not defined.
                                    if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/helmets")))) {
                                        slot = EquipmentSlot.HEAD;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/chestplates")))) {
                                        slot = EquipmentSlot.CHEST;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/leggings")))) {
                                        slot = EquipmentSlot.LEGS;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/boots")))) {
                                        slot = EquipmentSlot.FEET;
                                    }
                                }
                                if (eventSlot == slot) {
                                    AttributeModifier modifier = new AttributeModifier(attributeUUID, attributeName, attributeValue, operation);
                                    if (event != null) {
                                        if (attributeId.equals("vminus:mining_speed"))
                                            miningFlag = true;
                                        event.addModifier(attribute, modifier);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (itemData != null && itemData.has("nbt")) {
            ItemStack copyStack = VisionValueHelper.setNbts(itemData, itemstack.copy());
            //System.out.println(itemData);
            CompoundTag copyTag = copyStack.getOrCreateTag();
            if (copyTag != null && !copyTag.isEmpty()) {
                if (tag == null)
                    tag = itemstack.getOrCreateTag();
                for (String key : copyTag.getAllKeys()) {
                    tag.put(key, copyTag.get(key));
                }
                itemstack.setTag(tag);
            }
        }
        if (ModList.get().isLoaded("detour")) {
            if (itemstack.isEnchantable()) {
                if (tag == null)
                    tag = itemstack.getOrCreateTag();
                if (!tag.contains("enchantment_limit")) {
                    int limit = 2;
                    if (itemstack.is(ItemTags.create(new ResourceLocation("detour:one_enchantment_limit")))) {
                        limit = 1;
                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("detour:three_enchantment_limit")))) {
                        limit = 3;
                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("detour:four_enchantment_limit")))) {
                        limit = 4;
                    }
                    tag.putDouble("enchantment_limit", limit);
                }
            }
        }
        if (eventSlot == EquipmentSlot.MAINHAND) {
            int efficiencyLevel = EnchantmentHelper.getEnchantments(itemstack).getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
            if (efficiencyLevel > 0) {
                double miningSpeedValue = efficiencyLevel * efficiencyLevel + 1;
                AttributeModifier miningSpeedModifier = new AttributeModifier(UUID.fromString("83e34d00-65ae-11ef-814d-325096b39f47"), "Effeciency Mining Speed", miningSpeedValue, AttributeModifier.Operation.ADDITION);
                if (event != null) {
                    event.addModifier(VminusModAttributes.MININGSPEED.get(), miningSpeedModifier);
                }
            }
            if (!miningFlag) {
                if (itemstack.getItem() instanceof TieredItem tieredItem) {
                    double tierMiningSpeed = tieredItem.getTier().getSpeed();
                    AttributeModifier tierMiningSpeedModifier = new AttributeModifier(UUID.fromString("e14d7c20-65ae-11ef-814d-325096b39f47"), "Tier Mining Speed", tierMiningSpeed, AttributeModifier.Operation.ADDITION);
                    if (event != null) {
                        event.addModifier(VminusModAttributes.MININGSPEED.get(), tierMiningSpeedModifier);
                    }
                }
            }
        }
    }

}
