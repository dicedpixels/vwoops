package io.github.dicedpixels.vwoops.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.dicedpixels.vwoops.Vwoops;
import io.github.dicedpixels.vwoops.VwoopsBlocks;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

import net.fabricmc.loader.api.FabricLoader;

public class VwoopsConfig {
	private static final ImmutableSet<String> ALL_BLOCKS = VwoopsBlocks.ALL.stream().map(b -> Registries.BLOCK.getId(b).toString()).collect(ImmutableSet.toImmutableSet());
	private static final HashSet<String> BLOCKS = new HashSet<>();
	private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("vwoops.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void load() {
		if (Files.exists(CONFIG_FILE)) {
			try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
				HashSet<String> config = GSON.fromJson(reader, TypeToken.getParameterized(HashSet.class, String.class).getType());
				restoreBlocks(config);
			} catch (IOException e) {
				Vwoops.LOGGER.warn("Vwoops encountered an error while reading the config file.");
				throw new RuntimeException(e);
			}
		} else {
			restoreBlocks();
			save();
			Vwoops.LOGGER.warn("Vwoops could not load the config file.");
		}
	}

	public static void restoreBlocks(HashSet<String> collection) {
		BLOCKS.clear();
		BLOCKS.addAll(collection);
	}

	public static void restoreBlocks() {
		BLOCKS.clear();
		BLOCKS.addAll(ALL_BLOCKS);
	}

	public static void save() {
		try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
			GSON.toJson(BLOCKS, writer);
		} catch (IOException e) {
			Vwoops.LOGGER.warn("Vwoops encountered an error while saving the config file.");
		}
	}

	public static boolean toggleBlockAllowed(Block block) {
		String blockId = VwoopsBlocks.blockIdOf(block);
		if (BLOCKS.contains(blockId)) {
			BLOCKS.remove(blockId);
		} else {
			BLOCKS.add(blockId);
		}
		return isBlockAllowed(block);
	}

	public static boolean isBlockAllowed(Block block) {
		return BLOCKS.contains(VwoopsBlocks.blockIdOf(block));
	}
}
