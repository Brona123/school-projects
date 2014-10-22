package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class GameArenaFragment extends Fragment {
	private GamePanel backgroundPanel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
							 ViewGroup container, 
							 Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.map1,
									container,
									false);
		backgroundPanel = (GamePanel) view.findViewById(R.id.backgroundView);
		return view;
	}
	
	public void startNextLevel(){
		backgroundPanel.startNextLevel();
	}
}
