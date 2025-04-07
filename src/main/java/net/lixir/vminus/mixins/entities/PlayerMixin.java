package net.lixir.vminus.mixins.entities;

import com.google.gson.JsonObject;
import net.lixir.vminus.util.SizeAttributeUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Unique
    private final Player vminus$player = (Player) (Object) this;

    @ModifyArg(
            method = "eat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
            ),
            index = 4
    )
    private SoundEvent changeBurpSound(SoundEvent originalSound) {
        /*
        ItemStack itemstack = vminus$player.getUseItem();
        JsonObject visionData = Visions.getData(itemstack);
        vminus$player.getFoodData().eat(itemstack.getItem(), itemstack);

        String burpSound = VisionProperties.getString(VisionProperties.Names.FOOD_PROPERTIES, visionData, VisionProperties.Names.BURP_SOUND, itemstack);
        if (burpSound != null && !burpSound.isEmpty()) {
            ResourceLocation resourceLocation = new ResourceLocation(burpSound);
            return Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(resourceLocation));
        }

         */
        return originalSound;
    }

    @Inject(method = "getDimensions", at = @At(value = "RETURN"), cancellable = true)
    public void getDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        float defaultHeight = cir.getReturnValue().height;
        float defaultWidth = cir.getReturnValue().width;
        float width = defaultWidth * SizeAttributeUtil.getWidth(vminus$player);
        float height = defaultHeight * SizeAttributeUtil.getHeight(vminus$player);
        if (width != defaultWidth && height != defaultHeight)
            cir.setReturnValue(EntityDimensions.scalable(width, height));
    }


}
