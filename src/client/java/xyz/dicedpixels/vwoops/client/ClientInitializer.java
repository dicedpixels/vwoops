package xyz.dicedpixels.vwoops.client;

import java.util.Comparator;

import com.google.common.collect.ImmutableSortedSet;

import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.tag.client.v1.ClientTags;

import xyz.dicedpixels.vwoops.Vwoops;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var registeredBlocks = ClientTags.getOrCreateLocalTag(BlockTags.ENDERMAN_HOLDABLE).stream()
                .map(Registries.BLOCK::get)
                .collect(ImmutableSortedSet.toImmutableSortedSet(
                        Comparator.comparing(block -> block.getName().getString())));
        Vwoops.initRegisteredBlocks(registeredBlocks);
        Vwoops.init();
    }
}
