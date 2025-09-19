package net.lixir.vminus.api.datagen.lang.provider;

import net.lixir.vminus.api.datagen.lang.LangKey;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.EntityDefinition;
import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.lixir.vminus.api.registry.definition.RegistryDefinition;
import net.lixir.vminus.mixins.data.language.LanguageProviderAccessor;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.lixir.vminus.api.registry.definition.duck.EntityDefinitionDuck;
import net.lixir.vminus.api.registry.definition.duck.ItemDefinitionDuck;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

@SuppressWarnings("deprecation")
public abstract class VLangProvider extends LanguageProvider {
    private final String modId;

    public VLangProvider(PackOutput output, String modId, String locale) {
        super(output, modId, locale);
        this.modId = modId;
    }

    @Override
    protected void addTranslations() {
        for (Block block : VRegistry.fromId(modId).getBlocks()) {
            BlockDefinition entry = ((BlockDefinitionDuck) block).vMinus$getDefinition();
            if (entry != null) {
                handleEntry(block, BuiltInRegistries.BLOCK.getKey(block).getPath(), entry);
            }
        }

        for (Item item : VRegistry.fromId(modId).getItems()) {
            ItemDefinition entry = ((ItemDefinitionDuck) item).vMinus$getDefinition();
            if (entry != null) {
                handleEntry(item, BuiltInRegistries.ITEM.getKey(item).getPath(), entry);
            }
        }

        for (EntityType<?> entityType : VRegistry.fromId(modId).getEntityTypes()) {
            EntityDefinition entry = ((EntityDefinitionDuck) entityType).vMinus$getDefinition();
            if (entry != null) {
                handleEntry(entityType, BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath(), entry);
            }
        }
    }

    protected void gamerule(GameRules.@NotNull Key<?> rule, String name) {
        gamerule(rule, name, null);
    }

    protected void subtitle(String name, String translation) {
        add("subtitles." + name, translation);
    }

    protected void gamerule(GameRules.@NotNull Key<?> rule, String name, @Nullable String description) {
        String id = rule.getId();
        add("gamerule." + id, name);
        if (description != null)
            add("gamerule." + id + ".description", description);
    }


    private <E extends RegistryDefinition<E,T>,T> void handleEntry(Object object, String idPath, @NotNull RegistryDefinition<E, T> entry) {
        LangKey langKey = entry.getLangKey();
        if (langKey.isEmpty())
            return;
        if (langKey.isUnset()) {
            String name = toTitleCase(idPath.replace('_', ' '));
            if (object instanceof Block block) {
                add(block, name);
            } else if (object instanceof Item item) {
                add(item, name);
            } else if (object instanceof EntityType<?> entityType) {
                add(entityType, name);
            }
        } else {
            String lang = langKey.getKey();
            if (object instanceof Block block) {
                add(block, lang);
            } else if (object instanceof Item item) {
                add(item, lang);
            } else if (object instanceof EntityType<?> entityType) {
                add(entityType, lang);
            }
        }
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

    public boolean has(String key) {
        return getTranslations().containsKey(key);
    }

    protected Map<String, String> getTranslations() {
        return Collections.unmodifiableMap(((LanguageProviderAccessor) this).getData());
    }

    protected void flavor(Item item, String text) {
        String rawItemId = BuiltInRegistries.ITEM.getKey(item).toString().replaceAll(":", ".");
        String[] split = rawItemId.split("\\.");
        String modId = split[0];
        String itemId = split[1];
        String inspectKey = "item." + modId + "." + itemId + ".flavor";
        add(inspectKey, text);
    }

    protected void inspect(Item item, int index, String text) {
        String rawItemId = BuiltInRegistries.ITEM.getKey(item).toString().replaceAll(":", ".");
        String[] split = rawItemId.split("\\.");
        String modId = split[0];
        String itemId = split[1];
        String inspectKey = "item." + modId + "." + itemId + ".inspection." + index;
        add(inspectKey, text);
    }

    @Override
    public void add(@NotNull String key, @NotNull String value) {
        Map<String, String> data = ((LanguageProviderAccessor) this).getData();
        if (!has(key))
            data.put(key, value);
    }
}
