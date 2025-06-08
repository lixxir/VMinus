package net.lixir.vminus.mixins;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.SoundDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundDefinition.Sound.class)
public interface SoundDefinitionSoundAccessor {
    @Accessor("type")
    SoundDefinition.SoundType getType();

    @Accessor("name")
    ResourceLocation getName();
}