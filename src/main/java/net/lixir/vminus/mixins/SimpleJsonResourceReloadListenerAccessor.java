package net.lixir.vminus.mixins;

import com.google.gson.Gson;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SimpleJsonResourceReloadListener.class)
public interface SimpleJsonResourceReloadListenerAccessor {
    @Accessor("gson")
    Gson getGson();
}
