package net.lixir.vminus.resources.asset;
import net.lixir.vminus.VMinus;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VMinusFonts {
    public static final ResourceLocation ICONS = font("icons");

    @Contract(pure = true)
    private static @NotNull ResourceLocation font(String location) {
        return ResourceLocation.fromNamespaceAndPath(VMinus.ID, location);
    }
}
