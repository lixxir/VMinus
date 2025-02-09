package net.lixir.vminus.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

public class VMinusRarities {
    public static final Rarity LEGENDARY = Rarity.create("legendary", ChatFormatting.GOLD);
    public static final Rarity INVERTED = Rarity.create("inverted", ChatFormatting.DARK_AQUA);
    public static final Rarity UNOBTAINABLE = Rarity.create("unobtainable", ChatFormatting.valueOf("INDIGO"));
    public static final Rarity DELICACY = Rarity.create("delicacy", ChatFormatting.valueOf("PINK"));
}
