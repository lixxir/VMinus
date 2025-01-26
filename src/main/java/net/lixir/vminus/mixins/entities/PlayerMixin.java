package net.lixir.vminus.mixins.entities;

import com.google.gson.JsonObject;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Unique
    private final Player vminus$player = (Player) (Object) this;

    @Inject(method = "eat", at = @At("HEAD"), cancellable = true)
    public void eat(Level level, ItemStack itemstack, CallbackInfoReturnable<ItemStack> cir) {
        JsonObject visionData = Vision.getData(itemstack);
        vminus$player.getFoodData().eat(itemstack.getItem(), itemstack);

        String burpSound = VisionProperties.getString(VisionProperties.Names.FOOD_PROPERTIES, visionData, VisionProperties.Names.BURP_SOUND, itemstack);
        if (burpSound != null && !burpSound.isEmpty()) {
            ResourceLocation resourceLocation = new ResourceLocation(burpSound);
            vminus$player.playSound(Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(resourceLocation)), 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }

        cir.setReturnValue(vminus$player.eat(level, itemstack));
    }
}
