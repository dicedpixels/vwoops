package io.github.dicedpixels.vwoops.config;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import io.github.dicedpixels.vwoops.VwoopsBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class VwoopsConfigScreen extends Screen {
	private final Screen parentScreen;
	private ListWidget listWidget;
	private ButtonWidget resetButton;

	public VwoopsConfigScreen(Screen parentScreen) {
		super(Text.translatable("vwoops.config.title"));
		this.parentScreen = parentScreen;
		VwoopsConfig.load();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		this.listWidget.render(context, mouseX, mouseY, delta);
		this.resetButton.active = !VwoopsBlocks.ALL.stream().allMatch(VwoopsConfig::isBlockAllowed);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void close() {
		VwoopsConfig.save();
		if (this.client != null) {
			this.client.setScreen(this.parentScreen);
		}
	}

	@Override
	protected void init() {
		this.listWidget = new ListWidget(this.client, this);
		this.addSelectableChild(this.listWidget);
		this.listWidget.addSection(Text.translatable("vwoops.config.category.flowers"), VwoopsBlocks.FLOWERS);
		this.listWidget.addSection(Text.translatable("vwoops.config.category.dirt"), VwoopsBlocks.DIRT);
		this.listWidget.addSection(Text.translatable("vwoops.config.category.other"), VwoopsBlocks.OTHER);
		this.resetButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("vwoops.config.rest"), b -> {
			VwoopsConfig.restoreBlocks();
			this.listWidget.children().forEach(AbstractEntry::update);
		}).dimensions(this.width / 2 - 154, this.height - 28, 150, 20).build());
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("vwoops.config.done"), b -> {
			VwoopsConfig.save();
			if (this.client != null) {
				this.client.setScreen(this.parentScreen);
			}
		}).dimensions(this.width / 2 + 4, this.height - 28, 150, 20).build());
	}

	private static class ListWidget extends ElementListWidget<AbstractEntry> {
		public ListWidget(MinecraftClient client, Screen screen) {
			super(client, screen.width, screen.height, 43, screen.height - 32, 24);
		}

		public void addSection(Text title, ImmutableCollection<Block> collection) {
			this.addEntry(new CategoryEntry(title.copy().formatted(Formatting.YELLOW, Formatting.BOLD)));
			collection.forEach(b -> this.addEntry(new BlockEntry(b)));
		}
	}

	private static class BlockEntry extends AbstractEntry {
		private final Block block;
		private final CyclingButtonWidget<Boolean> toggleButton;

		public BlockEntry(Block block) {
			this.block = block;
			this.toggleButton = CyclingButtonWidget.onOffBuilder(Text.translatable("vwoops.config.on"), Text.translatable("vwoops.config.off")).omitKeyText().initially(VwoopsConfig.isBlockAllowed(this.block)).build(0, 0, 43, 20, Text.empty(), (b, v) -> b.setValue(VwoopsConfig.toggleBlockAllowed(this.block)));
		}

		public void update() {
			this.toggleButton.setValue(VwoopsConfig.isBlockAllowed(this.block));
		}

		@Override
		public List<? extends Selectable> selectableChildren() {
			return ImmutableList.of(this.toggleButton);
		}

		@Override
		public List<? extends Element> children() {
			return ImmutableList.of(this.toggleButton);
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			context.drawItem(new ItemStack(this.block.asItem()), x + 1, y + 1);
			context.drawText(client.textRenderer, this.block.getName(), x + 24, y +  entryHeight / 3, 16777215, false);
			this.toggleButton.setX(x + entryWidth - toggleButton.getWidth() - 2);
			this.toggleButton.setY(y);
			this.toggleButton.render(context, mouseX, mouseY, tickDelta);
		}
	}

	abstract static class AbstractEntry extends ElementListWidget.Entry<AbstractEntry> {
		final static MinecraftClient client = MinecraftClient.getInstance();

		void update() {}
	}

	private static class CategoryEntry extends AbstractEntry {
		private final Text text;

		public CategoryEntry(Text categoryText) {
			this.text = categoryText;
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			context.drawCenteredTextWithShadow(client.textRenderer, this.text.copy().formatted(Formatting.BOLD, Formatting.YELLOW), x + entryWidth / 2, y + 8, 16777215);
		}

		@Override
		public List<? extends Selectable> selectableChildren() {
			return ImmutableList.of();
		}

		@Override
		public List<? extends Element> children() {
			return ImmutableList.of();
		}
	}
}
