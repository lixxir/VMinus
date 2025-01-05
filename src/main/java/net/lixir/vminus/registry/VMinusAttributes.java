/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.registry;

import net.lixir.vminus.VMinusMod;
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
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, VMinusMod.MODID);
    public static final RegistryObject<Attribute> PROTECTION = ATTRIBUTES.register("protection", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> BLASTPROTECTION = ATTRIBUTES.register("blast_protection", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".blast_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> MININGSPEED = ATTRIBUTES.register("mining_speed", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".mining_speed", 0, -255, 255)).setSyncable(true));
    public static final RegistryObject<Attribute> MAGICPROTECTION = ATTRIBUTES.register("magic_protection", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".magic_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> FALLPROTECTION = ATTRIBUTES.register("fall_protection", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".fall_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> BLUNTPROTECTION = ATTRIBUTES.register("blunt_protection", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".blunt_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> FIREPROTECTION = ATTRIBUTES.register("fire_protection", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".fire_protection", 0, -100, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> CRITICALDAMAGE = ATTRIBUTES.register("critical_damage", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".critical_damage", 0, 0, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> MOBDETECTIONRANGE = ATTRIBUTES.register("mob_detection_range", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".mob_detection_range", 0, -100, 0)).setSyncable(true));
    public static final RegistryObject<Attribute> HEALTHLOSTSTATBOOST = ATTRIBUTES.register("health_lost_stat_boost", () -> (new RangedAttribute("attribute." + VMinusMod.MODID + ".health_lost_stat_boost", 0, -100, 100)).setSyncable(true));

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> {
            ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
        });
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, PROTECTION.get());
        event.add(EntityType.PLAYER, BLASTPROTECTION.get());
        event.add(EntityType.PLAYER, MAGICPROTECTION.get());
        event.add(EntityType.PLAYER, FALLPROTECTION.get());
        event.add(EntityType.PLAYER, BLUNTPROTECTION.get());
        event.add(EntityType.PLAYER, FIREPROTECTION.get());
        event.add(EntityType.PLAYER, MOBDETECTIONRANGE.get());
    }
}
