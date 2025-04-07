package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
    public static final String TRANSLUCENCE_KEY = "Translucency";
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, VMinus.ID);

    public static final RegistryObject<Attribute> PROTECTION = register("protection", 0, -100, 100);
    public static final RegistryObject<Attribute> BLAST_PROTECTION = register("blast_protection", 0, -100, 100);
    public static final RegistryObject<Attribute> MINING_SPEED = register("mining_speed", 0, -100, 100);
    public static final RegistryObject<Attribute> MAGIC_PROTECTION = register("magic_protection", 0, -100, 100);
    public static final RegistryObject<Attribute> FALL_PROTECTION = register("fall_protection", 0, -100, 100);
    public static final RegistryObject<Attribute> BLUNT_PROTECTION = register("blunt_protection", 0, -100, 100);
    public static final RegistryObject<Attribute> FIRE_PROTECTION = register("fire_protection", 0, -100, 100);
    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = register("critical_damage", 0, -100, 100);
    public static final RegistryObject<Attribute> MOB_DETECTION_RANGE = register("mob_detection_range", 0, -100, 0);
    public static final RegistryObject<Attribute> JUMP_BOOST = register("jump_boost", 0, -100, 100);
    public static final RegistryObject<Attribute> WIDTH = register("width", 1, 0.2, 5);
    public static final RegistryObject<Attribute> HEIGHT = register("height", 1, 0.2, 5);

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, PROTECTION.get());
        event.add(EntityType.PLAYER, BLAST_PROTECTION.get());
        event.add(EntityType.PLAYER, MAGIC_PROTECTION.get());
        event.add(EntityType.PLAYER, FALL_PROTECTION.get());
        event.add(EntityType.PLAYER, BLUNT_PROTECTION.get());
        event.add(EntityType.PLAYER, FIRE_PROTECTION.get());
        event.add(EntityType.PLAYER, CRITICAL_DAMAGE.get());
        event.add(EntityType.PLAYER, MOB_DETECTION_RANGE.get());
        event.add(EntityType.PLAYER, JUMP_BOOST.get());
        event.add(EntityType.PLAYER, WIDTH.get(), 0.9375F);
        event.add(EntityType.PLAYER, HEIGHT.get(), 0.9375F);
    }


    private static RegistryObject<Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + VMinus.ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue).setSyncable(true));
    }
}
