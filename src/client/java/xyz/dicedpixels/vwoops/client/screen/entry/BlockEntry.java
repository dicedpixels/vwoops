package xyz.dicedpixels.vwoops.client.screen.entry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import xyz.dicedpixels.pixel.client.screen.entry.AbstractEntry;
import xyz.dicedpixels.vwoops.Vwoops;
import xyz.dicedpixels.vwoops.client.screen.widget.BlockItemWidget;
import xyz.dicedpixels.vwoops.client.util.Texts;

public class BlockEntry extends AbstractEntry {
    private final List<ClickableWidget> children = Lists.newArrayList();
    private final DirectionalLayoutWidget layout;
    private final Block block;

    private BlockEntry(Block block) {
        this.block = block;
        layout = DirectionalLayoutWidget.horizontal().spacing(5);
        var toggle = CyclingButtonWidget.onOffBuilder(Texts.DENY, Texts.ALLOW)
                .omitKeyText()
                .initially(Vwoops.allowedBlocks().contains(block))
                .build(0, 0, 40, 20, Text.empty(), (button, value) -> Vwoops.toggleBlock(block));
        layout.add(new BlockItemWidget(block));
        var name = new TextWidget(150, 18, block.getName(), MinecraftClient.getInstance().textRenderer).alignLeft();
        name.setTooltip(Tooltip.of(Text.of(Registries.BLOCK.getId(block))));
        var spacer = DirectionalLayoutWidget.vertical();
        spacer.add(EmptyWidget.ofHeight(2));
        spacer.add(name);
        spacer.refreshPositions();
        layout.add(spacer);
        layout.add(toggle);
        layout.refreshPositions();
        layout.forEachChild(children::add);
    }

    public static BlockEntry of(Block block) {
        return new BlockEntry(block);
    }

    public Block block() {
        return block;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return children;
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @Override
    public void render(
            DrawContext context,
            int index,
            int y,
            int x,
            int entryWidth,
            int entryHeight,
            int mouseX,
            int mouseY,
            boolean hovered,
            float delta) {
        layout.setPosition(x, y);
        layout.forEachChild(child -> child.render(context, mouseX, mouseY, delta));
    }
}
