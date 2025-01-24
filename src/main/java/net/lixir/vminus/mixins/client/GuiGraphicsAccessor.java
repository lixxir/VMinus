package net.lixir.vminus.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Collection;
import java.util.Set;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Accessor("pose")
    PoseStack getPoseStack();

    @Accessor("bufferSource")
    MultiBufferSource.BufferSource getBufferSource();
}