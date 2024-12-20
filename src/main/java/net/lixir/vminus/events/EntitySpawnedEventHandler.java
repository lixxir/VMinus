package net.lixir.vminus.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.helpers.MobVariantHelper;
import net.lixir.vminus.network.mobvariants.MobVariantSyncPacket;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class EntitySpawnedEventHandler {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        Entity entity = event.getEntity();
        if (entity == null)
            return;

        JsonObject visionData = VisionHandler.getVisionData(entity.getType());
        // Banning banned entities
        if (VisionValueHelper.isBooleanMet(visionData, "banned", entity)) {
            if (event != null && event.isCancelable()) {
                event.setCanceled(true);
            } else if (event != null && event.hasResult()) {
                event.setResult(Event.Result.DENY);
            }
            return;
        }

        // Changing base attributes if it applies
        if (entity instanceof LivingEntity) {
            if (visionData != null) {
                if (visionData.has("base_attributes")) {
                    JsonArray baseAttributesArray = visionData.getAsJsonArray("base_attributes");
                    for (JsonElement element : baseAttributesArray) {
                        JsonObject elementData = element.getAsJsonObject();
                        if (elementData.has("id") && elementData.has("value")) {
                            String attributeId = elementData.get("id").getAsString();
                            double value = elementData.get("value").getAsDouble();
                            LivingEntity livingEntity = (LivingEntity) entity;
                            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeId));
                            if (attribute != null) {
                                AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
                                if (attributeInstance != null) {
                                    attributeInstance.setBaseValue(value);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Attempts to replace an in-world item entity if it needs to be, or bans if it needs to be.
        if (entity instanceof ItemEntity) {
            ItemStack itemStack = (entity instanceof ItemEntity _itemEnt ? _itemEnt.getItem() : ItemStack.EMPTY);
            JsonObject itemData = VisionHandler.getVisionData(itemStack);
            if (itemData != null) {
                if (itemData.has("drop_replace") || itemData.has("replace")) {
                    String replaceString = VisionValueHelper.getFirstValidString(itemData, "replace", itemStack);
                    if (replaceString == null || replaceString.isEmpty())
                        replaceString = VisionValueHelper.getFirstValidString(itemData, "drop_replace", itemStack);
                    ResourceLocation replaceLocation = new ResourceLocation(replaceString);
                    Item replacementItem = ForgeRegistries.ITEMS.getValue(replaceLocation);
                    if (replacementItem != null) {
                        ItemStack newItemStack = new ItemStack(replacementItem);
                        if (event != null && event.isCancelable()) {
                            event.setCanceled(true);
                        } else if (event != null && event.hasResult()) {
                            event.setResult(Event.Result.DENY);
                        }
                        Level world = entity.level();
                        ItemEntity newItemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), newItemStack);
                        world.addFreshEntity(newItemEntity);
                    }
                } else if (itemData.has("banned") && VisionValueHelper.isBooleanMet(itemData, "banned", itemStack)) {
                    if (event != null && event.isCancelable()) {
                        event.setCanceled(true);
                    } else if (event != null && event.hasResult()) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }

        // Adjusting max health for when attributes are set for entities.
        if (!entity.getPersistentData().getBoolean("health_adjust")) {
            if (entity instanceof LivingEntity _entity)
                _entity.setHealth((float) ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).getBaseValue());
            entity.getPersistentData().putBoolean("health_adjust", true);
        }

        if (visionData != null && visionData.has("variants")) {
            final String chosenVariant = MobVariantHelper.setOrGetVariant(entity, visionData);
            /* Have to have the first one without a delay for when it first spawns,
             and another one with a delay on rejoin so that it works */
            serverLevel.getServer().execute(() -> {
                VMinusMod.PACKET_HANDLER.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                        new MobVariantSyncPacket(entity.getId(), chosenVariant)
                );
            });
            VMinusMod.queueServerWork(1, () -> {
                serverLevel.getServer().execute(() -> {
                    VMinusMod.PACKET_HANDLER.send(
                            PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                            new MobVariantSyncPacket(entity.getId(), chosenVariant)
                    );
                });
            });
        }
    }
}
