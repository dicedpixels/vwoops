package xyz.dicedpixels.vwoops.client.gui;

import java.util.Collection;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;

public class ConfigListWidget extends ElementListWidget<ConfigEntry> {
    private final Set<ConfigEntry> entries = new ObjectLinkedOpenHashSet<>();

    public ConfigListWidget(MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(client, width + 1, height, y, itemHeight);
    }

    public void addConfigEntries(Collection<Block> blocks) {
        blocks.forEach(block -> {
            var entry = ConfigEntry.of(client, block);

            addEntry(entry);
            entries.add(entry);
        });
    }

    public void clearConfigEntries() {
        clearEntries();
        entries.clear();
    }

    @Override
    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        var x1 = getX() + (width - entryWidth) / 2;
        var x2 = getX() + (width + entryWidth) / 2;

        context.fill(x1, y - 2, x2, y + entryHeight, 0x40FFFFFF);
    }

    public void filterConfigEntries(String text) {
        clearEntries();

        if (text.isEmpty()) {
            entries.forEach(this::addEntry);
        } else {
            for (ConfigEntry entry : entries) {
                if (StringUtils.containsIgnoreCase(entry.getBlockName(), text)) {
                    addEntry(entry);
                }
            }
        }

        setScrollY(0);
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
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        setSelected(getHoveredEntry());
        super.renderWidget(context, mouseX, mouseY, delta);
    }
}
