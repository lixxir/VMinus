
package net.lixir.vminus.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class RegistryHelper {
	private static List<String> blockList = new ArrayList<>();
	private static List<String> itemList = new ArrayList<>();
	private static List<String> entityList = new ArrayList<>();
	private static final TagKey<Block> BLOCK_BLACKLIST_TAG = TagKey.create(ForgeRegistries.Keys.BLOCKS, new ResourceLocation("vminus:random_block_blacklist"));
	private static final TagKey<Item> ITEM_BLACKLIST_TAG = TagKey.create(ForgeRegistries.Keys.ITEMS, new ResourceLocation("vminus:random_item_blacklist"));
	private static final TagKey<EntityType<?>> ENTITY_BLACKLIST_TAG = TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, new ResourceLocation("vminus:random_entity_blacklist"));

	public static String[] splitRegistryId(String registryId) {
		return registryId.split(",");
	}

	public static Optional<EntityData> parseEntityData(String registryIdWithWeight) {
		String[] parts = splitRegistryId(registryIdWithWeight);
		if (parts.length == 2) {
			String entityId = parts[0];
			try {
				int weight = Integer.parseInt(parts[1]);
				return Optional.of(new EntityData(entityId, weight));
			} catch (NumberFormatException e) {
				System.err.println("Invalid weight format: " + parts[1]);
			}
		} else {
			System.err.println("Invalid registry ID format: " + registryIdWithWeight);
		}
		return Optional.empty();
	}

	public record EntityData(String entityId, int weight) {
		@Override
			public String toString() {
				return "EntityData{id='" + entityId + "', weight=" + weight + "}";
			}
		}

	public static void initializeLists() {
		blockList = ForgeRegistries.BLOCKS.getValues().stream().filter(block -> !block.builtInRegistryHolder().is(BLOCK_BLACKLIST_TAG)).map(block -> ForgeRegistries.BLOCKS.getKey(block).toString()).collect(Collectors.toList());
		itemList = ForgeRegistries.ITEMS.getValues().stream().filter(item -> !item.builtInRegistryHolder().is(ITEM_BLACKLIST_TAG)).map(item -> ForgeRegistries.ITEMS.getKey(item).toString()).collect(Collectors.toList());
		entityList = ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(entityType -> !entityType.builtInRegistryHolder().is(ENTITY_BLACKLIST_TAG)).map(entityType -> ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString())
				.collect(Collectors.toList());
	}

	public static List<String> getBlockList() {
		return blockList;
	}

	public static List<String> getItemList() {
		return itemList;
	}

	public static List<String> getEntityList() {
		return entityList;
	}

	public static String getRandomBlock() {
		return blockList.isEmpty() ? null : blockList.get(new Random().nextInt(blockList.size()));
	}

	public static String getRandomItem() {
		return itemList.isEmpty() ? null : itemList.get(new Random().nextInt(itemList.size()));
	}

	public static String getRandomEntity() {
		return entityList.isEmpty() ? null : entityList.get(new Random().nextInt(entityList.size()));
	}

	@SubscribeEvent
	public static void onWorldLoad(net.minecraftforge.event.level.LevelEvent.Load event) {
		initializeLists();
	}
}
