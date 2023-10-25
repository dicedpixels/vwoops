package xyz.dicedpixels.configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import net.fabricmc.loader.api.FabricLoader;

public final class Configuration {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("vwoops.json");
    private static Configuration configuration;

    @SerializedName("first-run")
    private boolean firstRun = true;

    @SerializedName("allowed-blocks")
    private Set<String> allowedBlocks = Sets.newHashSet();

    private Configuration() {}

    public static Configuration instance() {
        return configuration = configuration == null ? load() : configuration;
    }

    private static Configuration load() {
        if (Files.exists(PATH)) {
            try (var reader = Files.newBufferedReader(PATH)) {
                return GSON.fromJson(reader, Configuration.class);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return new Configuration();
    }

    public void save() {
        try (var writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(configuration, writer);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean firstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public Set<String> allowedBlocks() {
        return allowedBlocks;
    }

    public void setAllowedBlocks(Set<String> allowedBlocks) {
        this.allowedBlocks = allowedBlocks;
    }
}
