package net.lixir.vminus.roles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoleSavedData extends SavedData {
    private final Map<UUID, String> playerRoles = new HashMap<>();

    public static @NotNull RoleSavedData load(@NotNull CompoundTag tag) {
        RoleSavedData data = new RoleSavedData();
        ListTag list = tag.getList("Roles", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag entry = (CompoundTag) t;
            data.playerRoles.put(UUID.fromString(entry.getString("UUID")), entry.getString("Role"));
        }
        return data;
    }

    public void removeRole(@NotNull ServerPlayer player) {
        playerRoles.remove(player.getUUID());
        setDirty();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, String> e : playerRoles.entrySet()) {
            CompoundTag entry = new CompoundTag();
            entry.putString("UUID", e.getKey().toString());
            entry.putString("Role", e.getValue());
            list.add(entry);
        }
        tag.put("Roles", list);
        return tag;
    }

    public void setRole(@NotNull ServerPlayer player, String role) {
        playerRoles.put(player.getUUID(), role);
        setDirty();
    }

    public String getRole(UUID uuid) {
        return playerRoles.getOrDefault(uuid, "");
    }

    public static @NotNull RoleSavedData get(@NotNull Level level) {
        return level.getServer().overworld().getDataStorage()
                .computeIfAbsent(RoleSavedData::load, RoleSavedData::new, "player_roles");
    }
}
