package net.lixir.vminus.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HorseArmorLayer.class)
public class HorseArmorLayerMixin {
    // Adds the enchantment layer glint to horse armor.
    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", shift = At.Shift.AFTER))
    private VertexConsumer render(VertexConsumer builderIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Horse horse, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
                                  float headPitch) {
        ItemStack itemstack = horse.getArmor();
        HorseArmorItem horseArmor = (HorseArmorItem) itemstack.getItem();
        return ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.entityCutoutNoCull(horseArmor.getTexture()), false, itemstack.hasFoil());
    }
}
