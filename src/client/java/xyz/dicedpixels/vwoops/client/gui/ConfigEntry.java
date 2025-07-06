package xyz.dicedpixels.vwoops.client.gui;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ElementListWidget.Entry;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import xyz.dicedpixels.vwoops.Blocks;

public class ConfigEntry extends Entry<ConfigEntry> {
    private final String blockName;
    private final List<ClickableWidget> children = new ObjectArrayList<>();
    private final DirectionalLayoutWidget layout = DirectionalLayoutWidget.horizontal().spacing(5);

    private ConfigEntry(MinecraftClient client, Block block) {
        blockName = block.getName().getString();

        var toggleButton = CyclingButtonWidget.onOffBuilder(Text.translatable("vwoops.gui.config.yes"), Text.translatable("vwoops.gui.config.no"))
            .initially(Blocks.getHoldableBlocks().contains(block))
            .build(0, 0, 90, 20, Text.translatable("vwoops.gui.config.holdable"), (button, value) -> Blocks.toggleBlock(block));
        var blockNameText = new TextWidget(300 - 10 - 90 - 22, 22, block.getName(), client.textRenderer).alignLeft();

        layout.add(new BlockItemWidget(block));
        layout.add(blockNameText);
        layout.add(toggleButton);
        layout.refreshPositions();
        layout.forEachChild(children::add);
    }

    public static ConfigEntry of(MinecraftClient client, Block block) {
        return new ConfigEntry(client, block);
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    public String getBlockName() {
        return blockName;
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        layout.setPosition(x - 1, y - 1);
        layout.forEachChild(widget -> widget.render(context, mouseX, mouseY, deltaTicks));
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return children;
    }
}
