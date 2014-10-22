/**
 * @author Sami Joutsijoki
 * The fragment that holds the game surface.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameArenaFragment extends Fragment {
	
	/**
	 * The gamepanel object for callback.
	 */
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
	
	/**
	 * Starts the next level by calling the 
	 * background panel's startNextLevel() -method.
	 */
	public void startNextLevel(){
		backgroundPanel.startNextLevel();
	}
}
