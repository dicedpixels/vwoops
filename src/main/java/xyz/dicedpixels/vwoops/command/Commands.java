package xyz.dicedpixels.vwoops.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import xyz.dicedpixels.vwoops.Blocks;

public class Commands {
    private static Runnable configScreenInvoker = () -> {};

    private static RequiredArgumentBuilder<ServerCommandSource, BlockStateArgument> argument(CommandRegistryAccess registry) {
        return CommandManager.argument("block", BlockStateArgumentType.blockState(registry));
    }

    public static void registerClientCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("vwoops").executes(context -> {
            configScreenInvoker.run();
            return Command.SINGLE_SUCCESS;
        }));
    }

    public static void registerServerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry) {
        dispatcher.register(CommandManager.literal("vwoops")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("add")
                .then(Commands.argument(registry)
                    .suggests((context, builder) -> CommandSource.suggestFromIdentifier(Blocks.streamNonHoldableBlocks(), builder, Registries.BLOCK::getId, Block::getName))
                    .executes(context -> {
                        var block = BlockStateArgumentType.getBlockState(context, "block").getBlockState().getBlock();

                        if (Blocks.addBlock(block)) {
                            context.getSource().sendFeedback(CommandFeedback.withAppending("Endermen will hold", block.getName()), true);
                        }

                        return Command.SINGLE_SUCCESS;
                    }))
                .then(CommandManager.literal("*")
                    .executes(context -> {
                        if (Blocks.addAllBlocks()) {
                            context.getSource().sendFeedback(CommandFeedback.withAppending("Endermen will hold all blocks in", BlockTags.ENDERMAN_HOLDABLE.getName()), true);
                        }

                        return Command.SINGLE_SUCCESS;
                    })))
            .then(CommandManager.literal("remove")
                .then(Commands.argument(registry)
                    .suggests((context, builder) -> CommandSource.suggestFromIdentifier(Blocks.streamHoldableBlocks(), builder, Registries.BLOCK::getId, Block::getName))
                    .executes(context -> {
                        var block = BlockStateArgumentType.getBlockState(context, "block").getBlockState().getBlock();

                        if (Blocks.removeBlock(block)) {
                            context.getSource().sendFeedback(CommandFeedback.withAppending("Endermen will not hold", block.getName()), true);
                        }

                        return Command.SINGLE_SUCCESS;
                    }))
                .then(CommandManager.literal("*")
                    .executes(context -> {
                        if (Blocks.removeAllBlocks()) {
                            context.getSource().sendFeedback(CommandFeedback.withAppending("Endermen will not hold any blocks in", BlockTags.ENDERMAN_HOLDABLE.getName()), true);
                        }

                        return Command.SINGLE_SUCCESS;
                    }))));
    }

    public static void setConfigScreenInvoker(Runnable configScreenInvoker) {
        Commands.configScreenInvoker = configScreenInvoker;
    }
}
