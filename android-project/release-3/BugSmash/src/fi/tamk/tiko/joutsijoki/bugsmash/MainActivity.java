package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Button startButton;
	private Button hiScoreButton;
	private Button exitButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initButtons();
	}
	
	private void initButtons(){
		startButton = (Button)findViewById(R.id.startGame);
		hiScoreButton = (Button)findViewById(R.id.hiScore);
		exitButton = (Button)findViewById(R.id.exit);
		
		startButton.setOnClickListener(this);
		hiScoreButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View src) {
		
		if (src == startButton){
			Intent i = new Intent(this, GameActivity.class);
			startActivity(i);
		} else if (src == hiScoreButton){
			Intent i = new Intent(this, HighscoreActivity.class);
			startActivity(i);
		} else if (src == exitButton){
			finish();
		}
	}
}
