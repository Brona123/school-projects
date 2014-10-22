/**
 * @author Sami Joutsijoki
 * The fragment that gets displayed whenever a stage is completed.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VictoryFragment extends Fragment implements OnClickListener{
	
	/**
	 * The button that starts the next level.
	 */
	private Button continueButton;
	
	/**
	 * The button that goes back to main menu.
	 */
	private Button mainMenuButton;
	
	/**
	 * The callback object for communicating between fragments.
	 */
	private GameActivity callback;
	
	/**
	 * The text view that displays the amount of bugs smashed.
	 */
	private TextView bugsSmashedView;
	
	/**
	 * The text view that displays the accuracy.
	 */
	private TextView accuracyView;
	
	/**
	 * The text view that displays the max streak.
	 */
	private TextView streakView;
	
	/**
	 * The text view that displays the total score.
	 */
	private TextView scoreView;
	
	/**
	 * The progress bar that visually displays the amount of bugs smashed.
	 */
	private ProgressBar bugsSmashedBar;
	
	/**
	 * The progress bar that visually displays the accuracy.
	 */
	private ProgressBar accuracyBar;
	
	/**
	 * The view that holds ads.
	 */
	private AdView adView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
							 ViewGroup container, 
							 Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.win,
									container,
									false);
		continueButton = (Button)view.findViewById(R.id.victoryContinueButton);
		mainMenuButton = (Button)view.findViewById(R.id.victoryMainMenuButton);
		bugsSmashedView = (TextView)view.findViewById(R.id.victoryBugsSmashedView);
		accuracyView = (TextView)view.findViewById(R.id.victoryAccuracyView);
		streakView = (TextView)view.findViewById(R.id.victoryStreakView);
		scoreView = (TextView)view.findViewById(R.id.victoryScoreView);
		
		bugsSmashedBar = (ProgressBar)view.findViewById(R.id.victoryBugsSmashedBar);
		accuracyBar = (ProgressBar)view.findViewById(R.id.victoryAccuracyBar);
		
		continueButton.setOnClickListener(this);
		mainMenuButton.setOnClickListener(this);
		
		adView = (AdView)view.findViewById(R.id.adView);
	    
		return view;
	}

	@Override
	public void onClick(View src) {
		if (src == continueButton){
			Log.d("Victory", "CONTINUE");
			
			new AlertDialog.Builder(callback)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setMessage("Continue to next stage")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	VictoryFragment.this.getView().setVisibility(View.INVISIBLE);
					callback.startNextLevel();
		        }
	
		    })
		    .setNegativeButton("No", null)
		    .show();
			
		} else if (src == mainMenuButton){
			Log.d("Victory", "MAINMENU");
			
			new AlertDialog.Builder(callback)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setMessage("Return to Main Menu?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	VictoryFragment.this.getView().setVisibility(View.INVISIBLE);
		        	callback.returnToMainMenu();
		        }
	
		    })
		    .setNegativeButton("No", null)
		    .show();
			
		}
		
	}
	
	/**
	 * Sets the callback game activity to the given object.
	 * @param host The GameActivity object.
	 */
	public void setCallback(GameActivity host){
		this.callback = host;
	}
	
	/**
	 * Sends the request do load ads.
	 */
	public void loadAds(){
		AdRequest adRequest = new AdRequest.Builder()
	    .build();
		
	    adView.loadAd(adRequest);
	}
	
	/**
	 * Sets the given results visible.
	 * @param touchedAmount The total amount of user touches.
	 * @param bugsSpawned The total amount of bugs spawned.
	 * @param bugsSmashed The total amount of bugs smashed.
	 * @param highestFactor The highest streak/factor.
	 * @param score The score.
	 */
	public void setResults(int touchedAmount, int bugsSpawned, int bugsSmashed, int highestFactor, int score){
		double smashPercentage = ((double)bugsSmashed / (double)bugsSpawned) * 100;
		double accuracyPercentage = ((double)bugsSmashed / (double)touchedAmount) * 100;
		
		bugsSmashedView.setText("Bugs smashed: " + String.valueOf(bugsSmashed) + "/" + String.valueOf(bugsSpawned));
		bugsSmashedBar.setProgress(0);
		bugsSmashedBar.setProgress((int)smashPercentage);
		
		double accuracy = Math.round(accuracyPercentage*100.0)/100.0;
		accuracyView.setText("Accuracy: " + String.valueOf(accuracy) + "%");
		accuracyBar.setProgress(0);
		accuracyBar.setProgress((int)accuracyPercentage);
		
		streakView.setText("Highest streak: " + String.valueOf(highestFactor));
		scoreView.setText("Stage score: " + String.valueOf(score));
	}
}
