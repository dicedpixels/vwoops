package xyz.dicedpixels.vwoops.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import xyz.dicedpixels.vwoops.Vwoops;

public final class VwoopsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry) {
        var rootNode = CommandManager.literal("vwoops");

        rootNode.requires(source -> source.hasPermissionLevel(2));
        rootNode.then(allowNode(registry));
        rootNode.then(denyNode(registry));

        dispatcher.register(rootNode);
    }

    private static LiteralArgumentBuilder<ServerCommandSource> allowNode(CommandRegistryAccess registry) {
        var rootNode = CommandManager.literal("allow");

        var blockStateArg = CommandManager.argument("block", BlockStateArgumentType.blockState(registry))
                .suggests((context, builder) -> CommandSource.suggestFromIdentifier(
                        Vwoops.registeredBlocks().stream()
                                .filter(block -> !Vwoops.allowedBlocks().contains(block)),
                        builder,
                        Registries.BLOCK::getId,
                        Block::getName))
                .executes(context -> {
                    var block = BlockStateArgumentType.getBlockState(context, "block")
                            .getBlockState()
                            .getBlock();

                    if (Vwoops.allowBlock(block)) {
                        context.getSource()
                                .sendFeedback(() -> Text.literal("Allowed: ").append(formattedBlockId(block)), false);
                    }

                    return Command.SINGLE_SUCCESS;
                });

        var allNode = CommandManager.literal("*").executes(context -> {
            if (Vwoops.allowAllBlocks()) {
                context.getSource().sendFeedback(() -> Text.literal("Allowed all blocks"), true);
            }

            return Command.SINGLE_SUCCESS;
        });

        rootNode.then(blockStateArg);
        rootNode.then(allNode);

        return rootNode;
    }

    private static LiteralArgumentBuilder<ServerCommandSource> denyNode(CommandRegistryAccess registry) {
        var rootNode = CommandManager.literal("deny");

        var blockStateArg = CommandManager.argument("block", BlockStateArgumentType.blockState(registry))
                .suggests((context, builder) -> CommandSource.suggestFromIdentifier(
                        Vwoops.allowedBlocks(), builder, Registries.BLOCK::getId, Block::getName))
                .executes(context -> {
                    var block = BlockStateArgumentType.getBlockState(context, "block")
                            .getBlockState()
                            .getBlock();

                    if (Vwoops.denyBlock(block)) {
                        context.getSource()
                                .sendFeedback(() -> Text.literal("Denied: ").append(formattedBlockId(block)), false);
                    }

                    return Command.SINGLE_SUCCESS;
                });

        var allNode = CommandManager.literal("*").executes(context -> {
            if (Vwoops.denyAllBlocks()) {
                context.getSource().sendFeedback(() -> Text.literal("Denied all blocks"), false);
            }

            return Command.SINGLE_SUCCESS;
        });

        rootNode.then(blockStateArg);
        rootNode.then(allNode);

        return rootNode;
    }

    private static Text formattedBlockId(Block block) {
        var id = Text.literal(Registries.BLOCK.getId(block).toString()).formatted(Formatting.LIGHT_PURPLE);
        var hoverEvent =
                new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(new ItemStack(block)));

        return Texts.bracketed(id.styled(style -> style.withHoverEvent(hoverEvent)));
    }
}
