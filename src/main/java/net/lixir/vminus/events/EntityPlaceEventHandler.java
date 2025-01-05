package net.lixir.vminus.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityPlaceEventHandler {
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        LevelAccessor world = event.getLevel();
        Entity entity = event.getEntity();
        if (entity == null) return;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();


        BlockPos pos = new BlockPos(x, y, z);
        BlockState blockState = world.getBlockState(pos);
        JsonObject blockData = VisionHandler.getVisionData(blockState.getBlock());
        if (blockData != null && blockData.has("place_blockstate")) {
            JsonArray blockStates = blockData.getAsJsonArray("place_blockstate");
            for (JsonElement element : blockStates) {
                JsonObject stateData = element.getAsJsonObject();
                String name = stateData.get("name").getAsString();
                String type = stateData.get("type").getAsString();
                switch (type) {
                    case "boolean":
                        boolean boolValue = stateData.get("value").getAsBoolean();
                        if (blockState.getBlock().getStateDefinition().getProperty(name) instanceof BooleanProperty booleanProp) {
                            world.setBlock(pos, blockState.setValue(booleanProp, boolValue), 3);
                        }
                        break;
                    case "int":
                        int intValue = stateData.get("value").getAsInt();
                        if (blockState.getBlock().getStateDefinition().getProperty(name) instanceof IntegerProperty intProp && intProp.getPossibleValues().contains(intValue)) {
                            world.setBlock(pos, blockState.setValue(intProp, intValue), 3);
                        }
                        break;
                    case "enum":
                        String enumValue = stateData.get("value").getAsString();
                        if (blockState.getBlock().getStateDefinition().getProperty(name) instanceof EnumProperty enumProp && enumProp.getValue(enumValue).isPresent()) {
                            world.setBlock(pos, blockState.setValue(enumProp, (Enum) enumProp.getValue(enumValue).get()), 3);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported block state type: " + type);
                }
            }
        }
    }
}
