package net.lixir.vminus.visions.resources;

import net.lixir.vminus.visions.resources.codec.*;

public class VisionCodecs {
    private static final VisionIntCodec VISION_INT_CODEC = new VisionIntCodec();
    private static final VisionFloatCodec VISION_FLOAT_CODEC = new VisionFloatCodec();
    private static final VisionDoubleCodec VISION_DOUBLE_CODEC = new VisionDoubleCodec();
    private static final VisionCreativeOrderCodec VISION_CREATIVE_ORDER_CODEC = new VisionCreativeOrderCodec();
    private static final VisionBooleanCodec VISION_BOOLEAN_CODEC = new VisionBooleanCodec();
    private static final VisionItemReplacementCodec VISION_ITEM_REPLACEMENT_CODEC = new VisionItemReplacementCodec();
    private static final VisionItemStackCodec VISION_ITEM_STACK_CODEC = new VisionItemStackCodec();
    private static final VisionHexCodec VISION_HEX_CODEC = new VisionHexCodec();

    public static VisionItemStackCodec itemStackCodec() {
        return VISION_ITEM_STACK_CODEC;
    }

    public static VisionItemReplacementCodec itemReplacementCodec() {
        return VISION_ITEM_REPLACEMENT_CODEC;
    }

    public static VisionFloatCodec floatCodec() {
        return VISION_FLOAT_CODEC;
    }

    public static VisionDoubleCodec doubleCodec() {
        return VISION_DOUBLE_CODEC;
    }

    public static VisionIntCodec intCodec() {
        return VISION_INT_CODEC;
    }

    public static VisionCreativeOrderCodec creativeOrderCodec() {
        return VISION_CREATIVE_ORDER_CODEC;
    }

    public static VisionBooleanCodec booleanCodec() {
        return VISION_BOOLEAN_CODEC;
    }

    public static VisionHexCodec hexCodec() {
        return VISION_HEX_CODEC;
    }
}
