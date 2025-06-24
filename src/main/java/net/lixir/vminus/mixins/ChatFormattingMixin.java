package net.lixir.vminus.mixins;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

@Mixin(ChatFormatting.class)
@SuppressWarnings({"targetItemStack"})
@Unique
public abstract class ChatFormattingMixin {
    @Shadow
    @Final
    @Mutable
    private static ChatFormatting[] $VALUES;

    static {
        vMinus$addFormatting("INDIGO", 'g', vMinus$hexToInt("8653fc"));
        vMinus$addFormatting("LIGHT_PINK", 't', vMinus$hexToInt("f99dca"));
        vMinus$addFormatting("PINK", 'q', vMinus$hexToInt("f771b2"));
        vMinus$addFormatting("ORANGE", 'i', vMinus$hexToInt("fc702a"));
        vMinus$addFormatting("CORAL", 'p', vMinus$hexToInt("fc8c5f"));
        vMinus$addFormatting("SKY_BLUE", 'v', vMinus$hexToInt("7badfc"));
        vMinus$addFormatting("PINE", 'n', vMinus$hexToInt("629646"));
        vMinus$addFormatting("PLUM", 'z', vMinus$hexToInt("4d3b7f"));
        vMinus$addFormatting("TOOTHPASTE", 'h', vMinus$hexToInt("2afcd2"));
        vMinus$addFormatting("NEON_YELLOW", 'y', vMinus$hexToInt("fce305"));
        vMinus$addFormatting("BROWN", 'j', vMinus$hexToInt("82522e"));
        vMinus$addFormatting("DARK_BROWN", 'u', vMinus$hexToInt("3a2b1f"));
        vMinus$addFormatting("NEON_RED", 'x', vMinus$hexToInt("fc0521"));
    }

    @Invoker(value = "<init>")
    public static ChatFormatting vMinus$init(String internalName, int internalId, String p_12667_, char p_12668_, int p_12669_, @Nullable Integer p_12630_) {
        throw new AssertionError();
    }

    @Unique
    private static void vMinus$addFormatting(String internalName, char code, int color) {
        ArrayList<ChatFormatting> categories = new ArrayList<>(Arrays.asList(ChatFormattingMixin.$VALUES));
        int id = categories.get(categories.size() - 1).ordinal() + 1;
        ChatFormatting category = vMinus$init(internalName, id, internalName, code, id, color);

        categories.add(category);
        ChatFormattingMixin.$VALUES = categories.toArray(new ChatFormatting[0]);
    }

    @Unique
    private static int vMinus$hexToInt(@NotNull String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }
        return Integer.parseInt(hexColor, 16);
    }
}