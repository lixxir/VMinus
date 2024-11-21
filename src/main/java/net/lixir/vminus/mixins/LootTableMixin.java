package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.lixir.vminus.VisionValueHelper;
import net.lixir.vminus.LootTableAccessor;
import net.lixir.vminus.VisionHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(LootTable.class)
public abstract class LootTableMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("LootTableMixin");

    @Shadow
    private List<Integer> getAvailableSlots(Container container, RandomSource randomSource) {
        return List.of();
    }

    @Shadow
    public abstract ObjectArrayList<ItemStack> getRandomItems(LootContext context);

    @Shadow
    private void shuffleAndSplitItems(ObjectArrayList<ItemStack> items, int slots, RandomSource randomSource) {
    }

    /**
     * @param container  The container to fill.
     * @param lootParams Parameters for generating loot.
     * @param seed       The random seed for loot generation.
     * @reason To filter out any banned items.
     * @author lixir
     */
    @Overwrite
    public void fill(Container container, LootParams lootParams, long seed) {
        LootTable lootTable = (LootTable) (Object) this;
        ResourceLocation sequence = LootTableAccessor.getRandomSequence(lootTable);
        LootContext lootContext = (new LootContext.Builder(lootParams)).withOptionalRandomSeed(seed).create(sequence);
        ObjectArrayList<ItemStack> itemStacks = getRandomItems(lootContext);
        RandomSource randomSource = lootContext.getRandom();
        List<Integer> availableSlots = this.getAvailableSlots(container, randomSource);
        ObjectArrayList<ItemStack> filteredItems = itemStacks.stream().filter(itemstack -> {
            JsonObject itemData = VisionHandler.getVisionData(itemstack);
            return !VisionValueHelper.isBooleanMet(itemData, "banned", itemstack);
        }).collect(Collectors.toCollection(ObjectArrayList::new));
        this.shuffleAndSplitItems(filteredItems, availableSlots.size(), randomSource);
        for (ItemStack itemstack : filteredItems) {
            if (availableSlots.isEmpty()) {
                LOGGER.warn("Tried to over-fill a container");
                return;
            }
            container.setItem(availableSlots.remove(availableSlots.size() - 1), itemstack);
        }
    }
}
