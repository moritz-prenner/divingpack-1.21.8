package com.example;

import items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.item.equipment.ArmorMaterial.*;

public class DivingPack implements ModInitializer {
    public static final String MOD_ID = "divingpack";

    @Override
    public void onInitialize() {
        ModItems.registerModItems();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                boolean holdsItem = isHoldingItem(player);
                if (holdsItem) {
                    int playerBlockY = player.getBlockY();
                    player.sendMessage(Text.literal(String.valueOf("Your current height is: " + playerBlockY)).formatted(Formatting.BLUE), true);
                }
            }
        });
    }

    private boolean isHoldingItem(ServerPlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.DIVING_WATCH) || player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.DIVING_WATCH);
    }
}