package net.lixir.vminus.item.trait;

import net.lixir.vminus.VMinus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemTraits {
    private static final String TRAITS_TAG = "ItemTraits";

    public static final DeferredRegister<ItemTrait> TRAITS = DeferredRegister.create(new ResourceLocation(VMinus.ID, "traits"), VMinus.ID);
    public static final Supplier<IForgeRegistry<ItemTrait>> TRAIT_REGISTRY = TRAITS.makeRegistry(() -> new RegistryBuilder<ItemTrait>()
            .setName(new ResourceLocation(VMinus.ID, "traits"))
            .setDefaultKey(new ResourceLocation(VMinus.ID, "default"))
            .setMaxID(Integer.MAX_VALUE - 1));

    public static final RegistryObject<ItemTrait> INSULATED = TRAITS.register("insulated", () -> new ItemTrait(new ResourceLocation(VMinus.ID, "insulated"), true));
    public static final RegistryObject<ItemTrait> PIGLIN_CHARM = TRAITS.register("piglin_charm", () -> new ItemTrait(new ResourceLocation(VMinus.ID, "piglin_charm"), true));
    public static final RegistryObject<ItemTrait> LIGHTFOOTED = TRAITS.register("lightfooted", () -> new ItemTrait(new ResourceLocation(VMinus.ID, "lightfooted"), true));

    public static boolean hasTrait(ItemStack stack, ItemTrait itemTrait) {
        if (!stack.hasTag()) return false;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG)) return false;
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        return traits.contains(itemTrait.resourceLocation().toString());
    }

    public static boolean getTrait(ItemStack stack, ItemTrait itemTrait) {
        if (!stack.hasTag()) return false;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG)) return false;
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        return traits.getBoolean(itemTrait.resourceLocation().toString());
    }

    public static ItemTrait fromId(ResourceLocation traitId) {
        return ItemTraits.TRAIT_REGISTRY.get().getValue(traitId);
    }

    public static void setTrait(ItemStack stack, ItemTrait itemTrait, boolean value) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        if (!tag.contains(TRAITS_TAG)) tag.put(TRAITS_TAG, new CompoundTag());
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        traits.putBoolean(itemTrait.resourceLocation().toString(), value);
    }

    public static List<ItemTrait> getTraits(ItemStack stack) {
        List<ItemTrait> itemTraitList = new ArrayList<>();

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG))
            return itemTraitList;

        CompoundTag traits = tag.getCompound(TRAITS_TAG);

        for (String key : traits.getAllKeys()) {
            ResourceLocation traitId = new ResourceLocation(key);
            ItemTrait itemTrait = TRAIT_REGISTRY.get().getValue(traitId);
            if (itemTrait != null) {
                itemTraitList.add(itemTrait);
            }
        }

        return itemTraitList;
    }

    public static void removeTrait(ItemStack stack, ItemTrait itemTrait) {
        if (!stack.hasTag()) return;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG)) return;
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        traits.remove(itemTrait.resourceLocation().toString());
        tag.put(TRAITS_TAG, traits);
    }

    public static void copyTraits(ItemStack source, ItemStack target) {
        if (!source.hasTag()) return;
        CompoundTag sourceTag = source.getTag();
        if (sourceTag == null || !sourceTag.contains(TRAITS_TAG)) return;
        CompoundTag sourceTraits = sourceTag.getCompound(TRAITS_TAG);
        if (!target.hasTag()) target.setTag(new CompoundTag());
        CompoundTag targetTag = target.getTag();
        if (targetTag == null) return;
        targetTag.put(TRAITS_TAG, sourceTraits.copy());
    }
}
