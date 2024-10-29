package xyz.dicedpixels.vwoops;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Commands {
    private static int addAllBlocks(CommandContext<ServerCommandSource> context) {
        if (Vwoops.addAllBlocks()) {
            context.getSource().sendFeedback(() -> Translations.WILL_HOLD_ALL, true);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int addBlock(CommandContext<ServerCommandSource> context) {
        var block = BlockStateArgumentType.getBlockState(context, "block").getBlockState().getBlock();

        if (Vwoops.addBlock(block)) {
            context.getSource().sendFeedback(() -> Translations.WILL_HOLD.apply(block), true);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static RequiredArgumentBuilder<ServerCommandSource, BlockStateArgument> argument(CommandRegistryAccess registry) {
        return CommandManager.argument("block", BlockStateArgumentType.blockState(registry));
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry) {
        dispatcher.register(CommandManager.literal("vwoops")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("add")
                .then(Commands.argument(registry)
                    .suggests(Commands::suggestNonHoldableBlocks)
                    .executes(Commands::addBlock))
                .then(CommandManager.literal("*")
                    .executes(Commands::addAllBlocks)))
            .then(CommandManager.literal("remove")
                .then(Commands.argument(registry)
                    .suggests(Commands::suggestHoldableBlocks)
                    .executes(Commands::removeBlock))
                .then(CommandManager.literal("*")
                    .executes(Commands::removeAllBlocks))));
    }

    private static int removeAllBlocks(CommandContext<ServerCommandSource> context) {
        if (Vwoops.removeAllBlocks()) {
            context.getSource().sendFeedback(() -> Translations.WILL_NOT_HOLD_ALL, true);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int removeBlock(CommandContext<ServerCommandSource> context) {
        var block = BlockStateArgumentType.getBlockState(context, "block").getBlockState().getBlock();

        if (Vwoops.removeBlock(block)) {
            context.getSource().sendFeedback(() -> Translations.WILL_NOT_HOLD.apply(block), true);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> suggestHoldableBlocks(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestFromIdentifier(Vwoops.getHoldableBlocks().stream(), builder, Registries.BLOCK::getId, Block::getName);
    }

    private static CompletableFuture<Suggestions> suggestNonHoldableBlocks(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestFromIdentifier(Vwoops.getNonHoldableBlocks().stream(), builder, Registries.BLOCK::getId, Block::getName);
    }
}
