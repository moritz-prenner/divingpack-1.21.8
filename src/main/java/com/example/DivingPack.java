package com.example;

import items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class DivingPack implements ModInitializer {
    public static final String MOD_ID = "divingpack";

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity) entity).sendMessage(Text.literal("hiii"), true);
            }
        });
    }
}