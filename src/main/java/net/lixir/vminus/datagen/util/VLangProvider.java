package net.lixir.vminus.datagen.util;

import net.lixir.vminus.mixins.LanguageProviderAccessor;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.Map;

public class VLangProvider extends LanguageProvider {
    private final String modId;

    public VLangProvider(PackOutput output, String modId, String locale) {
        super(output, modId, locale);
        this.modId = modId;
    }

    private static String toTitleCase(String input) {
        String[] words = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }

    public String getModId() {
        return modId;
    }

    @Override
    protected void addTranslations() {
        for (Block block : UnifiedRegistry.fromId(modId).getBlocks()) {
            BlockEntryAccessor accessor = (BlockEntryAccessor) block;
            BlockEntry blockEntry = accessor.vminus$getEntry();
            if (blockEntry == null)
                continue;
            String langKey = blockEntry.getLangValue();
            if (langKey == null || has(langKey))
                continue;
            if (langKey.equals("default")) {
                add(block);
            } else {
                add(block, langKey);
            }
        }
        for (Item item : UnifiedRegistry.fromId(modId).getItems()) {
            ItemEntryAccessor accessor = (ItemEntryAccessor) item;
            ItemEntry itemEntry = accessor.vminus$getEntry();
            if (itemEntry == null)
                continue;
            String langKey = itemEntry.getLangValue();
            if (langKey == null || has(langKey))
                continue;
            if (langKey.equals("default")) {
                add(item);
            } else {
                add(item, langKey);
            }
        }
    }

    protected void add(Item item) {
        String id = ForgeRegistries.ITEMS.getKey(item).getPath();
        String name = toTitleCase(id.replace('_', ' '));
        add(item, name);
    }

    protected void add(Block block) {
        String id = ForgeRegistries.BLOCKS.getKey(block).getPath();
        String name = toTitleCase(id.replace('_', ' '));
        add(block, name);
    }

    protected Map<String, String> getTranslations() {
        return Collections.unmodifiableMap(((LanguageProviderAccessor) this).getData());
    }

    public boolean has(String key) {
        return getTranslations().containsKey(key);
    }

    @Override
    public void add(String key, String value) {
        Map<String, String> data = ((LanguageProviderAccessor) this).getData();
        if (!has(key))
            data.put(key, value);
    }

}
