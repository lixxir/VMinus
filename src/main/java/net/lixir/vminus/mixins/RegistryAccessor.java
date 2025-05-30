package net.lixir.vminus.mixins;

import com.mojang.serialization.Keyable;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Registry.class)
public interface RegistryAccessor<T> extends Keyable, IdMap<T> {

    @Invoker("register")
    static <T> T addRegistry(Registry<? super T> registry, ResourceLocation id, T value) {
        throw new UnsupportedOperationException();
    }
}
