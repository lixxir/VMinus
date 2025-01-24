package net.lixir.vminus.events;

import com.google.gson.JsonObject;
import net.lixir.vminus.registry.VMinusSounds;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

@Mod.EventBusSubscriber
public class DroppedItemEventHandler {
	@SubscribeEvent
	public static void vminus$ItemTossEvent(ItemTossEvent event) {
		Entity entity = event.getEntity();
		if (entity == null)
			return;
		LevelAccessor world = event.getEntity().level();
		Item item = event.getEntity().getItem().getItem();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		if (!world.isClientSide()) {
            if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					JsonObject visionData = Vision.getData(item);
					String soundString = VisionProperties.getString(visionData, "drop_sound", item);
					if (soundString != null && !soundString.isEmpty()) {
						ResourceLocation resourceLocation = new ResourceLocation(soundString);
						_level.playSound(null, BlockPos.containing(x, y, z),
                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(resourceLocation)),
								SoundSource.PLAYERS, (float) 0.2,
								(float) (1 + Mth.nextDouble(RandomSource.create(), -0.1, 0.1)));
					}
					_level.playSound(null, BlockPos.containing(x, y, z),
							VMinusSounds.ITEM_DROP.get(),
							SoundSource.PLAYERS, (float) 0.2,
							(float) (1 + Mth.nextDouble(RandomSource.create(), -0.1, 0.1)));
				}
			}
        }
	}
}
