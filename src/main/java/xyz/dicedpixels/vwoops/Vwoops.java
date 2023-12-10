package xyz.dicedpixels.vwoops;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import xyz.dicedpixels.pixel.config.ConfigurationHandler;
import xyz.dicedpixels.vwoops.config.Configuration;

public final class Vwoops {
    private static final ConfigurationHandler HANDLER =
            ConfigurationHandler.builder().with("vwoops").build();
    private static final Configuration CONFIG = HANDLER.load(Configuration.class);
    private static ImmutableSortedSet<Block> registeredBlocks;
    private static boolean firstRun;
    private static Set<Block> allowedBlocks;

    public static void init() {
        allowedBlocks = CONFIG.allowedBlocks.stream()
                .map(id -> Registries.BLOCK.get(new Identifier(id)))
                .collect(Collectors.toSet());
        firstRun = CONFIG.firstRun;
        if (firstRun) {
            allowedBlocks.addAll(registeredBlocks);
            firstRun = false;
            save();
        }
    }

    public static ImmutableSortedSet<Block> registeredBlocks() {
        return registeredBlocks;
    }

    public static void initRegisteredBlocks(ImmutableSortedSet<Block> registeredBlocks) {
        if (Vwoops.registeredBlocks == null) {
            Vwoops.registeredBlocks = registeredBlocks;
        }
    }

    public static ImmutableSet<Block> allowedBlocks() {
        return ImmutableSet.copyOf(allowedBlocks);
    }

    public static boolean allowBlock(Block block) {
        var result = allowedBlocks.add(block);
        save();
        return result;
    }

    public static boolean denyBlock(Block block) {
        var result = allowedBlocks.remove(block);
        save();
        return result;
    }

    public static boolean allowAllBlocks() {
        var result = allowedBlocks.addAll(registeredBlocks);
        save();
        return result;
    }

    public static boolean denyAllBlocks() {
        if (!allowedBlocks.isEmpty()) {
            allowedBlocks.clear();
            save();
            return true;
        }
        return false;
    }

    public static void toggleBlock(Block block) {
        if (allowedBlocks.contains(block)) {
            allowedBlocks.remove(block);
        } else {
            allowedBlocks.add(block);
        }
        save();
    }

    public static void save() {
        CONFIG.firstRun = firstRun;
        CONFIG.allowedBlocks = allowedBlocks.stream()
                .map(block -> Registries.BLOCK.getId(block).toString())
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
        HANDLER.save(CONFIG);
    }
}
