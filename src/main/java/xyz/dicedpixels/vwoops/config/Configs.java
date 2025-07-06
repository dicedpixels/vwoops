package xyz.dicedpixels.vwoops.config;

import java.util.HashSet;
import java.util.Set;

public final class Configs {
    public final boolean firstRun;
    public final Set<String> holdableBlocks;

    private Configs(boolean firstRun, Set<String> holdableBlocks) {
        this.firstRun = firstRun;
        this.holdableBlocks = holdableBlocks;
    }

    public static Configs empty() {
        return new Configs(true, new HashSet<>());
    }

    public static Configs of(boolean firstRun, Set<String> holdableBlocks) {
        return new Configs(firstRun, holdableBlocks);
    }
}
