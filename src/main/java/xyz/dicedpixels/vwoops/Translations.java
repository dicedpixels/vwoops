package xyz.dicedpixels.vwoops;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Translations {
    private static final Text ENDERMAN_HOLDABLE = Text.literal("#enderman_holdable").formatted(Formatting.LIGHT_PURPLE);
    private static final Text INDICATOR = Text.literal("|").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD);
    public static final Text DONE = Text.translatable("vwoops.gui.done");
    public static final Text YES = Text.translatable("vwoops.gui.yes");
    public static final Text NO = Text.translatable("vwoops.gui.no");
    public static final Text RESET = Text.translatable("vwoops.gui.reset");
    public static final Function<Block, Text> WILL_HOLD = block -> withIndicator("vwoops.command.will_hold", "Endermen will hold %s", block);
    public static final Text WILL_HOLD_ALL = withIndicator("vwoops.command.will_hold_all", "Endermen will hold all blocks in %s");
    public static final Function<Block, Text> WILL_NOT_HOLD = block -> withIndicator("vwoops.command.will_not_hold", "Endermen will not hold %s", block);
    public static final Text WILL_NOT_HOLD_ALL = withIndicator("vwoops.command.will_not_hold_all", "Endermen will not hold any blocks in %s");

    private static Text withIndicator(String key, String fallback, Block block) {
        return Text.translatableWithFallback("vwoops.command.output", "%s %s", INDICATOR, Text.translatableWithFallback(key, fallback, block.getName().formatted(Formatting.LIGHT_PURPLE)));
    }

    private static Text withIndicator(String key, String fallback) {
        return Text.translatableWithFallback("vwoops.command.output", "%s %s", INDICATOR, Text.translatableWithFallback(key, fallback, ENDERMAN_HOLDABLE));
    }
}
