package xyz.dicedpixels.vwoops.client;

import com.mojang.brigadier.Command;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import xyz.dicedpixels.vwoops.client.gui.ConfigScreen;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("vwoops").executes(context -> {
                var client = context.getSource().getClient();
                client.send(() -> client.setScreen(new ConfigScreen(client.currentScreen)));
                return Command.SINGLE_SUCCESS;
            }));
        });
    }
}
