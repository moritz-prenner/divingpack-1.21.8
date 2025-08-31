package com.example;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DivingGogglesCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("DivingGoggles")
                    .then(CommandManager.literal("toggleVisionRange")
                            .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                    .executes(ctx -> {
                                        int value = IntegerArgumentType.getInteger(ctx, "value");
                                        DivingPack.visionRange = value;
                                        ctx.getSource().sendFeedback(
                                                () -> Text.literal("Visibility Range for Diving Goggles set to: " + value),
                                                false
                                        );
                                        return 1;
                                    })
                            )
                    )
            );
        });
    }
}

