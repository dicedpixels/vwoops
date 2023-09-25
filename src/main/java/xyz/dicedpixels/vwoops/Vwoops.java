package xyz.dicedpixels.vwoops;

import java.util.Set;
import net.minecraft.block.Block;
import xyz.dicedpixels.vwoops.option.ConfigHandler;

public class Vwoops {
    public static final String MOD_ID = "vwoops";
    private static Set<Block> configured;
    private static Set<Block> registered;

    public static void init(Set<Block> configured, Set<Block> registered) {
        Vwoops.configured = configured;
        Vwoops.registered = registered;
    }

    public static boolean add(Block block) {
        return configured.add(block);
    }

    public static boolean remove(Block block) {
        return configured.remove(block);
    }

    public static boolean contains(Block block) {
        return configured.contains(block);
    }

    public static void toggle(Block block) {
        if (configured.contains(block)) {
            configured.remove(block);
        } else {
            configured.add(block);
        }
    }

    public static void restore() {
        configured.clear();
        configured.addAll(registered);
    }

    public static boolean isEmpty() {
        return configured.isEmpty();
    }

    public static Set<Block> configured() {
        return configured;
    }

    public static Set<Block> registered() {
        return registered;
    }

    public static void saveConfig() {
        ConfigHandler.save(configured);
    }
}
