package xyz.dicedpixels.client.screen;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import xyz.dicedpixels.Vwoops;
import xyz.dicedpixels.client.screen.widget.ListWidget;

public final class ConfigurationScreen extends Screen {
    private static final Identifier BACKGROUND = new Identifier("textures/block/stone.png");
    private final Screen parent;
    private ListWidget list;

    public ConfigurationScreen(Screen parent) {
        super(Text.literal("Vwoops"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        list = new ListWidget(client, width, height, 10, height - 80, 25);
        var search = new TextFieldWidget(textRenderer, 200, 20, Text.empty());
        search.setPlaceholder(Text.literal("Search..."));
        search.setChangedListener(string -> list.filter(string));
        addDrawableChild(list);
        Vwoops.registeredBlocks().forEach(list::addEntry);
        var grid = new GridWidget().setSpacing(5);
        var adder = grid.createAdder(1);
        var buttons = DirectionalLayoutWidget.horizontal().spacing(5);
        adder.add(new TextWidget(Text.literal("Vwoops"), textRenderer));
        adder.add(search);
        buttons.add(ButtonWidget.builder(Text.literal("Reset"), button -> {
                    search.setText(StringUtils.EMPTY);
                    Vwoops.allowAllBlocks();
                    list.refresh();
                })
                .width(200 / 4)
                .build());
        buttons.add(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .width((200 / 4 * 3) - 5)
                .build());
        adder.add(buttons);
        grid.refreshPositions();
        grid.setPosition(10, height - grid.getHeight() - 10);
        grid.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
        context.drawTexture(BACKGROUND, 0, 0, 0, 0.0F, 0.0F, width, height, 40, 40);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
