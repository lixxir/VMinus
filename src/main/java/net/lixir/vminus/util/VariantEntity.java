package net.lixir.vminus.util;

import net.lixir.vminus.vision.util.VisionEntityVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface VariantEntity {

   void vminus$setVariant(ResourceLocation name, ResourceLocation texture);

   @Nullable ResourceLocation vminus$getVariantTexture();

   @Nullable ResourceLocation vminus$getVariantName();

   static @Nullable VisionEntityVariant setFromWeightedList(LivingEntity entity) {
      /*
      if (entity instanceof VariantEntity variantEntity) {
         List<VisionEntityVariant> entityVariants = EntityVision.of(entity).variant.values(new VisionContext(entity));
         ArrayList<VisionEntityVariant> weightedEntityVariants = getWeightedVisionEntityVariants(entityVariants);

         VisionEntityVariant selectedVariant = !weightedEntityVariants.isEmpty()
                 ? weightedEntityVariants.get(Mth.nextInt(RandomSource.create(), 0, weightedEntityVariants.size() - 1))
                 : null;

         if (selectedVariant == null)
            return null;
         variantEntity.vminus$setVariant(selectedVariant.name(), selectedVariant.texture());
         return selectedVariant;
      }

       */
      return null;
   }

   static @NotNull ArrayList<VisionEntityVariant> getWeightedVisionEntityVariants(List<VisionEntityVariant> entityVariants) {
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
