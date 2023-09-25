package xyz.dicedpixels.vwoops.client.gui;

import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.apache.commons.compress.utils.Lists;
import xyz.dicedpixels.vwoops.Vwoops;
import xyz.dicedpixels.vwoops.client.util.Texts;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private ListWidget list;
    private static final int BORDER_OFFSET = 2;

    public ConfigScreen(Screen parent) {
        super(Text.literal("Vwoops"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        list = new ListWidget(client, width, height, 25, this.height - 30, 25);
        addDrawableChild(list);
        footer();
        Vwoops.registered().stream()
                .sorted(Comparator.comparing(block -> block.getName().getString()))
                .forEach(block -> list.add(block));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(context);
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    private void footer() {
        var grid = new GridWidget().setColumnSpacing(5);
        var adder = grid.createAdder(2);
        adder.add(ButtonWidget.builder(Texts.RESET, button -> {
                    Vwoops.restore();
                    list.children().forEach(Entry::update);
                    Vwoops.saveConfig();
                })
                .width(ButtonWidget.DEFAULT_WIDTH_SMALL)
                .build());
        adder.add(ButtonWidget.builder(ScreenTexts.DONE, button -> {
                    Vwoops.saveConfig();
                    close();
                })
                .build());
        grid.forEachChild(this::addDrawableChild);
        grid.refreshPositions();
        grid.setPosition(width / 2 - grid.getWidth() / 2, height - grid.getHeight() - 5);
    }

    private static class ListWidget extends ElementListWidget<Entry> {
        private final EntryBuilder builder = new EntryBuilder(client, width);

        public ListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
            super(client, width, height, top, bottom, itemHeight);
        }

        public void add(Block block) {
            addEntry(builder.build(block));
        }
    }

    private record EntryBuilder(MinecraftClient client, int width) {
        public Entry build(Block block) {
            var layout = DirectionalLayoutWidget.horizontal().spacing(5);
            var text = new TextWidget(160, 20 + BORDER_OFFSET, block.getName(), client.textRenderer);
            var toggleButton = CyclingButtonWidget.onOffBuilder()
                    .omitKeyText()
                    .initially(Vwoops.contains(block))
                    .build(0, 0, 30, 20, Text.empty(), (button, value) -> Vwoops.toggle(block));
            text.alignLeft();
            layout.add(EmptyWidget.ofWidth(15));
            layout.add(text);
            layout.add(toggleButton);
            layout.refreshPositions();
            layout.setX(width / 2 - layout.getWidth() / 2);
            return new Entry(block, layout, toggleButton);
        }
    }

    static class Entry extends ElementListWidget.Entry<Entry> {
        private final Block block;
        private final LayoutWidget layout;
        private final List<ClickableWidget> children = Lists.newArrayList();
        private final String blockId;
        private final CyclingButtonWidget<Boolean> button;
        private static final MinecraftClient client = MinecraftClient.getInstance();

        Entry(Block block, LayoutWidget layout, CyclingButtonWidget<Boolean> button) {
            this.block = block;
            this.layout = layout;
            this.button = button;
            this.blockId = Registries.BLOCK.getId(block).toString();
            this.layout.forEachChild(this.children::add);
        }

        public void update() {
            button.setValue(Vwoops.contains(block));
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
            if (hovered && mouseX >= layout.getX() && mouseX <= layout.getX() + 12) {
                context.drawTooltip(client.textRenderer, Text.literal(blockId), mouseX, mouseY);
            }
            context.drawItem(new ItemStack(block.asItem()), x, y + BORDER_OFFSET);
            layout.forEachChild(child -> {
                child.setY(y);
                child.render(context, mouseX, mouseY, delta);
            });
        }
    }
}
