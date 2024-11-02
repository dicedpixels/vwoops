package xyz.dicedpixels.vwoops;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.WorldSavePath;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Vwoops.init(server.getSavePath(WorldSavePath.ROOT), server.getRegistryManager().get(RegistryKeys.BLOCK));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Commands.register(dispatcher, registryAccess);
        });
    }
}
