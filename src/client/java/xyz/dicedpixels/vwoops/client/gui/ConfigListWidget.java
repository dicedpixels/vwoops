package xyz.dicedpixels.vwoops.client.gui;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipState;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import xyz.dicedpixels.vwoops.Translations;
import xyz.dicedpixels.vwoops.Vwoops;
import xyz.dicedpixels.vwoops.client.gui.ConfigListWidget.ConfigEntry;

public class ConfigListWidget extends ElementListWidget<ConfigEntry> {
    private final Set<ConfigEntry> entries = new ObjectLinkedOpenHashSet<>();

    public ConfigListWidget(MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight - 2);
    }

    public void addConfigEntries(Stream<Block> blocks) {
        blocks.forEach(block -> {
            var entry = new ConfigEntry(client, block);
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
        context.fill(x1, y - 2, x2, y + entryHeight + 2, 0x40ffffff);
    }

    public void filterConfigEntries(String text) {
        clearEntries();

        if (text.isEmpty()) {
            entries.forEach(this::addEntry);
        } else {
            entries.stream().filter(entry -> StringUtils.containsIgnoreCase(entry.getBlockName(), text)).forEach(this::addEntry);
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

    public static class ConfigEntry extends Entry<ConfigEntry> {
        private final String blockName;
        private final List<ClickableWidget> children = new ObjectArrayList<>();
        private final DirectionalLayoutWidget layout = DirectionalLayoutWidget.horizontal().spacing(5);

        public ConfigEntry(MinecraftClient client, Block block) {
            this.blockName = block.getName().getString();

            var toggleButton = CyclingButtonWidget.onOffBuilder(Translations.YES, Translations.NO).initially(Vwoops.getHoldableBlocks().contains(block)).build(0, 0, 90, 20, Translations.HOLDABALE, (button, value) -> Vwoops.toggleBlock(block));
            var blockNameText = new TextWidget(300 - 10 - 90 - 22, 22, block.getName(), client.textRenderer).alignLeft();

            layout.add(new BlockItemWidget(block));
            layout.add(blockNameText);
            layout.add(toggleButton);
            layout.refreshPositions();
            layout.forEachChild(children::add);
        }

        @Override
        public List<? extends Element> children() {
            return children;
        }

        public String getBlockName() {
            return blockName;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            layout.setPosition(x - 1, y - 1);
            layout.forEachChild(widget -> widget.render(context, mouseX, mouseY, tickDelta));
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return children;
        }

        public static class BlockItemWidget extends ClickableWidget {
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
            public boolean isNarratable() {
                return false;
            }

            @Override
            public void playDownSound(SoundManager soundManager) {}

            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                context.drawItem(new ItemStack(block), getX() + 2, getY() + 2);
                tooltipState.render(isHovered(), isFocused(), getNavigationFocus());
            }
        }
    }
}
