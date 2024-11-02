package xyz.dicedpixels.vwoops;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

public class Vwoops {
    private static DefaultedRegistry<Block> blockRegistry;
    private static Set<Block> blocksInTag;
    private static Set<Block> holdableBlocks;
    private static Path worldRoot;

    public static boolean addAllBlocks() {
        var result = holdableBlocks.addAll(blocksInTag);
        save();
        return result;
    }

    public static boolean addBlock(Block block) {
        var result = holdableBlocks.add(block);
        save();
        return result;
    }

    public static int compareBlockNames(Block first, Block second) {
        return first.getName().getString().compareTo(second.getName().getString());
    }

    public static Set<Block> getBlocksInTag() {
        return blocksInTag;
    }

    public static Set<Block> getHoldableBlocks() {
        return holdableBlocks;
    }

    public static Set<Block> getNonHoldableBlocks() {
        return Sets.difference(blocksInTag, holdableBlocks);
    }

    public static void init(Path worldRoot, Registry<Block> blockRegistry) {
        Vwoops.blockRegistry = (DefaultedRegistry<Block>) blockRegistry;
        Vwoops.worldRoot = worldRoot;

        var config = Config.load(worldRoot);

        holdableBlocks = config.holdableBlocks.stream().map(id -> Registries.BLOCK.get(Identifier.of(id))).collect(Collectors.toSet());
        blocksInTag = blockRegistry.streamEntries().filter(reference -> reference.isIn(BlockTags.ENDERMAN_HOLDABLE)).map(Reference::value).collect(Collectors.toUnmodifiableSet());

        if (config.firstRun) {
            holdableBlocks = new HashSet<>(blocksInTag);
            save();
        }
    }

    public static boolean removeAllBlocks() {
        if (holdableBlocks.isEmpty()) {
            return false;
        }

        holdableBlocks.clear();
        save();
        return true;
    }

    public static boolean removeBlock(Block block) {
        var result = holdableBlocks.remove(block);
        save();
        return result;
    }

    public static void save() {
        Config.save(worldRoot, holdableBlocks.stream().map(block -> blockRegistry.getId(block).toString()).collect(Collectors.toSet()));
    }

    public static void toggleBlock(Block block) {
        if (holdableBlocks.contains(block)) {
            holdableBlocks.remove(block);
        } else {
            holdableBlocks.add(block);
        }

        save();
    }
}
