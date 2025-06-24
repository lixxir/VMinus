package net.lixir.vminus.mixins.server;

import com.google.gson.Gson;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleJsonResourceReloadListener.class)
public interface SimpleJsonResourceReloadListenerAccessor {
    @Accessor("gson")
    Gson getGson();
}
