package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;

@Mixin(SoundDefinitionsProvider.class)
public interface SoundDefinitionsProviderAccessor {
    @Accessor("sounds")
    Map<String, SoundDefinition> getSoundList();
    @Invoker("mapToJson")
    JsonObject invokeMapToJson(final Map<String, SoundDefinition> map);
}
