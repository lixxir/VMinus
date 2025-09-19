package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.api.datagen.lang.provider.VLangProvider;
import net.minecraft.data.PackOutput;

public class VMinusLangProvider extends VLangProvider {
    public VMinusLangProvider(PackOutput output, String locale) {
        super(output, VMinus.ID, locale);
    }

    @Override
    protected void addTranslations() {
        super.addTranslations();
        add("attribute.vminus.key.mining_speed", "Mining Speed");
        add("attribute.vminus.key.magic_protection", "Magic Protection");
        add("attribute.vminus.key.fire_protection", "Fire Protection");
        add("attribute.vminus.key.protection", "General Protection");
        add("attribute.vminus.key.fall_protection", "Fall Protection");
        add("attribute.vminus.key.blunt_protection", "Blunt Protection");
        add("attribute.vminus.key.blast_protection", "Blast Protection");

        add("itemTrait.vminus.piglin_charm", "Piglin Charm");
        add("itemTrait.vminus.insulated", "Insulated");
        add("itemTrait.vminus.lightfooted", "Lightfooted");

        add("rarity.common", "Common");
        add("rarity.uncommon", "Uncommon");
        add("rarity.rare", "Rare");
        add("rarity.epic", "Epic");
        add("rarity.delicacy", "Delicacy");
        add("rarity.legendary", "Legendary");
        add("rarity.inverted", "Inverted");
        add("rarity.unobtainable", "Unobtainable");
    }
}
