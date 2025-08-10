package net.lixir.vminus.roles;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoleManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    public static final RoleManager INSTANCE = new RoleManager();
    private final Map<String, Role> roles = new HashMap<>();

    public RoleManager() {
        super(GSON, "roles");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsonMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) throws JsonParseException {
        roles.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            try {
                JsonObject jsonObject = entry.getValue().getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                Set<String> commands = new HashSet<>();
                jsonObject.getAsJsonArray("allowed_commands").forEach(el -> commands.add(el.getAsString()));
                boolean canUseCommandBlocks = jsonObject.has("can_use_command_blocks") && jsonObject.get("can_use_command_blocks").getAsBoolean();
                roles.put(name, new Role(name, commands, canUseCommandBlocks));
            } catch (Exception e) {
               throw new JsonParseException(e);
            }
        }
    }

    public Set<String> getAllRoleNames() {
        return new HashSet<>(roles.keySet());
    }

    public @Nullable Role getRole(String name) {
        return roles.get(name);
    }
}
