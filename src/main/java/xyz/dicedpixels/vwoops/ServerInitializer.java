package xyz.dicedpixels.vwoops;

import com.google.common.collect.Sets;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import xyz.dicedpixels.vwoops.command.VwoopsCommand;
import xyz.dicedpixels.vwoops.option.ConfigHandler;

public class ServerInitializer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registry, env) -> VwoopsCommand.register(dispatcher, registry));
        var configured = ConfigHandler.load();
        Vwoops.init(configured, Sets.newHashSet());
    }
}
