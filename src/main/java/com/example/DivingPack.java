package com.example;

import items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.ArrayList;


public class DivingPack implements ModInitializer {
    public static final String MOD_ID = "divingpack";

    StatusEffectInstance UNLIMITED_WATER_BREATHING = new StatusEffectInstance(StatusEffects.WATER_BREATHING,200, 0, false, false, true);
    StatusEffectInstance UNLIMITED_SPEED = new StatusEffectInstance(StatusEffects.SPEED,200, 0, false, false, true);
    StatusEffectInstance UNLIMITED_GLOWING = new StatusEffectInstance(StatusEffects.GLOWING, Integer.MAX_VALUE, 0, false, false, true);

    ArrayList<LivingEntity> currentLivingEntitiesLoaded = new ArrayList<>();

    public static int visionRange = 30;

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        DivingGogglesCommand.register();

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity) {
                currentLivingEntitiesLoaded.add((LivingEntity) entity);

            }
        });

        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).removeStatusEffect(StatusEffects.GLOWING);
                currentLivingEntitiesLoaded.remove((LivingEntity) entity);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                boolean holdsDivingWatch = isHoldingItem(player);

                if (holdsDivingWatch) {
                    int playerBlockY = player.getBlockY();
                    player.sendMessage(Text.literal(String.valueOf("Your current height is: " + playerBlockY)).formatted(Formatting.BLUE), true);
                }

            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                boolean wearsGoggles = isWearingArmorPiece(player, EquipmentSlot.HEAD, ModItems.DIVING_GOGGLES);

                if (wearsGoggles) {
                    boolean playerUnderwater = player.isSubmergedInWater();

                    if (playerUnderwater) {
                        for (LivingEntity entity : currentLivingEntitiesLoaded) {
                            boolean entityUnderwater = entity.isSubmergedInWater();

                            if (player.distanceTo(entity) <= visionRange && entityUnderwater) {
                                    entity.addStatusEffect(UNLIMITED_GLOWING);
                            }
                            else {
                                entity.removeStatusEffect(StatusEffects.GLOWING);
                            }
                        }
                    }
                    else {
                        for (LivingEntity entity : currentLivingEntitiesLoaded) {
                            entity.removeStatusEffect(StatusEffects.GLOWING);
                        }
                    }
                }
                else {
                    for (LivingEntity entity : currentLivingEntitiesLoaded) {
                        entity.removeStatusEffect(StatusEffects.GLOWING);
                    }
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                boolean wearsTank = isWearingArmorPiece(player, EquipmentSlot.CHEST, ModItems.OXYGEN_TANK);

                if (wearsTank) {
                    player.addStatusEffect(UNLIMITED_WATER_BREATHING);
                }
                else {
                    player.removeStatusEffect(StatusEffects.WATER_BREATHING);
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                boolean wearsFins = isWearingArmorPiece(player, EquipmentSlot.FEET, ModItems.DIVING_FINS);

                if (wearsFins) {
                    boolean submergedUnderwater = player.isSubmergedInWater();

                    if (submergedUnderwater) {
                        player.addStatusEffect(UNLIMITED_SPEED);
                    } else {
                        player.removeStatusEffect(StatusEffects.SPEED);
                    }

                }


            }
        });


    }


    private boolean isHoldingItem(ServerPlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.DIVING_WATCH) || player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.DIVING_WATCH);
    }

    public boolean isWearingArmorPiece(ServerPlayerEntity player, EquipmentSlot slot, Item item) {
        ItemStack armorPiece = player.getEquippedStack(slot);
        return !armorPiece.isEmpty() && armorPiece.getItem() == item;
    }

}