package fi.tamk.tiko.joutsijoki.bugsmash;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	private Background background;
	private MainThread thread;
	private BugSpawnThread bugSpawnThread;
	private static final String TAG = GamePanel.class.getSimpleName();
	private int screenWidth;
	private int screenHeight;
	private ScoreChangedListener scoreHost;
	private MapCompletedListener mapHost;
	private MapProgressChanged progressHost;
	private int currentLevel = 1;
	private int gameFieldHeight;
	
	public GamePanel(Context context, AttributeSet attr){
		super(context, attr);
		this.scoreHost = (ScoreChangedListener)context;
		this.mapHost = (MapCompletedListener)context;
		this.progressHost = (MapProgressChanged)context;
		initScreenDimensions(context);
		
		Log.d(TAG, "Constructor");
		getHolder().addCallback(this);
		
		bugSpawnThread = new BugSpawnThread();
		bugSpawnThread.setResources(getResources());
		bugSpawnThread.setRunning(true);
		bugSpawnThread.start();
		
		thread = new MainThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
		setFocusable(true);
	}
	
	public void startNextLevel(){
		switch (currentLevel){
		case 1:
			startSecondLevel();
			break;
		case 2:
			startThirdLevel();
			break;
		}
		
		currentLevel++;
	}
	
	private void startSecondLevel(){
		Log.d(TAG, "STARTING SECOND LEVEL");
		
		background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.sand)
				, gameFieldHeight);
		background.setScrolling(true);
		background.setScrollingSpeed(2);
		
		bugSpawnThread = new BugSpawnThread();
		bugSpawnThread.setResources(getResources());
		bugSpawnThread.setRunning(true);
		bugSpawnThread.start();
		
		thread = new MainThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
		
		Log.d(TAG, "BACKGROUND OFFSET X: " + background.getOffsetX());
	}
	
	private void startThirdLevel(){
		background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.water)
				, gameFieldHeight);
		background.setScrolling(true);
		background.setScrollingSpeed(3);
		
		bugSpawnThread = new BugSpawnThread();
		bugSpawnThread.setResources(getResources());
		bugSpawnThread.setRunning(true);
		bugSpawnThread.start();
		
		thread = new MainThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}
	
	public void update(){
		bugSpawnThread.moveBugs();
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
			drawBackground(canvas);
			bugSpawnThread.drawBugs(canvas);
		}
	}
	
	private void drawBackground(Canvas canvas){
		if (background != null && canvas != null){
			
			if (background.getOffsetX() < (background.getWidth() - this.screenWidth)){
				background.draw(canvas);
				double x = background.getOffsetX();
				double end = background.getWidth() - this.screenWidth;
				progressHost.progressChanged((int)(x / end * 100));
				Log.d(TAG, "DOUBLE x: " + x + " DOUBLE END: " + end);
			} else {
				thread.setRunning(false);
				bugSpawnThread.setRunning(false);
				mapHost.mapCompleted();
				Log.d(TAG, "MAP COMPLETED");
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		checkCollision(x, y);
		return false;
	}
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
	     super.onSizeChanged(xNew, yNew, xOld, yOld);

	     int viewWidth = xNew;
	     int viewHeight = yNew;
	     this.gameFieldHeight = yNew;
	     
	     background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grass)
					, viewHeight);
	     background.setScrolling(true);
	     background.setScrollingSpeed(1);
	     
	     Log.d(TAG, "GAMEPANEL HEIGHT: " + viewHeight + " GAMEPANEL WIDTH: " + viewWidth);
	}
	
	private void checkCollision(float x, float y){
		boolean hit = false;
		hit = bugSpawnThread.checkCollision(x, y, scoreHost);
		
		if (!hit){
			scoreHost.resetFactor();
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
		// TODO Auto-generated method stub
		
	}
}
