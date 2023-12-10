package xyz.dicedpixels.vwoops;

import java.util.Comparator;

import com.google.common.collect.ImmutableSortedSet;

import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;

import xyz.dicedpixels.vwoops.command.VwoopsCommand;

public class ServerInitializer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            var registeredBlocks = Registries.BLOCK.stream()
                    .filter(block -> TagUtil.isIn(BlockTags.ENDERMAN_HOLDABLE, block))
                    .collect(ImmutableSortedSet.toImmutableSortedSet(
                            Comparator.comparing(block -> block.getName().getString())));
            Vwoops.initRegisteredBlocks(registeredBlocks);
            Vwoops.init();
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> Vwoops.save());
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registry, env) -> VwoopsCommand.register(dispatcher, registry));
    }
}
