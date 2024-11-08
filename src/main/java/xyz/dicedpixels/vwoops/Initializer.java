package xyz.dicedpixels.vwoops;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.WorldSavePath;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Vwoops.init(server.getSavePath(WorldSavePath.ROOT), server.getRegistryManager().getOrThrow(RegistryKeys.BLOCK));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment == CommandManager.RegistrationEnvironment.DEDICATED) {
                Commands.register(dispatcher, registryAccess);
            }
        });
    }
}
