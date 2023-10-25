package xyz.dicedpixels.client.screen.widget;

import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import xyz.dicedpixels.Vwoops;

public final class ListWidget extends ElementListWidget<ListWidget.BlockEntry> {
    private static final Identifier BACKGROUND = new Identifier("textures/block/stone.png");

    public ListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        setRenderBackground(false);
    }

    public void addEntry(Block block) {
        addEntry(new BlockEntry(client.textRenderer, block));
    }

    public void filter(String text) {
        clearEntries();
        if (!text.isEmpty()) {
            Vwoops.registeredBlocks().stream()
                    .filter(block ->
                            StringUtils.containsIgnoreCase(block.getName().getString(), text))
                    .forEach(this::addEntry);
        } else {
            Vwoops.registeredBlocks().forEach(this::addEntry);
        }
        setScrollAmount(0);
    }

    public void refresh() {
        clearEntries();
        Vwoops.registeredBlocks().forEach(this::addEntry);
        setScrollAmount(0);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
        context.drawTexture(
                BACKGROUND,
                left,
                top,
                (float) right,
                (float) (bottom + getScrollAmount()),
                right - left,
                bottom - top,
                40,
                40);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        context.fillGradient(RenderLayer.getGuiOverlay(), left, top, right, top + 5, 0xFF000000, 0, 0);
        context.fillGradient(RenderLayer.getGuiOverlay(), left, bottom - 5, right, bottom, 0, 0xFF000000, 0);
        super.render(context, mouseX, mouseY, delta);
    }

    static final class BlockEntry extends ElementListWidget.Entry<BlockEntry> {
        private final TextRenderer textRenderer;
        private final Block block;
        private final List<ClickableWidget> children = Lists.newArrayList();
        private final String id;
        private final DirectionalLayoutWidget layout;

        public BlockEntry(TextRenderer textRenderer, Block block) {
            this.textRenderer = textRenderer;
            this.block = block;
            var toggle = CyclingButtonWidget.onOffBuilder(Text.literal("Deny"), Text.literal("Allow"))
                    .omitKeyText()
                    .initially(Vwoops.allowedBlocks().contains(block))
                    .build(0, 0, 40, 20, Text.empty(), (button, value) -> Vwoops.toggleBlock(block));
            layout = DirectionalLayoutWidget.horizontal().spacing(5);
            layout.add(new TextWidget(150, 20, block.getName(), textRenderer).alignLeft());
            layout.add(toggle);
            layout.refreshPositions();
            layout.forEachChild(children::add);
            id = Registries.BLOCK.getId(block).toString();
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
            if (hovered && mouseX >= x + 25 && mouseX <= x + textRenderer.getWidth(id) - 25) {
                context.drawTooltip(textRenderer, Text.literal(id), mouseX, mouseY);
            }
            context.drawItem(new ItemStack(block), x, y + 1);
            layout.setPosition(x + 25, y);
            layout.forEachChild(child -> child.render(context, mouseX, mouseY, delta));
        }
    }
}
