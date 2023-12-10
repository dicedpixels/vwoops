package xyz.dicedpixels.vwoops.client.screen.widget;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class BlockItemWidget extends ClickableWidget {
    private final Block block;

    public BlockItemWidget(Block block) {
        super(0, 0, 16, 16, Text.empty());
        this.block = block;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawItem(new ItemStack(block), getX(), getY());
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

    @Override
    public void playDownSound(SoundManager soundManager) {}

    @Override
    public boolean isNarratable() {
        return false;
    }

    @Nullable @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return null;
    }
}
