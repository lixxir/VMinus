package net.lixir.vminus.resources.data.vision.codec;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.UseAnim;

public class VisionCodecs {
    // Collection of static instances of VisionCodecs for parsing values from JSONs
    public static final VisionIntegerCodec INTEGER = new VisionIntegerCodec();
    public static final VisionFloatCodec FLOAT = new VisionFloatCodec();
    public static final VisionResourceLocationCodec RESOURCE_LOCATION = new VisionResourceLocationCodec();
    public static final VisionDoubleCodec DOUBLE = new VisionDoubleCodec();
    public static final VisionIntegerNumberRangeCodec INTEGER_RANGE = new VisionIntegerNumberRangeCodec();
    public static final VisionCreativeOrderCodec CREATIVE_ORDER = new VisionCreativeOrderCodec();
    public static final VisionBaseAttributeCodec BASE_ATTRIBUTE = new VisionBaseAttributeCodec();
    public static final VisionEntityVariantCodec ENTITY_VARIANT = new VisionEntityVariantCodec();
    public static final VisionSoundEventCodec SOUND_EVENT = new VisionSoundEventCodec();
    public static final VisionBooleanCodec BOOLEAN = new VisionBooleanCodec();
    public static final VisionItemReplacementCodec ITEM_REPLACEMENT = new VisionItemReplacementCodec();
    public static final VisionBlockCodec BLOCK = new VisionBlockCodec();
    public static final VisionItemStackCodec ITEM_STACK = new VisionItemStackCodec();
    public static final VisionRarityCodec RARITY = new VisionRarityCodec();
    public static final VisionHexCodec HEX = new VisionHexCodec();
    public static final VisionSoundTypeCodec SOUND_TYPE = new VisionSoundTypeCodec();
    public static final VisionAttributeCodec ATTRIBUTE = new VisionAttributeCodec();
    public static final VisionFoodPropertiesCodec FOOD_PROPERTIES = new VisionFoodPropertiesCodec();
    public static final VisionEnumCodec<UseAnim> USE_ANIMATION = new VisionEnumCodec<>(UseAnim.class);
    public static final VisionEnumCodec<MobEffectCategory> EFFECT_CATEGORY = new VisionEnumCodec<>(MobEffectCategory.class);
    public static final VisionEnumCodec<EquipmentSlot> EQUIP_SLOT = new VisionEnumCodec<>(EquipmentSlot.class);
}
