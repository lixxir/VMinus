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

    @Unique
    private static final ChatFormatting INDIGO = vminus$addFormatting("INDIGO", 'g', hexToInt("8653fc"));
    @Unique
    private static final ChatFormatting LIGHT_PINK = vminus$addFormatting("LIGHT_PINK", 't',  hexToInt("f99dca"));
    @Unique
    private static final ChatFormatting PINK = vminus$addFormatting("PINK", 'q',  hexToInt("f771b2"));
    @Unique
    private static final ChatFormatting ORANGE = vminus$addFormatting("ORANGE", 'i', hexToInt("fc702a"));
    @Unique
    private static final ChatFormatting CORAL = vminus$addFormatting("CORAL", 'p', hexToInt("fc8c5f"));
    @Unique
    private static final ChatFormatting SKY_BLUE = vminus$addFormatting("SKY_BLUE", 'v', hexToInt("7badfc"));
    @Unique
    private static final ChatFormatting PINE = vminus$addFormatting("PINE", 'n', hexToInt("629646"));
    @Unique
    private static final ChatFormatting PLUM = vminus$addFormatting("PLUM", 'z', hexToInt("4d3b7f"));
    @Unique
    private static final ChatFormatting TOOTHPASTE = vminus$addFormatting("TOOTHPASTE", 'h', hexToInt("2afcd2"));
    @Unique
    private static final ChatFormatting NEON_YELLOW = vminus$addFormatting("NEON_YELLOW", 'y', hexToInt("fce305"));
    @Unique
    private static final ChatFormatting BROWN = vminus$addFormatting("BROWN", 'j', hexToInt("82522e"));
    @Unique
    private static final ChatFormatting DARK_BROWN = vminus$addFormatting("DARK_BROWN", 'u', hexToInt("3a2b1f"));
    @Unique
    private static final ChatFormatting NEON_RED = vminus$addFormatting("NEON_RED", 'x', hexToInt("fc0521"));
    @Unique
    private static final ChatFormatting COBALT = vminus$addFormatting("COBALT", 'C', hexToInt("0059ff"));

    @Invoker(value = "<init>")
    public static ChatFormatting chatFormattingInit(String internalName, int internalId, String p_12667_, char p_12668_, int p_12669_, @Nullable Integer p_12630_) {
        throw new AssertionError();
    }

    @Unique
    private static ChatFormatting vminus$addFormatting(String internalName, char code, int color) {
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