package xyz.dicedpixels.vwoops.option;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("vwoops.json");

    public static Set<Block> load() {
        if (Files.exists(PATH)) {
            try (BufferedReader reader = Files.newBufferedReader(PATH)) {
                return GSON.<Set<String>>fromJson(reader, Set.class).stream()
                        .map(id -> Registries.BLOCK.get(new Identifier(id)))
                        .collect(Collectors.toSet());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return Sets.newHashSet();
    }

    public static void save(Set<Block> blocks) {
        try (BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(
                    blocks.stream()
                            .map(block -> Registries.BLOCK.getId(block).toString())
                            .collect(Collectors.toSet()),
                    writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
