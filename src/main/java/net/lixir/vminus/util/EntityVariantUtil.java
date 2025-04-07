package net.lixir.vminus.util;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.EntityVision;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityVariantUtil {
    public static CompoundTag setOrGetVariant(Entity entity) {
        CompoundTag variantTag = entity.getPersistentData().getCompound("variant");
        String chosenVariant = variantTag.getString("name");
        if (chosenVariant.isEmpty()) {
            CompoundTag newVariantTag = new CompoundTag();
            List<VisionEntityVariant> entityVariants = EntityVision.of(entity).variant.values(new VisionConditionArguments(entity));
            ArrayList<VisionEntityVariant> weightedEntityVariants = getWeightedVisionEntityVariants(entityVariants);

            VisionEntityVariant selectedVariant = !weightedEntityVariants.isEmpty() ? weightedEntityVariants.get(Mth.nextInt(RandomSource.create(), 0, weightedEntityVariants.size()-1)) : null;
            if (selectedVariant == null)
                return null;

            String variantName = selectedVariant.name();
            newVariantTag.putString("name", variantName);
            newVariantTag.putString("texture", variantName.equals("normal") ? "null" : selectedVariant.texture().toString());
            entity.getPersistentData().put("variant", newVariantTag);
            return newVariantTag;
        }
        return variantTag;
    }

    private static @NotNull ArrayList<VisionEntityVariant> getWeightedVisionEntityVariants(List<VisionEntityVariant> entityVariants) {
        ArrayList<VisionEntityVariant> weightedEntityVariants = new ArrayList<>();
        boolean addNormal = true;
        for (VisionEntityVariant entityVariant : entityVariants) {
            if (entityVariant.replace()) {
                addNormal = false;
            }
            for (int i = 0; i < entityVariant.weight(); i++) weightedEntityVariants.add(entityVariant);

        }

        if (addNormal)
            weightedEntityVariants.add(new VisionEntityVariant("normal", null, 1, false));
        return weightedEntityVariants;
    }

    public static void setVariant(Entity entity, @Nullable String name, @Nullable String texture) {
        if (name != null && !name.isEmpty() && texture != null && !texture.isEmpty()) {
            CompoundTag variantTag = new CompoundTag();
            variantTag.putString("name", name);
            variantTag.putString("texture", texture);
            entity.getPersistentData().put("variant", variantTag);
        }
    }

    public static String getVariantName(Entity entity) {
        CompoundTag variantTag = entity.getPersistentData().getCompound("variant");
        String name = variantTag.getString("name");
        return name.isEmpty() ? "normal" : name;
    }

    public static @Nullable String getVariantTexture(Entity entity) {
        CompoundTag variantTag = entity.getPersistentData().getCompound("variant");
        String texture =  variantTag.getString("texture");
        return texture.equals("null") ? null : texture;
    }

}
