package net.lixir.vminus.mixins.data.sounddefinition;

import net.minecraftforge.common.data.SoundDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(SoundDefinition.class)
public interface SoundDefinitionAccessor {
    @Invoker("soundList")
    List<SoundDefinition.Sound> getSoundList();
}