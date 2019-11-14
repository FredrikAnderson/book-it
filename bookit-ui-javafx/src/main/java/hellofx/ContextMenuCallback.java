package hellofx;



import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.ContextMenuParameter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

public class ContextMenuCallback<T1, T2> implements Callback<ContextMenuParameter, ContextMenu> {

	@Override
	public ContextMenu call(ContextMenuParameter param) {
		System.out.println("Menu: param: " + param.toString());

		ContextMenu menu = createContextMenu();
		
		return menu;
	}

	public static ContextMenu createContextMenu() {
		ContextMenu menu = new ContextMenu();
		
		MenuItem it1 = new MenuItem("New");
		it1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				BookITApp.newEntry();				
			}			
		});

		MenuItem it2 = new MenuItem("Edit");
		it2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Edit context; src: " + event.getSource());
				
				BookITApp.editCurrentEntry();
				
			}
		});

		MenuItem it3 = new MenuItem("Delete");
		it3.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Delete context; src: " + event.getSource());
				
				BookITApp.deleteCurrentEntry();
				
			}
		});

		menu.getItems().add(it1);
		menu.getItems().add(it2);
		menu.getItems().add(it3);
		return menu;
	}

}
