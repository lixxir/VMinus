package net.lixir.vminus;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.lang.reflect.Field;

public class LootTableAccessor {
    public static ResourceLocation getRandomSequence(LootTable lootTable) {
        try {
            Field field = LootTable.class.getDeclaredField("randomSequence");
            field.setAccessible(true);
            return (ResourceLocation) field.get(lootTable);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
