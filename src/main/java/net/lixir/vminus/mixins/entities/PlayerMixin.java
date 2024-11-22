package net.lixir.vminus.mixins.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> p_250508_, Level p_250289_) {
        super(p_250508_, p_250289_);
    }

    @Shadow
    public abstract void playSound(SoundEvent p_36182_, float p_36183_, float p_36184_);

    @Shadow
    public abstract FoodData getFoodData();

    @Inject(method = "eat", at = @At("HEAD"), cancellable = true)
    public void eat(Level level, ItemStack itemstack, CallbackInfoReturnable<ItemStack> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        getFoodData().eat(itemstack.getItem(), itemstack);
        if (itemData != null && itemData.has("food_properties")) {
            JsonArray foodPropertiesArray = itemData.getAsJsonArray("food_properties");
            for (JsonElement element : foodPropertiesArray) {
                if (element.isJsonObject()) {
                    JsonObject foodProperties = element.getAsJsonObject();
                    if (foodProperties.has("burp_sound")) {
                        String soundName = foodProperties.get("burp_sound").getAsString();
                        SoundEvent eatSound = SoundHelper.getSoundEventFromString(soundName);
                        if (eatSound != null) {
                            playSound(eatSound, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                        }
                    }
                }
            }
        }
        cir.setReturnValue(super.eat(level, itemstack));
    }
}
