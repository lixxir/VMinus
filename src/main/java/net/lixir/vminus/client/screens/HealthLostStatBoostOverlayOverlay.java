package net.lixir.vminus.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lixir.vminus.registry.VMinusAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class HealthLostStatBoostOverlayOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player entity = Minecraft.getInstance().player;
        if (entity != null) {
            Level world = entity.level();
            double currentHealth = entity.getHealth();
            double maxHealth = entity.getMaxHealth();
            float healthPercentage = (float) currentHealth / (float) maxHealth;
            float modifierSum = 0.0f;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack itemStack = entity.getItemBySlot(slot);
                if (!itemStack.isEmpty()) {
                    modifierSum += getAttributeValueFromItem(itemStack, slot, VMinusAttributes.HEALTHLOSTSTATBOOST.get());
                }
            }
            if (modifierSum > 0) {
                float transparency = 1.0f - healthPercentage;
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderSystem.setShaderColor(1, 1, 1, transparency);
                event.getGuiGraphics().blit(new ResourceLocation("vminus:textures/screens/stat_boost_damage_taken_overlay.png"), 0, 0, 0, 0, w, h, w, h);
                RenderSystem.depthMask(true);
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }
        }
    }

    private static float getAttributeValueFromItem(ItemStack itemStack, EquipmentSlot slot, Attribute attribute) {
        Collection<AttributeModifier> modifiers = itemStack.getAttributeModifiers(slot).get(attribute);
        if (modifiers != null && !modifiers.isEmpty()) {
            return (float) modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
        }
        return 0.0f;
    }
}
