package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameArenaFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, 
							 ViewGroup container, 
							 Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.map1,
									container,
									false);
		
		return view;
	}
}
