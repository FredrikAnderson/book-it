package hellofx;

import com.calendarfx.view.DateControl.EntryDetailsParameter;

import javafx.util.Callback;

public class EntryDetailsManager<T1, T2> implements Callback<EntryDetailsParameter, Boolean> {

	@Override
	public Boolean call(EntryDetailsParameter param) {

		BookITApp.setCurrentEntry(param.getEntry());
		
		return true;
	}

}
