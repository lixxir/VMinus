package net.lixir.vminus.attribute;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.lixir.vminus.VMinus.REGISTRY;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VMinusAttributes {
    public static void init() {}

    public static final Attribute PROTECTION = REGISTRY.attribute("protection", 0, 0, 100);
    public static final Attribute BLAST_PROTECTION = REGISTRY.attribute("blast_protection", 0, 0, 100);
    public static final Attribute MINING_SPEED = REGISTRY.attribute("mining_speed", 0, 0, 100);
    public static final Attribute MAGIC_PROTECTION = REGISTRY.attribute("magic_protection", 0, 0, 1024);
    public static final Attribute FALL_PROTECTION = REGISTRY.attribute("fall_protection", 0, 0, 1024);
    public static final Attribute BLUNT_PROTECTION = REGISTRY.attribute("blunt_protection", 0, 0, 1024);
    public static final Attribute FIRE_PROTECTION = REGISTRY.attribute("fire_protection", 0, 0, 1024);
    public static final Attribute CRITICAL_DAMAGE = REGISTRY.attribute("critical_damage", 0, 0, 1024);
    public static final Attribute MOB_DETECTION_RANGE = REGISTRY.attribute("mob_detection_range", 0, 0, 0);
    public static final Attribute JUMP_BOOST = REGISTRY.attribute("jump_boost", 0, 0, 100);
    public static final Attribute WIDTH = REGISTRY.attribute("width", 1, 0.2, 5);
    public static final Attribute HEIGHT = REGISTRY.attribute("height", 1, 0.2, 5);

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, PROTECTION);
        event.add(EntityType.PLAYER, BLAST_PROTECTION);
        event.add(EntityType.PLAYER, MAGIC_PROTECTION);
        event.add(EntityType.PLAYER, FALL_PROTECTION);
        event.add(EntityType.PLAYER, BLUNT_PROTECTION);
        event.add(EntityType.PLAYER, FIRE_PROTECTION);
        event.add(EntityType.PLAYER, CRITICAL_DAMAGE);
        event.add(EntityType.PLAYER, MOB_DETECTION_RANGE);
        event.add(EntityType.PLAYER, JUMP_BOOST);
        event.add(EntityType.PLAYER, WIDTH, 0.9375F);
        event.add(EntityType.PLAYER, HEIGHT, 0.9375F);
    }

}
