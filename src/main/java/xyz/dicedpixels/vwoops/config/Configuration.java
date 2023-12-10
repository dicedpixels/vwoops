package xyz.dicedpixels.vwoops.config;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.annotations.SerializedName;

public class Configuration {
    @SerializedName("first-run")
    public boolean firstRun = true;

    @SerializedName("allowed-blocks")
    public Set<String> allowedBlocks = Sets.newHashSet();
}
