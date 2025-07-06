package xyz.dicedpixels.vwoops.client;

import net.minecraft.client.MinecraftClient;

import net.fabricmc.api.ClientModInitializer;

import xyz.dicedpixels.vwoops.client.gui.ConfigScreen;
import xyz.dicedpixels.vwoops.command.Commands;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Commands.setConfigScreenInvoker(() -> {
            var client = MinecraftClient.getInstance();

            client.send(() -> client.setScreen(new ConfigScreen(client.currentScreen)));
        });
    }
}
