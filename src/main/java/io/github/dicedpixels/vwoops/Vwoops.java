package io.github.dicedpixels.vwoops;

import io.github.dicedpixels.vwoops.config.VwoopsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;

public class Vwoops implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("vwoops");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Vwoops...");
		VwoopsConfig.load();
	}
}

