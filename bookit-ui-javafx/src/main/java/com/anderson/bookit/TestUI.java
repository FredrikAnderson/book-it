package com.anderson.bookit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.anderson.bookit.ui.FilterComboBox;
import com.anderson.bookit.ui.Lookup;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestUI extends Application {

	private static TestUI myInstance = null;

	Stage window;
	private Scene rootScene = null;

	private BorderPane borderPane = new BorderPane();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
//						calendarView.setToday(LocalDate.now());
//						calendarView.setTime(LocalTime.now());
					});

					try {
						// update every 10 seconds
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			};
		};

		updateTimeThread.setPriority(Thread.MIN_PRIORITY);
		updateTimeThread.setDaemon(true);
		updateTimeThread.start();

		FilterComboBox<String> testFilterComp = new FilterComboBox<>();
		
        String[] countries = { "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina", "Armenia", "Austria", "Bahamas", "Bahrain", "Bangladesh",
                "Barbados", "Belarus", "Belgium", "Benin", "Bhutan", "Bolivia", "Bosnia & Herzegovina", "Botswana", "Brazil", "Bulgaria", "Burkina Faso",
                "Burma", "Burundi", "Cambodia", "Cameroon", "Canada", "China", "Colombia", "Comoros", "Congo", "Croatia", "Cuba", "Cyprus", "Czech Republic",
                "Denmark", "Georgia", "Germany", "Ghana", "Great Britain", "Greece", "Hungary", "Holland", "India", "Iran", "Iraq", "Italy", "Somalia",
                "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Uganda", "Ukraine", "United Arab Emirates",
                "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Yemen", "Zaire", "Zambia", "Zimbabwe" };

		testFilterComp.setLookup(new TestLookup(countries));
		testFilterComp.setOnAction(new ItemSelectedHandler());
//		testFilterComp.setLi
		testFilterComp.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			System.out.println("New value: " + newValue);
		} );
		
		borderPane.setCenter(testFilterComp);
		
		rootScene = new Scene(borderPane);
		
		primaryStage.setTitle("Book IT");
		primaryStage.setScene(rootScene);
		primaryStage.setWidth(300);
		primaryStage.setHeight(300);
		primaryStage.centerOnScreen();
		primaryStage.show();

	}

	class ItemSelectedHandler implements EventHandler<ActionEvent> {
		@Override
        public void handle(ActionEvent event) {
			System.out.println("Item Sel, Event: "+ event.toString());

			FilterComboBox<String> fbox = (FilterComboBox<String>) event.getSource();
			String selectedItem = fbox.getSelectionModel().getSelectedItem();

			System.out.println("Item Selected: "+ selectedItem);			
        }
	}

    class TestLookup implements Lookup {
        private String[] data;

        TestLookup(String[] data) {
            this.data = data.clone();
        }

        @Override
        public Collection<?> lookup(String prefix) {
            List<String> list = new ArrayList<String>();
            for (String s : data) {
                if (s.toLowerCase().startsWith(prefix)) {
                    list.add(s);
                }
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                System.err.println("Interrupted sleep" + e);
//            }
            return list;
        }

    }

}