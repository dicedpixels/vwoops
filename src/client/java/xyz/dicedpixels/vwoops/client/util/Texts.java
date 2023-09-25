package xyz.dicedpixels.vwoops.client.util;

import net.minecraft.text.Text;
import xyz.dicedpixels.vwoops.Vwoops;

public class Texts {
    public static final Text RESET = translatable("options.reset");

    @SuppressWarnings("SameParameterValue")
    private static Text translatable(String key) {
        return Text.translatable(String.format("%s.%s", Vwoops.MOD_ID, key));
    }
}
