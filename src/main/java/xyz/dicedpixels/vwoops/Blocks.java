package xyz.dicedpixels.vwoops;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import xyz.dicedpixels.vwoops.config.ConfigManager;

public class Blocks {
    private static final Set<Block> holdableBlocks = new HashSet<>();
    private static Set<Block> blocksInTag = new HashSet<>();
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

    public static Collection<Block> getBlocksInTagSorted() {
        var list = new ArrayList<>(blocksInTag);

        list.sort(Blocks::compareBlockNames);
        return list;
    }

    public static Set<Block> getHoldableBlocks() {
        return holdableBlocks;
    }

    public static Set<Block> getNonHoldableBlocks() {
        return Sets.difference(blocksInTag, holdableBlocks);
    }

    public static void init(Path worldRoot) {
        Blocks.worldRoot = worldRoot;
        holdableBlocks.clear();
        blocksInTag.clear();

        var configs = ConfigManager.load(worldRoot);

        for (var id : configs.holdableBlocks) {
            holdableBlocks.add(Registries.BLOCK.get(Identifier.of(id)));
        }

        for (var entry : Registries.BLOCK.getIndexedEntries()) {
            if (entry.isIn(BlockTags.ENDERMAN_HOLDABLE)) {
                blocksInTag.add(entry.value());
            }
        }

        blocksInTag = Set.copyOf(blocksInTag);

        if (configs.firstRun) {
            holdableBlocks.clear();
            holdableBlocks.addAll(blocksInTag);
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
        var blockIds = new HashSet<String>();

        for (Block block : holdableBlocks) {
            blockIds.add(Registries.BLOCK.getId(block).toString());
        }

        ConfigManager.save(worldRoot, blockIds);
    }

    public static Stream<Block> streamHoldableBlocks() {
        return holdableBlocks.stream();
    }

    public static Stream<Block> streamNonHoldableBlocks() {
        return getNonHoldableBlocks().stream();
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
