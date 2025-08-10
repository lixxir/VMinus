package net.lixir.vminus.roles;

import java.util.Set;

public record Role(String name, Set<String> allowedCommands, boolean canUseGameMasterBlocks) {
}
