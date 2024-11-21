package net.lixir.vminus.procedures;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.core.VisionHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class EntityBaseAttributeProcedure {
    @SubscribeEvent
    public static void onEntitySpawned(EntityJoinLevelEvent event) {
        execute(event, event.getEntity());
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null || !(entity instanceof LivingEntity))
            return;
        JsonObject visionData = VisionHandler.getVisionData(entity);
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
}
