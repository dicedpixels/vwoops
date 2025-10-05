package xyz.dicedpixels.vwoops.client.gui;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipState;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public class BlockItemWidget extends ClickableWidget {
    private final Block block;
    private final TooltipState tooltipState = new TooltipState();

    public BlockItemWidget(Block block) {
        super(0, 0, 20, 20, Text.empty());
        this.block = block;
        tooltipState.setTooltip(Tooltip.of(Text.literal(Registries.BLOCK.getId(block).toString())));
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

    @Override
    public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return null;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {}

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawItem(new ItemStack(block), getX() + 2, getY() + 2);
        tooltipState.render(context, mouseX, mouseY, isHovered(), isFocused(), getNavigationFocus());
    }
}
