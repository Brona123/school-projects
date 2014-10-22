/**
 * @author Sami Joutsijoki
 * The credits window.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CreditsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.credits);
		
		Button b = (Button)findViewById(R.id.creditsBackToMainMenuButton);
		b.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				CreditsActivity.this.onBackPressed();
			}
		});
	}
}
