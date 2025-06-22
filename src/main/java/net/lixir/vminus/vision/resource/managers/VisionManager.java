package net.lixir.vminus.vision.resource.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.mixins.SimpleJsonResourceReloadListenerAccessor;
import net.lixir.vminus.vision.VisionEntry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.resource.VisionDeserializer;
import net.lixir.vminus.vision.resource.VisionProcessor;
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

public abstract class VisionManager<T> extends SimpleJsonResourceReloadListener {
    private final ICondition.IContext context;
    private final String directory;
    private final String singleListName;
    private final String multiListName;
    private final Gson gson;
    private final VisionType visionType;
    protected final Map<ResourceLocation, Integer> visionIndexes = new HashMap<>();
    protected final Map<ResourceLocation, VisionEntry<T>> visionEntries = new HashMap<>();

    private static final List<VisionManager<?>> VISION_MANAGERS = new ArrayList<>();

    protected VisionManager(ICondition.IContext context, @NotNull VisionType visionType) {
        super(new GsonBuilder()
                .registerTypeAdapter(VisionEntry.class, new VisionDeserializer<>(visionType.multiList(), visionType.classType()))
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create(), visionType.directory());

        this.context = context;
        this.directory = visionType.directory();
        this.singleListName = visionType.id();
        this.multiListName = visionType.multiList();
        this.visionType = visionType;
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

        applyVisions(visionEntries);
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

    public Map<ResourceLocation, Integer> getVisionIndexes() {
        return visionIndexes;
    }

    public ICondition.IContext getContext() {
        return context;
    }

    public VisionType getVisionType() {
        return visionType;
    }

    protected void applyVisions(List<VisionEntry<T>> visionEntries) {
        visionIndexes.clear();
    }
}
