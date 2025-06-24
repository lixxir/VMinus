package net.lixir.vminus.vision.resource.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.mixins.server.SimpleJsonResourceReloadListenerAccessor;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionEntry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.resource.VisionDeserializer;
import net.lixir.vminus.vision.resource.VisionProcessor;
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

public class VisionManager<T> extends SimpleJsonResourceReloadListener {
    private static final List<VisionManager<?>> VISION_MANAGERS = new ArrayList<>();

    private final ICondition.IContext context;
    private final String directory;
    private final String singleListName;
    private final String multiListName;
    private final Gson gson;
    private final VisionType<T> visionType;
    private final Registry<T> registry;

    protected final Map<ResourceLocation, Vision> idToVisionMap = new HashMap<>();
    protected final Map<Vision, ResourceLocation> visionToIdMap = new HashMap<>();

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

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
                         @NotNull ResourceManager resourceManager,
                         @NotNull ProfilerFiller profilerFiller) {
        List<VisionEntry<T>> visionEntries = new ArrayList<>();

        // Load from data packs
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            loadVisionEntry(entry.getKey().toString(), entry.getValue(), visionEntries);
        }

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

        for (T value : registry) {
            ResourceLocation id = registry.getKey(value);
            if (id == null)
                continue;
            VisionEntry<T> mergedEntry = new VisionEntry<>();
            for (VisionEntry<T> visionEntry : visionEntries) {
                if (VisionEntry.visionApplies(value, id.toString(), visionEntry.getEntries(), context)) {
                    mergedEntry.merge(visionEntry);
                }
            }

            ((VisionDuck)value).vMinus$setVisionId(id);
            if (!mergedEntry.isEmpty()) {
                Vision vision = Vision.fromEntry(id, mergedEntry, visionType);
                idToVisionMap.put(id, vision);
                visionToIdMap.put(vision, id);
                this.visionType.applyVision(value, id);
            }
        }

    }

    private void loadVisionEntry(String source, JsonElement element, List<VisionEntry<T>> outputList) {
        try {
            JsonObject processed = VisionProcessor.processJson(singleListName, multiListName, element);
            VisionEntry<T> entry = gson.fromJson(processed, new TypeToken<VisionEntry<T>>() {}.getType());
            outputList.add(entry);
        } catch (Exception e) {
            VMinus.LOGGER.error("Failed to load VisionEntry from '{}': {}", source, e.getMessage());
        }
    }

    public static void clearVisionManagers() {
        VISION_MANAGERS.clear();
    }

    public static @NotNull @UnmodifiableView List<VisionManager<?>> getVisionManagers() {
        return VISION_MANAGERS;
    }

    public Set<Map.Entry<ResourceLocation, Vision>> getVisionEntries() {
        return idToVisionMap.entrySet();
    }

    public ResourceLocation idFromVision(Vision vision) {
        return visionToIdMap.get(vision);
    }

    public VisionType<T> getVisionType() {
        return visionType;
    }

    @Override
    public String toString() {
        return "VisionManager[" + singleListName + "]";
    }
}
