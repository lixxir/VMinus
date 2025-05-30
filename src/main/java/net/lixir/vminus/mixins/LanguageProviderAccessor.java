package net.lixir.vminus.mixins;

import net.minecraftforge.common.data.LanguageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(LanguageProvider.class)
public interface LanguageProviderAccessor {
    @Accessor("data")
    Map<String, String> getData();
}
