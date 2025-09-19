package net.lixir.vminus.world.entity;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionEntityVariant;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface VariantEntity {

   void vMinus$setVariant(ResourceLocation name, ResourceLocation texture);

   @Nullable ResourceLocation vMinus$getVariantTexture();

   @Nullable ResourceLocation vMinus$getVariantName();

   static @Nullable VisionEntityVariant setFromWeightedList(LivingEntity entity) {
      if (entity instanceof VariantEntity variantEntity) {
         List<VisionEntityVariant> entityVariants = Vision.get((VisionDuck)entity).getValues(VisionProperties.Entities.VARIANT, new VisionContext(entity));
         ArrayList<VisionEntityVariant> weightedEntityVariants = getWeightedVisionEntityVariants(entityVariants);

         VisionEntityVariant selectedVariant = !weightedEntityVariants.isEmpty()
                 ? weightedEntityVariants.get(Mth.nextInt(entity.getRandom(), 0, weightedEntityVariants.size() - 1))
                 : null;

         if (selectedVariant == null)
            return null;
         variantEntity.vMinus$setVariant(selectedVariant.name(), selectedVariant.texture());
         return selectedVariant;
      }


      return null;
   }

   static @NotNull ArrayList<VisionEntityVariant> getWeightedVisionEntityVariants(@NotNull List<VisionEntityVariant> entityVariants) {
      ArrayList<VisionEntityVariant> weightedEntityVariants = new ArrayList<>();
      boolean addNormal = true;
      for (VisionEntityVariant entityVariant : entityVariants) {
         ResourceLocation name = entityVariant.name();
         ResourceLocation texture = entityVariant.texture();
         if (name == null || texture == null)
            continue;
         if (name.getPath().isEmpty() || texture.getPath().isEmpty())
            continue;
          if (entityVariant.replace())
            addNormal = false;
         for (int i = 0; i < entityVariant.weight(); i++)
            weightedEntityVariants.add(entityVariant);
      }

      if (addNormal)
         weightedEntityVariants.add(new VisionEntityVariant(null, null, 1, false));
      return weightedEntityVariants;
   }
}
