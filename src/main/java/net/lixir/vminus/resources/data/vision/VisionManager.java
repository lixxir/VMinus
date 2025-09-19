package net.lixir.vminus.resources.data.vision;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.mixins.server.SimpleJsonResourceReloadListenerAccessor;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionEntry;
import net.lixir.vminus.vision.VisionType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

@SuppressWarnings("deprecated")
public class VisionManager<T> extends SimpleJsonResourceReloadListener {
    private static final Set<VisionManager<?>> VISION_MANAGERS = new HashSet<>();
    protected final Map<ResourceLocation, Vision> idToVisionMap = new HashMap<>();
    private final ICondition.IContext context;
    private final String directory;
    private final String singleListName;
    private final String multiListName;
    private final Gson gson;
    private final VisionType<T> visionType;
    private final Registry<T> registry;

    public VisionManager(@NotNull VisionType<T> visionType, Registry<T> registry, ICondition.IContext context) {
        super(new GsonBuilder()
                .registerTypeAdapter(VisionEntry.class, new VisionDeserializer<>(visionType.getMultiList(), visionType))
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create(), visionType.getDirectory());

        this.context = context;
        this.directory = visionType.getDirectory();
        this.singleListName = visionType.getId();
        this.multiListName = visionType.getMultiList();
        this.visionType = visionType;
        this.registry = registry;
        this.gson = ((SimpleJsonResourceReloadListenerAccessor) this).getGson();
        VISION_MANAGERS.add(this);
    }

    public static void clearVisionManagers() {
        VISION_MANAGERS.clear();
    }

    public static @NotNull @UnmodifiableView Set<VisionManager<?>> getVisionManagers() {
        return VISION_MANAGERS;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
                         @NotNull ResourceManager resourceManager,
                         @NotNull ProfilerFiller profilerFiller) {
        List<VisionEntry<T>> visionEntries = new ArrayList<>();

        // Load from data packs
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            loadVisionEntry(entry.getKey().toString(), entry.getValue(), visionEntries);
        }
        int datapackSize = visionEntries.size();
        if (datapackSize > 0)
            VMinus.LOGGER.info("Loaded {} {} visions from data", datapackSize, visionType.getId());

        // Load from config
        File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), directory);
        if (configDir.exists() && configDir.isDirectory()) {
            File[] files = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    try (Reader reader = new FileReader(file)) {
                        JsonElement element = JsonParser.parseReader(reader);
                        loadVisionEntry(file.getName(), element, visionEntries);
                    } catch (Exception e) {
                        VMinus.LOGGER.error("Error reading config file '{}':", file.getName(), e);
                    }
                }
            }
        }

        int configSize = visionEntries.size() - datapackSize;
        if (configSize > 0)
            VMinus.LOGGER.info("Loaded {} {} visions from config", configSize, visionType.getId());

        for (T value : registry) {
            ResourceLocation id = registry.getKey(value);
            if (id == null)
                continue;
            VisionDuck duck = ((VisionDuck) value);
            duck.vMinus$setVisionId(id);
            VisionEntry<T> mergedEntry = buildMergedEntry(value, id.toString(), visionEntries);

            if (!mergedEntry.isEmpty()) {
                Vision vision = Vision.fromEntry(id, mergedEntry, visionType);
                idToVisionMap.put(id, vision);
                visionType.putVision(id, vision);
                visionType.applyVision(value, id);
                duck.vMinus$update();
            }
        }
    }

    private @NotNull VisionEntry<T> buildMergedEntry(T value, String id, @NotNull List<VisionEntry<T>> visionEntries) {
        VisionEntry<T> mergedEntry = new VisionEntry<>();
        for (VisionEntry<T> visionEntry : visionEntries) {
            if (VisionEntry.visionApplies(value, id, visionEntry.getEntries(), context)) {
                mergedEntry.merge(visionEntry);
            }
        }
        return mergedEntry;
    }

    private void loadVisionEntry(String source, JsonElement element, List<VisionEntry<T>> outputList) {
        try {
            JsonObject processed = VisionFormatter.processJson(singleListName, multiListName, element);
            VisionEntry<T> entry = gson.fromJson(processed, new TypeToken<VisionEntry<T>>() {
            }.getType());
            outputList.add(entry);
        } catch (Exception e) {
            VMinus.LOGGER.error("Failed to load VisionEntry from '{}': {}", source, e.getMessage());
        }
    }

    public Set<Map.Entry<ResourceLocation, Vision>> getVisionEntries() {
        return idToVisionMap.entrySet();
    }

    public VisionType<T> getVisionType() {
        return visionType;
    }

    @Override
    public int hashCode() {
        return visionType.hashCode();
    }

    @Override
    public String toString() {
        return "VisionManager[" + singleListName + "]";
    }
}
