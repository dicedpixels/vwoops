package io.github.dicedpixels.vwoops;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;

public class VwoopsBlocks {
	public static final ImmutableSet<Block> FLOWERS = ImmutableSet.of(
			Blocks.DANDELION,          // minecraft:dandelion
			Blocks.POPPY,              // minecraft:poppy
			Blocks.BLUE_ORCHID,        // minecraft:blue_orchid
			Blocks.ALLIUM,             // minecraft:allium
			Blocks.AZURE_BLUET,        // minecraft:azure_bluet
			Blocks.RED_TULIP,          // minecraft:red_tulip
			Blocks.ORANGE_TULIP,       // minecraft:orange_tulip
			Blocks.WHITE_TULIP,        // minecraft:white_tulip
			Blocks.PINK_TULIP,         // minecraft:pink_tulip
			Blocks.OXEYE_DAISY,        // minecraft:oxeye_daisy
			Blocks.CORNFLOWER,         // minecraft:cornflower
			Blocks.LILY_OF_THE_VALLEY, // minecraft:lily_of_the_valley
			Blocks.WITHER_ROSE         // minecraft:wither_rose);
	);

	public static final ImmutableSet<Block> DIRT = ImmutableSet.of(
			Blocks.DIRT,                // minecraft:dirt,
			Blocks.GRASS_BLOCK,         // minecraft:grass_block,
			Blocks.PODZOL,              // minecraft:podzol,
			Blocks.COARSE_DIRT,         // minecraft:coarse_dirt,
			Blocks.MYCELIUM,            // minecraft:mycelium,
			Blocks.ROOTED_DIRT,         // minecraft:rooted_dirt,
			Blocks.MOSS_BLOCK,          // minecraft:moss_block,
			Blocks.MUD,                 // minecraft:mud,
			Blocks.MUDDY_MANGROVE_ROOTS // minecraft:muddy_mangrove_roots
	);

	public static final ImmutableSet<Block> OTHER = ImmutableSet.of(
			Blocks.GRAVEL,         // minecraft:gravel
			Blocks.BROWN_MUSHROOM, // minecraft:brown_mushroom
			Blocks.RED_MUSHROOM,   // minecraft:red_mushroom
			Blocks.TNT,            // minecraft:tnt
			Blocks.CACTUS,         // minecraft:cactus
			Blocks.CLAY,           // minecraft:clay
			Blocks.PUMPKIN,        // minecraft:pumpkin
			Blocks.CARVED_PUMPKIN, // minecraft:carved_pumpkin
			Blocks.MELON,          // minecraft:melon
			Blocks.CRIMSON_FUNGUS, // minecraft:crimson_fungus
			Blocks.CRIMSON_NYLIUM, // minecraft:crimson_nylium
			Blocks.CRIMSON_ROOTS,  // minecraft:crimson_roots
			Blocks.WARPED_FUNGUS,  // minecraft:warped_fungus
			Blocks.WARPED_NYLIUM,  // minecraft:warped_nylium
			Blocks.WARPED_ROOTS,   // minecraft:warped_roots
			Blocks.SAND,           // minecraft:sand
			Blocks.RED_SAND        // minecraft:red_sand
	);

	public static final ImmutableSet<Block> ALL = ImmutableSet.<Block>builder().addAll(FLOWERS).addAll(DIRT).addAll(OTHER).build();

	public static String blockIdOf(Block block) {
		return Registries.BLOCK.getId(block).toString();
	}
}
