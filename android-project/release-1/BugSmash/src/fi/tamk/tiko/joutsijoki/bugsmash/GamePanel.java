package fi.tamk.tiko.joutsijoki.bugsmash;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	private Background background;
	private ArrayList<Bug> bugContainer;
	private MainThread thread;
	private Thread bugSpawnThread;
	private static final String TAG = GamePanel.class.getSimpleName();
	private int screenWidth;
	private int screenHeight;
	private ScoreChangedListener host;
	
	public GamePanel(Context context, AttributeSet attr){
		super(context, attr);
		this.host = (ScoreChangedListener)context;
		
		Log.d(TAG, "Constructor");
		getHolder().addCallback(this);
		background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bugsmashbackground));
		bugContainer = new ArrayList<Bug>();
		
		
		bugSpawnThread = new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					for (int i = 0; i < 20; i++){
						Bug ant = new Bug(BitmapFactory.decodeResource(getResources(), R.drawable.ant)
										  , i*(-32)
										  , i*32);
						bugContainer.add(ant);
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		bugSpawnThread.start();
		
		thread = new MainThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
		setFocusable(true);
		initScreenDimensions(context);
	}
	
	@SuppressLint("NewApi")
	private void initScreenDimensions(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		this.screenWidth = size.x;
		this.screenHeight = size.y;
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if (canvas != null){
			canvas.drawColor(Color.WHITE);
			Log.d("GamePanel", "DRAWING");
			drawBackground(canvas);
			drawBugs(canvas);
		}
	}
	
	private void drawBugs(Canvas canvas){
		for (int i = 0; i < bugContainer.size(); i++){
			Bug temp;
			if ((temp = bugContainer.get(i)) != null){
				temp.draw(canvas);
				//temp.rotate();
			}
		}
	}
	
	private void drawBackground(Canvas canvas){
		if (background != null && canvas != null){
			if (background.getOffsetX() < (background.getWidth() - this.screenWidth)){
				background.draw(canvas);
			} else {
				thread.setRunning(false);
				bugSpawnThread = null;
				System.gc();
				
				
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("GamePanel", "TOUCHED");
		
		float x = event.getX();
		float y = event.getY();
		checkCollision(x, y);
		return false;
	}
	
	private void checkCollision(float x, float y){
		boolean hit = false;
		
		for (Bug bug : bugContainer){
			if (bug.collidesWith(x, y)){
				bug.destroy();
				host.onScoreChanged();
				hit = true;
			}
		}
		
		if (!hit){
			host.resetFactor();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}
}
