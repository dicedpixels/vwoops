package xyz.dicedpixels.vwoops.client.screen.widget;

import java.util.LinkedHashSet;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import xyz.dicedpixels.pixel.client.screen.entry.AbstractEntry;
import xyz.dicedpixels.pixel.client.screen.widget.AbstractListWidget;
import xyz.dicedpixels.vwoops.client.screen.entry.BlockEntry;

public class ListWidget extends AbstractListWidget<AbstractEntry> {
    private final LinkedHashSet<AbstractEntry> entries = Sets.newLinkedHashSet();

    public ListWidget(MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        setRenderBackground(false);
    }

    public void addEntry(Block block) {
        var entry = BlockEntry.of(block);
        entries.add(entry);
        addEntry(entry);
    }

    public void filter(String text) {
        clearEntries();

        if (!text.isEmpty()) {
            entries.stream()
                    .filter(entry -> StringUtils.containsIgnoreCase(
                            ((BlockEntry) entry).block().getName().getString(), text))
                    .forEach(this::addEntry);
        } else {
            entries.forEach(this::addEntry);
        }

        setScrollAmount(0);
    }

    public void reset() {
        clearEntries();
        setScrollAmount(0);
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 80;
    }

    @Override
    protected boolean isSelectedEntry(int index) {
        var selected = getSelectedOrNull();
        return selected != null && selected == children().get(index);
    }

    @Override
    protected void drawSelectionHighlight(
            DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        var x1 = getX() + (width - entryWidth) / 2;
        var x2 = getX() + (width + entryWidth) / 2;

        context.fill(x1 + 1, y - 1, x2 - 1, y + entryHeight, 0xFF0A0A0A);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        setSelected(getHoveredEntry());
        super.renderWidget(context, mouseX, mouseY, delta);
    }
}
