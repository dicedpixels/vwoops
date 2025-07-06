package xyz.dicedpixels.vwoops.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import xyz.dicedpixels.vwoops.Blocks;
import xyz.dicedpixels.vwoops.Vwoops;

public class ConfigScreen extends Screen {
    private final Screen parentScreen;
    private ConfigListWidget list;
    private TextFieldWidget searchField;

    public ConfigScreen(Screen parentScreen) {
        super(Vwoops.TITLE);
        this.parentScreen = parentScreen;
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parentScreen);
        }
    }

    @Override
    protected void init() {
        searchField = new TextFieldWidget(textRenderer, 0, 0, 200, 20, searchField, Text.empty());
        searchField.setPlaceholder(Text.translatable("vwoops.gui.config.search").styled(style -> style.withColor(0x555555)));
        searchField.setChangedListener(text -> list.filterConfigEntries(text));

        list = addDrawableChild(new ConfigListWidget(client, width, height - 90, 10, 24));
        list.addConfigEntries(Blocks.getBlocksInTagSorted());

        var mainGrid = new GridWidget().setSpacing(5);
        var adder = mainGrid.createAdder(1);
        var buttonsLayout = DirectionalLayoutWidget.horizontal().spacing(5);
        var titleLayout = DirectionalLayoutWidget.horizontal().spacing(5);

        titleLayout.add(new TextWidget(title, textRenderer));
        titleLayout.add(new TextWidget(Vwoops.getVersion(), textRenderer));

        adder.add(titleLayout);
        adder.add(searchField);

        var resetButton = ButtonWidget.builder(Text.translatable("vwoops.gui.config.reset"), button -> resetConfig())
            .width(200 / 4)
            .tooltip(Tooltip.of(Text.translatable("vwoops.gui.config.reset_tooltip")))
            .build();

        buttonsLayout.add(resetButton);
        buttonsLayout.add(ButtonWidget.builder(Text.translatable("vwoops.gui.config.done"), button -> close()).width((200 / 4 * 3) - 5).build());

        adder.add(buttonsLayout);

        mainGrid.refreshPositions();
        mainGrid.setPosition(10, height - mainGrid.getHeight() - 10);
        mainGrid.forEachChild(this::addDrawableChild);
    }

    public void resetConfig() {
        searchField.setText("");

        if (Screen.hasShiftDown()) {
            Blocks.removeAllBlocks();
        } else {
            Blocks.addAllBlocks();
        }

        list.clearConfigEntries();
        list.addConfigEntries(Blocks.getBlocksInTagSorted());
    }
}
