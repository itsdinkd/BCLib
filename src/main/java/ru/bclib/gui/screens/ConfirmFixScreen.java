package ru.bclib.gui.screens;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.Nullable;
import ru.bclib.gui.gridlayout.GridCheckboxCell;
import ru.bclib.gui.gridlayout.GridLayout.Alignment;
import ru.bclib.gui.gridlayout.GridRow;

@Environment(EnvType.CLIENT)
public class ConfirmFixScreen extends BCLibScreen {
	protected final ConfirmFixScreen.Listener listener;
	private final Component description;
	protected int id;
	
	public ConfirmFixScreen(@Nullable Screen parent, ConfirmFixScreen.Listener listener) {
		super(parent, new TranslatableComponent("bclib.datafixer.backupWarning.title"));
		this.listener = listener;
		
		this.description = new TranslatableComponent("bclib.datafixer.backupWarning.message");
	}
	
	protected void initLayout() {
		final int BUTTON_HEIGHT = 20;
		
		grid.addRow().addMessage(this.description, this.font, Alignment.CENTER);
		grid.addSpacerRow();
		
		GridRow row = grid.addRow();
		GridCheckboxCell backup = row.addCheckbox(new TranslatableComponent("bclib.datafixer.backupWarning.backup"), true, BUTTON_HEIGHT, this.font);
		
		grid.addSpacerRow(10);
		
		row = grid.addRow();
		GridCheckboxCell fix = row.addCheckbox(new TranslatableComponent("bclib.datafixer.backupWarning.fix"), true, BUTTON_HEIGHT, this.font);
		
		grid.addSpacerRow(20);
		
		row = grid.addRow();
		row.addFiller();
		row.addButton(CommonComponents.GUI_CANCEL, BUTTON_HEIGHT, this.font, (button) -> {
			onClose();
		});
		row.addSpacer();
		row.addButton(CommonComponents.GUI_PROCEED, BUTTON_HEIGHT, this.font, (button) -> {
			this.listener.proceed(backup.isChecked(), fix.isChecked());
		});
		row.addFiller();
	}
	
	public boolean shouldCloseOnEsc() {
		return true;
	}
	
	@Environment(EnvType.CLIENT)
	public interface Listener {
		void proceed(boolean createBackup, boolean applyPatches);
	}
}
