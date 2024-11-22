package net.lixir.vminus;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.init.VminusModAttributes;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.network.capes.SetCapePacket;
import net.lixir.vminus.visions.ResourceVisionHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class VMinusEvents {
    private static final ConcurrentHashMap<String, UUID> UUID_CACHE = new ConcurrentHashMap<>();
    private static boolean isModLoaded(String modId) {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
    }

    @SubscribeEvent
    public static void onNonClientWorldLoad(LevelEvent.Load event) {
        LevelAccessor world = event.getLevel();
        if (world.isClientSide())
            return;
        ResourceVisionHelper.generateItemVisionsFile(world);
        ResourceVisionHelper.generateBlockVisionsFile(world);
        ResourceVisionHelper.generateEntityVisionsFile(world);
        ResourceVisionHelper.generateEffectVisionsFile(world);
        ResourceVisionHelper.generateEnchantmentVisionsFile(world);
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        // Loading all visions for optimization
        VisionHandler.clearCaches();
        VisionHandler.loadVisions();
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerLoggedInEvent event) {
        ServerPlayer newPlayer = (ServerPlayer) event.getEntity();
        for (ServerPlayer otherPlayer : newPlayer.server.getPlayerList().getPlayers()) {
            otherPlayer.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                VMinusMod.PACKET_HANDLER.sendTo(new SetCapePacket(capability.cape_id, otherPlayer.getUUID()), newPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {

            LevelAccessor world = event.getEntity().level();
            DamageSource damagesource = event.getSource();
            Entity entity = event.getEntity();
            Entity sourceentity = event.getSource().getEntity();
            Entity immediatesourceentity = event.getSource().getDirectEntity();
            double amount = event.getAmount();

            if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null)
                return;

            List<ResourceLocation> particles = new ArrayList<ResourceLocation>();
            ItemStack mainhandItem;
            double vX = 0;
            double vY = 0;
            double vZ = 0;
            double ranX = 0;
            double ranZ = 0;
            double ranY = 0;
            Entity directEntity = null;
            if (sourceentity != null && !(entity instanceof Player _plr && _plr.getAbilities().instabuild) && entity.isAlive() && sourceentity.isAlive() && entity instanceof LivingEntity) {
                mainhandItem = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
                directEntity = damagesource.getDirectEntity();
                System.out.println(mainhandItem);
                if (!mainhandItem.isEmpty() && mainhandItem.isEnchanted()) {
                    if (((isModLoaded("detour") && (sourceentity instanceof Player _plr ? _plr.getAttackStrengthScale(0) : 0) >= 0.75) || !isModLoaded("detour"))
                            || !(sourceentity instanceof Player) || directEntity == null
                            && !(directEntity == (damagesource.getEntity())) && entity.isAttackable()) {
                        // iterating through all of the items enchants
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(mainhandItem);
                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                            Enchantment enchantment = entry.getKey();
                            // getting the vision data from the enchant
                            JsonObject visionData = VisionHandler.getVisionData(enchantment);
                            if (visionData != null) {
                                if (visionData.has("particle")) {
                                    // getting the string and resource location to add to the particle list
                                    String particleString = VisionValueHelper.getFirstValidString(visionData, "particle");
                                    if (!particleString.isEmpty() && particleString != null) {
                                        ResourceLocation particleLocation = new ResourceLocation(particleString);
                                        if (particleLocation != null)
                                            particles.add(particleLocation);
                                    }
                                }
                                if (visionData.has("sound")) {
                                    String soundString = VisionValueHelper.getFirstValidString(visionData, "sound");
                                    if (!world.isClientSide())
                                        world.playSound(null, BlockPos.containing(sourceentity.getX(),
                                                sourceentity.getY() + 1,
                                                sourceentity.getZ()),
                                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundString))),
                                                SoundSource.PLAYERS,
                                                (float) 0.8,
                                                (float) 1);
                                }
                            }
                        }
                    }
                    // spawning random particles from the created list
                    if (particles.size() > 0) {
                        for (int index0 = 0; index0 < Mth.nextInt(RandomSource.create(), 5, 7); index0++) {
                            ResourceLocation chosenParticle = particles.get(Mth.nextInt(RandomSource.create(), 0, (particles.size() - 1)));
                            System.out.println(chosenParticle);
                            // getting random positions
                            ranX = entity.getX() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * (-1) - 0.3, entity.getBbWidth() / 2 + 0.3);
                            ranY = entity.getY() + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight() + 0.3);
                            ranZ = entity.getZ() + Mth.nextDouble(RandomSource.create(), (entity.getBbWidth() / 2) * (-1) - 0.3, entity.getBbWidth() / 2 + 0.3);
                            // getting random velocities
                            vX = Mth.nextDouble(RandomSource.create(), -0.08, 0.08);
                            vY = Mth.nextDouble(RandomSource.create(), -0.08, 0.08);
                            vZ = Mth.nextDouble(RandomSource.create(), -0.08, 0.08);
                            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(chosenParticle);
                            if (particleType instanceof SimpleParticleType simpleParticleType && simpleParticleType != null) {
                                //world.sendParticles(simpleParticleType, ranX, ranY, ranZ, 1, 0, 0, 0, Mth.nextDouble(RandomSource.create(), 0.01, 0.03));
                                world.addParticle(simpleParticleType, ranX, ranY, ranZ, vX, vY, vZ);
                            }
                        }
                    }


                }
            }
        }
    }

    // Setting attributes, removing nbt, and some other things.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack itemstack = event.getItemStack();
        String itemId = ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString();
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        CompoundTag tag = itemstack.getTag();
        EquipmentSlot eventSlot = (event != null) ? event.getSlotType() : null;
        Boolean miningFlag = false;
        if (itemData != null && (itemData.has("attributes") || itemData.has("tag") || itemData.has("remove_attributes"))) {
            if (itemData.has("remove_attributes")) {
                JsonArray removeAttributesArray = itemData.getAsJsonArray("remove_attributes");
                Set<String> attributesToRemove = new HashSet<>();
                for (JsonElement element : removeAttributesArray) {
                    JsonObject removeData = element.getAsJsonObject();
                    if (removeData.has("id")) {
                        attributesToRemove.add(removeData.get("id").getAsString());
                    }
                }
                if (event != null) {
                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                    for (Attribute attribute : originalModifiers.keySet()) {
                        for (AttributeModifier modifier : originalModifiers.get(attribute)) {
                            String modifierId = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();
                            if (attributesToRemove.contains(modifierId)) {
                                event.removeModifier(attribute, modifier);
                            }
                        }
                    }
                }
            }
            if (itemData.has("attributes")) {
                if (tag == null || !tag.contains("broken")) {
                    JsonArray attributesArray = itemData.getAsJsonArray("attributes");
                    for (JsonElement element : attributesArray) {
                        JsonObject attributeData = element.getAsJsonObject();
                        if (attributeData.has("id") && attributeData.has("value")) {
                            String attributeId = attributeData.get("id").getAsString();
                            String attributeName = "";
                            if (attributeData.has("name")) {
                                attributeName = attributeData.get("name").getAsString();
                            } else {
                                if (attributeId.indexOf(".") != -1) {
                                    attributeName = attributeId.substring(attributeId.indexOf(".") + 1);
                                } else if (attributeId.indexOf(":") != -1) {
                                    attributeName = attributeId.substring(attributeId.indexOf(":") + 1);
                                }
                                attributeName = attributeName.replaceAll("_", " ");
                            }

                            String operationString = attributeData.has("operation") ? attributeData.get("operation").getAsString().toLowerCase() : "addition";
                            double attributeValue = attributeData.get("value").getAsDouble();
                            boolean replace = attributeData.has("replace") && attributeData.get("replace").getAsBoolean();
                            if (replace) {
                                if (event != null) {
                                    Multimap<Attribute, AttributeModifier> originalModifiers = event.getOriginalModifiers();
                                    for (Attribute attribute : originalModifiers.keySet()) {
                                        for (AttributeModifier modifier : originalModifiers.get(attribute)) {
                                            String modifierId = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();
                                            if (modifierId.equals(attributeId)) {
                                                event.removeModifier(attribute, modifier);
                                            }
                                        }
                                    }
                                }
                            }

                            AttributeModifier.Operation operation;
                            switch (operationString) {
                                case "multiply_base":
                                    operation = AttributeModifier.Operation.MULTIPLY_BASE;
                                    break;
                                case "multiply_total":
                                    operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
                                    break;
                                default:
                                    operation = AttributeModifier.Operation.ADDITION;
                                    break;
                            }
                            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attributeId));
                            if (attribute != null) {
                                UUID attributeUUID;
                                String compositeId = itemId + "|" + attributeId;
                                // caching & getting uuids
                                if (attributeData.has("uuid")) {
                                    attributeUUID = UUID.fromString(attributeData.get("uuid").getAsString());
                                } else if (UUID_CACHE.get(compositeId) != null) {
                                    attributeUUID = UUID_CACHE.get(compositeId);
                                } else {
                                    UUID randomUUID = UUID.randomUUID();
                                    UUID_CACHE.put(compositeId, randomUUID);
                                    attributeUUID = randomUUID;
                                }
                                EquipmentSlot slot = EquipmentSlot.MAINHAND;
                                if (attributeData.has("slot")) {
                                    slot = EquipmentSlot.valueOf(attributeData.get("slot").getAsString().toUpperCase());
                                } else {
                                    // attempted fallback to get the equipment slot if not defined.
                                    if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/helmets")))) {
                                        slot = EquipmentSlot.HEAD;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/chestplates")))) {
                                        slot = EquipmentSlot.CHEST;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/leggings")))) {
                                        slot = EquipmentSlot.LEGS;
                                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("forge", "armors/boots")))) {
                                        slot = EquipmentSlot.FEET;
                                    }
                                }
                                if (eventSlot == slot) {
                                    AttributeModifier modifier = new AttributeModifier(attributeUUID, attributeName, attributeValue, operation);
                                    if (event != null) {
                                        if (attributeId.equals("vminus:mining_speed"))
                                            miningFlag = true;
                                        event.addModifier(attribute, modifier);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (itemData != null && itemData.has("nbt")) {
            ItemStack copyStack = VisionValueHelper.setNbts(itemData, itemstack.copy());
            //System.out.println(itemData);
            CompoundTag copyTag = copyStack.getOrCreateTag();
            if (copyTag != null && !copyTag.isEmpty()) {
                if (tag == null)
                    tag = itemstack.getOrCreateTag();
                for (String key : copyTag.getAllKeys()) {
                    tag.put(key, copyTag.get(key));
                }
                itemstack.setTag(tag);
            }
        }
        if (ModList.get().isLoaded("detour")) {
            if (itemstack.isEnchantable()) {
                if (tag == null)
                    tag = itemstack.getOrCreateTag();
                if (!tag.contains("enchantment_limit")) {
                    int limit = 2;
                    if (itemstack.is(ItemTags.create(new ResourceLocation("detour:one_enchantment_limit")))) {
                        limit = 1;
                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("detour:three_enchantment_limit")))) {
                        limit = 3;
                    } else if (itemstack.is(ItemTags.create(new ResourceLocation("detour:four_enchantment_limit")))) {
                        limit = 4;
                    }
                    tag.putDouble("enchantment_limit", limit);
                }
            }
        }
        if (eventSlot == EquipmentSlot.MAINHAND) {
            int efficiencyLevel = EnchantmentHelper.getEnchantments(itemstack).getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
            if (efficiencyLevel > 0) {
                double miningSpeedValue = efficiencyLevel * efficiencyLevel + 1;
                AttributeModifier miningSpeedModifier = new AttributeModifier(UUID.fromString("83e34d00-65ae-11ef-814d-325096b39f47"), "Effeciency Mining Speed", miningSpeedValue, AttributeModifier.Operation.ADDITION);
                if (event != null) {
                    event.addModifier(VminusModAttributes.MININGSPEED.get(), miningSpeedModifier);
                }
            }
            if (!miningFlag) {
                if (itemstack.getItem() instanceof TieredItem tieredItem) {
                    double tierMiningSpeed = tieredItem.getTier().getSpeed();
                    AttributeModifier tierMiningSpeedModifier = new AttributeModifier(UUID.fromString("e14d7c20-65ae-11ef-814d-325096b39f47"), "Tier Mining Speed", tierMiningSpeed, AttributeModifier.Operation.ADDITION);
                    if (event != null) {
                        event.addModifier(VminusModAttributes.MININGSPEED.get(), tierMiningSpeedModifier);
                    }
                }
            }
        }
    }

}
