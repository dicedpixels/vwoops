package xyz.dicedpixels.vwoops.client;

import java.util.stream.Collectors;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.tag.client.v1.ClientTags;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import xyz.dicedpixels.vwoops.Vwoops;
import xyz.dicedpixels.vwoops.option.ConfigHandler;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var registeredBlocks =
                ClientTags.getOrCreateLocalTag(TagKey.of(RegistryKeys.BLOCK, new Identifier("enderman_holdable")))
                        .stream()
                        .map(Registries.BLOCK::get)
                        .collect(Collectors.toSet());
        var configured = ConfigHandler.load();
        Vwoops.init(configured, registeredBlocks);
    }
}
