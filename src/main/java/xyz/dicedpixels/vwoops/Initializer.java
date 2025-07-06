package xyz.dicedpixels.vwoops;

import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.util.WorldSavePath;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import xyz.dicedpixels.vwoops.command.Commands;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> Blocks.init(server.getSavePath(WorldSavePath.ROOT)));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment == RegistrationEnvironment.DEDICATED) {
                Commands.registerServerCommands(dispatcher, registryAccess);
            }

            if (environment == RegistrationEnvironment.INTEGRATED) {
                Commands.registerClientCommand(dispatcher);
            }
        });
        Vwoops.init();
    }
}
