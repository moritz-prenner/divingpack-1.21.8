package com.example;


import items.ModItems;
import net.fabricmc.api.ModInitializer;

public class DivingPack implements ModInitializer {
    public static final String MOD_ID = "divingpack";

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
    }
}