package hellofx;

import com.calendarfx.view.DateControl.EntryContextMenuParameter;

import javafx.scene.control.ContextMenu;
import javafx.util.Callback;

public class EntryContextMenuCallback<T1, T2> implements Callback<EntryContextMenuParameter, ContextMenu> {

	@Override
	public ContextMenu call(EntryContextMenuParameter param) {
		
		ContextMenu menu = ContextMenuCallback.createContextMenu();
		
		return menu;
	}

}
