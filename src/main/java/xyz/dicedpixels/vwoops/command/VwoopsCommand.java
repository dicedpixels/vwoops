package xyz.dicedpixels.vwoops.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import xyz.dicedpixels.vwoops.Vwoops;

public class VwoopsCommand {
    private static final SuggestionProvider<ServerCommandSource> REGISTERED_BLOCKS =
            (context, builder) -> CommandSource.suggestFromIdentifier(
                    Registries.BLOCK.stream()
                            .filter(block ->
                                    TagUtil.isIn(BlockTags.ENDERMAN_HOLDABLE, block) && !Vwoops.contains(block)),
                    builder,
                    Registries.BLOCK::getId,
                    Block::getName);
    private static final SuggestionProvider<ServerCommandSource> CONFIGURED_BLOCKS = (context, builder) ->
            CommandSource.suggestFromIdentifier(Vwoops.configured(), builder, Registries.BLOCK::getId, Block::getName);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry) {
        dispatcher.register(CommandManager.literal("vwoops")
                .requires(source -> source.hasPermissionLevel(3))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("block", BlockStateArgumentType.blockState(registry))
                                .suggests(REGISTERED_BLOCKS)
                                .executes(context -> executeAdd(
                                        context.getSource(), BlockStateArgumentType.getBlockState(context, "block")))))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("block", BlockStateArgumentType.blockState(registry))
                                .suggests(CONFIGURED_BLOCKS)
                                .executes(context -> executeRemove(
                                        context.getSource(), BlockStateArgumentType.getBlockState(context, "block")))))
                .then(CommandManager.literal("restore").executes(context -> executeRestore(context.getSource()))));
    }

    private static int executeAdd(ServerCommandSource source, BlockStateArgument state) {
        var block = state.getBlockState().getBlock();
        if (Vwoops.add(block)) {
            Vwoops.saveConfig();
            source.sendFeedback(
                    () -> Text.literal("Vwoops: Added")
                            .append(ScreenTexts.SPACE)
                            .append(hoverable(block)),
                    false);
        }
        return 0;
    }

    private static int executeRemove(ServerCommandSource source, BlockStateArgument state) {
        var block = state.getBlockState().getBlock();
        if (Vwoops.remove(block)) {
            Vwoops.saveConfig();
            source.sendFeedback(
                    () -> Text.literal("Vwoops: Removed")
                            .append(ScreenTexts.SPACE)
                            .append(hoverable(block)),
                    false);
        }
        return 0;
    }

    private static int executeRestore(ServerCommandSource source) {
        if (!Vwoops.isEmpty()) {
            Vwoops.restore();
            Vwoops.saveConfig();
            source.sendFeedback(() -> Text.literal("Vwoops: Restored"), false);
        }
        return 0;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static MutableText hoverable(Block block) {
        return Texts.bracketed(
                Text.literal(Registries.BLOCK.getId(block).toString()).styled(style -> style.withColor(0xDE7AFA)
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(new ItemStack(block))))));
    }
}
