package xyz.dicedpixels.vwoops.command;

import java.util.function.Supplier;

import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

public final class CommandFeedback {
    public static Supplier<Text> withAppending(String text, Text appending) {
        return () -> Text.empty()
            .append(Text.literal("|").styled(style -> style.withFormatting(Formatting.DARK_AQUA, Formatting.BOLD)))
            .append(ScreenTexts.SPACE)
            .append(Text.literal(text))
            .append(ScreenTexts.SPACE)
            .append(Texts.bracketed(appending.copy().styled(style -> style.withFormatting(Formatting.DARK_AQUA))));
    }
}
