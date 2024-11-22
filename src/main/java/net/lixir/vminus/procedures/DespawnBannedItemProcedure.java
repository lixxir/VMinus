package net.lixir.vminus.procedures;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionValueHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class DespawnBannedItemProcedure {
    @SubscribeEvent
    public static void onEntitySpawned(EntityJoinLevelEvent event) {
        execute(event, event.getEntity());
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        ItemStack convert = ItemStack.EMPTY;
        if (entity instanceof ItemEntity) {
            convert = (entity instanceof ItemEntity _itemEnt ? _itemEnt.getItem() : ItemStack.EMPTY);
            JsonObject itemData = VisionHandler.getVisionData(convert);
            if (itemData != null) {
                if (itemData.has("drop_replace") || itemData.has("replace")) {
                    String replaceString = VisionValueHelper.getFirstValidString(itemData, "replace", convert);
                    if (replaceString == null || replaceString.isEmpty())
                        replaceString = VisionValueHelper.getFirstValidString(itemData, "drop_replace", convert);
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
                } else if (itemData.has("banned") && VisionValueHelper.isBooleanMet(itemData, "banned", convert)) {
                    if (event != null && event.isCancelable()) {
                        event.setCanceled(true);
                    } else if (event != null && event.hasResult()) {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }
}
