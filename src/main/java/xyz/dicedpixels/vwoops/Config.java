package xyz.dicedpixels.vwoops;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Config {
    private static final String FILE_NAME = "vwoops.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final TypeToken<Set<String>> SET_TYPE = new TypeToken<>() {};

    public boolean firstRun = true;
    public Set<String> holdableBlocks = new HashSet<>();

    private Config() {}

    private Config(boolean firstRun, Set<String> holdableBlocks) {
        this.firstRun = firstRun;
        this.holdableBlocks = holdableBlocks;
    }

    public static Config load(Path worldRoot) {
        var configPath = worldRoot.normalize().resolve(FILE_NAME);

        if (Files.exists(configPath)) {
            try (var reader = Files.newBufferedReader(configPath)) {
                return new Config(false, GSON.fromJson(reader, SET_TYPE));
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        return new Config();
    }

    public static void save(Path worldRoot, Set<String> holdableBlocks) {
        try (var writer = Files.newBufferedWriter(worldRoot.normalize().resolve(FILE_NAME))) {
            GSON.toJson(holdableBlocks, writer);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
