package hellofx.ui;



import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class FilterComboBox {

//	private ObservableList<String>    items;

	private static ComboBox<?> cbox;
		
	public static <T> void autoTypeSelect(ComboBox<T> comboBox) {	
		cbox = comboBox;
		
		cbox.setOnKeyReleased( new KeyHandler(cbox) );
	}
	
    private static class KeyHandler<T> implements EventHandler<KeyEvent> {

    	private static ComboBox<?> cbox;

        private SingleSelectionModel<T> sm;
        private String					typed;

        public KeyHandler(ComboBox<T> cbox) {
        	cbox = cbox;
            sm = cbox.getSelectionModel();
            typed = "";
        }

        @Override
        public void handle( KeyEvent event ) {
            // handle non alphanumeric keys like backspace, delete etc
        	typed.notify();
            if( event.getCode() == KeyCode.BACK_SPACE && typed.length()>0)
                typed = typed.substring( 0, typed.length() - 1 );
            else typed += event.getText();

            if( typed.length() == 0 ) {
                sm.selectFirst();
                return;
            }
            System.out.println("Typed: " + typed);
            
            synchronized (typed) {
                try {
					typed.wait(5 * 100000);
					
		            for( Object item: cbox.getItems() ) {
		            	if (item.toString().startsWith(typed)) {
//		            		cbox.setValue((T) item);
//		            		sm.select(item);
		            	}
		            }
					
				} catch (InterruptedException e) {
					e.printStackTrace();
					// Another key typed, fall-through
				}				
			}
            
            
//            for( String item: getItems() ) {
//                if( item.startsWith( typed ) ) sm.select( item );
//            }
        }

    }

}
