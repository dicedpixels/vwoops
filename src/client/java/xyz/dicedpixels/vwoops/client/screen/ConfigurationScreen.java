package xyz.dicedpixels.vwoops.client.screen;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import xyz.dicedpixels.pixel.client.screen.AbstractScreen;
import xyz.dicedpixels.vwoops.Vwoops;
import xyz.dicedpixels.vwoops.client.screen.widget.ListWidget;
import xyz.dicedpixels.vwoops.client.util.Texts;

public class ConfigurationScreen extends AbstractScreen {
    private final Screen parent;
    private TextFieldWidget search;

    public ConfigurationScreen(Screen parent) {
        super(Text.literal("Vwoops"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        var list = addDrawableChild(new ListWidget(client, width, height - 90, 10, 25));
        Vwoops.registeredBlocks().forEach(list::addEntry);
        search = new TextFieldWidget(textRenderer, 200, 20, Text.empty());
        search.setChangedListener(list::filter);
        var grid = new GridWidget().setSpacing(5);
        var adder = grid.createAdder(1);
        var buttons = DirectionalLayoutWidget.horizontal().spacing(5);
        adder.add(new TextWidget(title, textRenderer));
        adder.add(search);
        buttons.add(ButtonWidget.builder(Texts.RESET, button -> {
                    search.setText(StringUtils.EMPTY);
                    Vwoops.allowAllBlocks();
                    list.reset();
                    Vwoops.registeredBlocks().forEach(list::addEntry);
                })
                .width(200 / 4)
                .build());
        buttons.add(ButtonWidget.builder(Texts.DONE, button -> close())
                .width((200 / 4 * 3) - 5)
                .build());
        adder.add(buttons);
        grid.refreshPositions();
        grid.setPosition(10, height - grid.getHeight() - 10);
        grid.forEachChild(this::addDrawableChild);
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        var text = search.getText();
        super.resize(client, width, height);
        search.setText(text);
    }
}
