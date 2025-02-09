/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VMinusAttributes {
    public static final String MOMENTUM_NBT_KEY = "MomentumStored";
    public static final String TRANSLUCENCE_KEY = "Translucency";
    public static final double MOMENTUM_DECAY_RATE = 0.003;
    public static final double MOMENTUM_BUILDUP_RATE = 0.001;
    public static final float TRANSLUCENCY_RATE = 0.01f;
    public static final String MOMENTUM_SPEED_NAME = "Momentum Speed Boost";

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, VMinus.ID);
    public static final RegistryObject<Attribute> PROTECTION = ATTRIBUTES.register("protection", () -> (new RangedAttribute("attribute." + VMinus.ID + ".protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> BLAST_PROTECTION = ATTRIBUTES.register("blast_protection", () -> (new RangedAttribute("attribute." + VMinus.ID + ".blast_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> MINING_SPEED = ATTRIBUTES.register("mining_speed", () -> (new RangedAttribute("attribute." + VMinus.ID + ".mining_speed", 0, -255, 255)).setSyncable(true));
    public static final RegistryObject<Attribute> MAGIC_PROTECTION = ATTRIBUTES.register("magic_protection", () -> (new RangedAttribute("attribute." + VMinus.ID + ".magic_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> FALL_PROTECTION = ATTRIBUTES.register("fall_protection", () -> (new RangedAttribute("attribute." + VMinus.ID + ".fall_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> BLUNT_PROTECTION = ATTRIBUTES.register("blunt_protection", () -> (new RangedAttribute("attribute." + VMinus.ID + ".blunt_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> FIRE_PROTECTION = ATTRIBUTES.register("fire_protection", () -> (new RangedAttribute("attribute." + VMinus.ID + ".fire_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = ATTRIBUTES.register("critical_damage", () -> (new RangedAttribute("attribute." + VMinus.ID + ".critical_damage", 0, 0, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> MOB_DETECTION_RANGE = ATTRIBUTES.register("mob_detection_range", () -> (new RangedAttribute("attribute." + VMinus.ID + ".mob_detection_range", 0, -100, 0)).setSyncable(true));
    public static final RegistryObject<Attribute> HEALTH_LOST_STAT_BOOST = ATTRIBUTES.register("health_lost_stat_boost", () -> (new RangedAttribute("attribute." + VMinus.ID + ".health_lost_stat_boost", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> MOMENTUM = ATTRIBUTES.register("momentum", () -> (new RangedAttribute("attribute." + VMinus.ID + ".momentum", 0, 0, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> TRANSLUCENCE = ATTRIBUTES.register("translucence", () -> (new RangedAttribute("attribute." + VMinus.ID + ".translucence", 0, 0, 100)).setSyncable(true));

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, PROTECTION.get());
        event.add(EntityType.PLAYER, BLAST_PROTECTION.get());
        event.add(EntityType.PLAYER, MAGIC_PROTECTION.get());
        event.add(EntityType.PLAYER, FALL_PROTECTION.get());
        event.add(EntityType.PLAYER, BLUNT_PROTECTION.get());
        event.add(EntityType.PLAYER, FIRE_PROTECTION.get());
        event.add(EntityType.PLAYER, CRITICAL_DAMAGE.get());
        event.add(EntityType.PLAYER, HEALTH_LOST_STAT_BOOST.get());
        event.add(EntityType.PLAYER, MOB_DETECTION_RANGE.get());
        event.add(EntityType.PLAYER, MOMENTUM.get());
        event.add(EntityType.PLAYER, TRANSLUCENCE.get());
    }
}
