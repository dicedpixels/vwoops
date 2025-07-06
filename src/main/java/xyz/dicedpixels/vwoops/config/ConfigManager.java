package xyz.dicedpixels.vwoops.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import xyz.dicedpixels.vwoops.Vwoops;

public final class ConfigManager {
    private static final String FILE_NAME = "vwoops.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final TypeToken<Set<String>> SET_TYPE = new TypeToken<>() {};

    public static Configs load(Path worldRoot) {
        var path = worldRoot.normalize().resolve(FILE_NAME);

        if (Files.exists(path)) {
            try (var reader = Files.newBufferedReader(path)) {
                return Configs.of(false, GSON.fromJson(reader, SET_TYPE));
            } catch (Exception exception) {
                Vwoops.LOGGER.error("Failed to load configs at '{}'. Using default values.", path, exception);
            }
        }

        return Configs.empty();
    }

    public static void save(Path worldRoot, Set<String> holdableBlocks) {
        var path = worldRoot.normalize().resolve(FILE_NAME);

        try (var writer = Files.newBufferedWriter(path)) {
            GSON.toJson(holdableBlocks, writer);
        } catch (Exception exception) {
            Vwoops.LOGGER.error("Failed to save configs to '{}'.", path, exception);
        }
    }
}
