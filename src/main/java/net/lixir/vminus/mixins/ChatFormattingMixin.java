package net.lixir.vminus.mixins;

import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

@Mixin(ChatFormatting.class)
@SuppressWarnings({"target", "unchecked"})
@Unique
public abstract class ChatFormattingMixin {
    @Shadow
    @Final
    @Mutable
    private static ChatFormatting[] $VALUES;

    private static final ChatFormatting INDIGO = addFormatting("INDIGO", 'g', hexToInt("8653fc"));
    private static final ChatFormatting LIGHT_PINK = addFormatting("LIGHT_PINK", 't',  hexToInt("f99dca"));
    private static final ChatFormatting PINK = addFormatting("PINK", 'q',  hexToInt("f771b2"));
    private static final ChatFormatting ORANGE = addFormatting("ORANGE", 'i', hexToInt("fc702a"));
    private static final ChatFormatting CORAL = addFormatting("CORAL", 'p', hexToInt("fc8c5f"));
    private static final ChatFormatting SKY_BLUE = addFormatting("SKY_BLUE", 'v', hexToInt("7badfc"));
    private static final ChatFormatting PINE = addFormatting("PINE", 'n', hexToInt("629646"));
    private static final ChatFormatting PLUM = addFormatting("PLUM", 'z', hexToInt("4d3b7f"));
    private static final ChatFormatting TOOTHPASTE = addFormatting("TOOTHPASTE", 'h', hexToInt("2afcd2"));
    private static final ChatFormatting NEON_YELLOW = addFormatting("NEON_YELLOW", 'y', hexToInt("fce305"));
    private static final ChatFormatting BROWN = addFormatting("BROWN", 'j', hexToInt("82522e"));
    private static final ChatFormatting DARK_BROWN = addFormatting("DARK_BROWN", 'u', hexToInt("3a2b1f"));
    private static final ChatFormatting NEON_RED = addFormatting("NEON_RED", 'x', hexToInt("fc0521"));
    private static final ChatFormatting COBALT = addFormatting("COBALT", 'C', hexToInt("0059ff"));
    @Invoker(value = "<init>")
    public static ChatFormatting chatFormattingInit(String internalName, int internalId, String p_12667_, char p_12668_, int p_12669_, @Nullable Integer p_12630_) {
        throw new AssertionError();
    }

    private static ChatFormatting addFormatting(String internalName, char code, int color) {
        ArrayList<ChatFormatting> categories = new ArrayList<>(Arrays.asList(ChatFormattingMixin.$VALUES));
        int id = categories.get(categories.size() - 1).ordinal() + 1;
        ChatFormatting category = chatFormattingInit(internalName, id, internalName, code, id, color);

        categories.add(category);
        ChatFormattingMixin.$VALUES = categories.toArray(new ChatFormatting[0]);
        return category;
    }
    private static int hexToInt(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }
        return Integer.parseInt(hexColor, 16);
    }
}