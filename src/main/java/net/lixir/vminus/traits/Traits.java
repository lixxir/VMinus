package net.lixir.vminus.traits;

import net.lixir.vminus.VMinus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Traits {
    private static final String TRAITS_TAG = "Traits";
    public static final DeferredRegister<Trait> TRAITS = DeferredRegister.create(new ResourceLocation(VMinus.ID, "traits"), VMinus.ID);
    public static final Supplier<IForgeRegistry<Trait>> TRAIT_REGISTRY = TRAITS.makeRegistry(() -> new RegistryBuilder<Trait>()
                            .setName(new ResourceLocation(VMinus.ID, "traits"))
                            .setDefaultKey(new ResourceLocation(VMinus.ID, "default"))
                            .setMaxID(Integer.MAX_VALUE - 1));

    public static final RegistryObject<Trait> VEIL = TRAITS.register("veil", () -> new Trait(new ResourceLocation(VMinus.ID, "veil"), true));
    public static final RegistryObject<Trait> INSULATED = TRAITS.register("insulated", () -> new Trait(new ResourceLocation(VMinus.ID, "insulated"), true));
    public static final RegistryObject<Trait> PIGLIN_CHARM = TRAITS.register("piglin_charm", () -> new Trait(new ResourceLocation(VMinus.ID, "piglin_charm"), true));
    public static final RegistryObject<Trait> LIGHTFOOTED = TRAITS.register("lightfooted", () -> new Trait(new ResourceLocation(VMinus.ID, "lightfooted"), true));
    public static final RegistryObject<Trait> SURGE = TRAITS.register("surge", () -> new Trait(new ResourceLocation(VMinus.ID, "surge"), false));

    public static boolean hasTrait(ItemStack stack, Trait trait) {
        if (!stack.hasTag()) return false;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG)) return false;
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        return traits.contains(trait.getResourceLocation().toString());
    }

    public static boolean getTrait(ItemStack stack, Trait trait) {
        if (!stack.hasTag()) return false;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG)) return false;
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        return traits.getBoolean(trait.getResourceLocation().toString());
    }

    public static void setTrait(ItemStack stack, Trait trait, boolean value) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        if (!tag.contains(TRAITS_TAG)) tag.put(TRAITS_TAG, new CompoundTag());
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        traits.putBoolean(trait.getResourceLocation().toString(), value);
    }

    public static List<Trait> getTraits(ItemStack stack) {
        List<Trait> traitList = new ArrayList<>();

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG))
            return traitList;

        CompoundTag traits = tag.getCompound(TRAITS_TAG);

        for (String key : traits.getAllKeys()) {
            ResourceLocation traitId = new ResourceLocation(key);
            Trait trait = Traits.TRAIT_REGISTRY.get().getValue(traitId);
            if (trait != null) {
                traitList.add(trait);
            }
        }

        return traitList;
    }

    public static void removeTrait(ItemStack stack,Trait trait) {
        if (!stack.hasTag()) return;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TRAITS_TAG)) return;
        CompoundTag traits = tag.getCompound(TRAITS_TAG);
        traits.remove(trait.getResourceLocation().toString());
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
