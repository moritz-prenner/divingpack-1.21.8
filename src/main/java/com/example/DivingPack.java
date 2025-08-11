package com.example;

import items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Items;

public class DivingPack implements ModInitializer {
    public static final String MOD_ID = "divingpack";

    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof PigEntity) {
                entity.dropItem(world, Items.CHAIN);
            }
        });
        ModItems.registerModItems();

    }
}