package xyz.dicedpixels.vwoops.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import xyz.dicedpixels.vwoops.Translations;
import xyz.dicedpixels.vwoops.Vwoops;

public class ConfigScreen extends Screen {
    private final Screen parentScreen;
    private ConfigListWidget list;
    private TextFieldWidget searchField;

    public ConfigScreen(Screen parentScreen) {
        super(Text.literal("Vwoops"));
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
        searchField.setPlaceholder(Translations.SEARCH.formatted(Formatting.DARK_GRAY));
        searchField.setChangedListener(text -> list.filterConfigEntries(text));

        list = addDrawableChild(new ConfigListWidget(client, width, height - 90, 10, 24));
        list.addConfigEntries(Vwoops.getBlocksInTag().stream().sorted(Vwoops::compareBlockNames));

        var mainGrid = new GridWidget().setSpacing(5);
        var adder = mainGrid.createAdder(1);
        var buttonsLayout = DirectionalLayoutWidget.horizontal().spacing(5);

        adder.add(new TextWidget(title, textRenderer));
        adder.add(searchField);

        buttonsLayout.add(ButtonWidget.builder(Translations.RESET, button -> resetConfig()).width(200 / 4).build());
        buttonsLayout.add(ButtonWidget.builder(Translations.DONE, button -> close()).width((200 / 4 * 3) - 5).build());

        adder.add(buttonsLayout);

        mainGrid.refreshPositions();
        mainGrid.setPosition(10, height - mainGrid.getHeight() - 10);
        mainGrid.forEachChild(this::addDrawableChild);
    }

    public void resetConfig() {
        searchField.setText("");
        Vwoops.addAllBlocks();
        list.clearConfigEntries();
        list.addConfigEntries(Vwoops.getBlocksInTag().stream().sorted(Vwoops::compareBlockNames));
    }
}
