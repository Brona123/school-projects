/**
 * @author Sami Joutsijoki
 * The class that handles user touch input and displays everything.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
	
	/**
	 * The background object that handles the background logic.
	 */
	private Background background;
	
	/**
	 * The main thread that handles updating and rendering calls.
	 */
	private MainThread thread;
	
	/**
	 * The bug spawning thread that handles bug spawning.
	 */
	private BugSpawnThread bugSpawnThread;
	
	/**
	 * The tag used for debugging.
	 */
	private static final String TAG = GamePanel.class.getSimpleName();
	
	/**
	 * Indicates the screen width.
	 */
	private int screenWidth;
	
	/**
	 * Callback interface for updating score on a different fragment.
	 */
	private ScoreChangedListener scoreHost;
	
	/**
	 * Callback interface for handling stage completed events.
	 */
	private MapCompletedListener mapHost;
	
	/**
	 * Callback interface for updating stage progress.
	 */
	private MapProgressChanged progressHost;
	
	/**
	 * Callback interface for game completion event.
	 */
	private GameCompletedListener gameCompletedHost;
	
	/**
	 * Indicates the current level index, default 0.
	 */
	private int currentLevel = 0;
	
	/**
	 * Indicates the game field height.
	 */
	private int gameFieldHeight;
	
	/**
	 * Indicates the game field width.
	 */
	private int gameFieldWidth;
	
	/**
	 * Handles the soundtracks.
	 */
	private MediaPlayer mPlayer;
	
	/**
	 * Holds the array of soundtracks.
	 */
	private int [] playList = {R.raw.bugsmash_theme6, R.raw.bugsmash_theme5, R.raw.bugsmash_theme4};
	
	/**
	 * Holds the array of background images.
	 */
	private int [] backgrounds = {R.drawable.grass, R.drawable.sand, R.drawable.asphalt};
	
	/**
	 * Indicates the current song, default 0.
	 */
	private int songIndex = 0;
	
	/**
	 * Indicates if the stage is completed.
	 */
	private boolean mapCompleted = false;
	
	/**
	 * Handles the sound effects.
	 */
	private final SoundPool sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
	
	/**
	 * Holds sound effect IDs.
	 */
	private int [] soundIds = new int[2];
	
	/**
	 * Indicates the amount of bugs smashed.
	 */
	private int bugsSmashed;
	
	/**
	 * Indicates the total times the user touched the screen.
	 */
	private int touchedAmount;
	
	/**
	 * Creates a new gamepanel with the given context and attributeset.
	 * @param context The context where this view is created.
	 * @param attr The possible attributes.
	 */
	public GamePanel(Context context, AttributeSet attr){
		super(context, attr);
		this.scoreHost = (ScoreChangedListener)context;
		this.mapHost = (MapCompletedListener)context;
		this.progressHost = (MapProgressChanged)context;
		this.gameCompletedHost = (GameCompletedListener)context;
		initScreenDimensions(context);
		
		loadSoundEffects(context);
		
		mPlayer = MediaPlayer.create(context, playList[songIndex]);
		mPlayer.setLooping(true);
		mPlayer.start();
		
		Log.d(TAG, "Constructor");
		getHolder().addCallback(this);
		
		startLevel(5000, 1);
		setFocusable(true);
	}
	
	/**
	 * Starts a new level with the given interval 
	 * and background scrolling speed.
	 * @param spawnInterval The amount of milliseconds between bug spawns.
	 * @param scrollSpeed The speed of the scrolling background.
	 */
	private void startLevel(int spawnInterval, int scrollSpeed){
		resetStats();
		
		if (gameFieldHeight != 0){
			background = new Background(BitmapFactory.decodeResource(getResources(), backgrounds[currentLevel])
					, gameFieldHeight);
			background.setScrolling(true);
			background.setScrollingSpeed(scrollSpeed);
		}
		
		bugSpawnThread = new BugSpawnThread(spawnInterval);
		bugSpawnThread.setAreaDimensions(gameFieldWidth, gameFieldHeight);
		bugSpawnThread.setResources(getResources());
		bugSpawnThread.setRunning(true);
		bugSpawnThread.start();
		
		thread = new MainThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}
	
	/**
	 * Resets the stats.
	 */
	private void resetStats(){
		this.touchedAmount = 0;
		this.bugsSmashed = 0;
	}
	
	/**
	 * Loads the sound effects from the given context.
	 * @param ctx The context of the sound effects.
	 */
	private void loadSoundEffects(Context ctx){
		soundIds[0] = sp.load(ctx, R.raw.squish0, 1);
		soundIds[1] = sp.load(ctx, R.raw.squish1, 1);
	}
	
	/**
	 * Starts the next level based on the current level.
	 */
	public void startNextLevel(){
		switch (currentLevel){
		case 0:
			currentLevel++;
			startLevel(5000, 1);
			playNextTrack();
			break;
		case 1:
			currentLevel++;
			startLevel(5000, 1);
			playNextTrack();
			break;
		case 2:
			endGame();
			break;
		}
	}
	
	/**
	 * Plays the next soundtrack in the list.
	 */
	private void playNextTrack(){
		songIndex = (songIndex + 1) % playList.length;
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(playList[songIndex]);
        
        try {
	        mPlayer.reset();
	        mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
	        mPlayer.prepare();
	        mPlayer.setLooping(true);
	        mPlayer.start();
	        afd.close();
        } catch (IOException e){
        	Log.d(TAG, "Unable to play track");
        }
	}
	
	/**
	 * Ends the game.
	 */
	private void endGame(){
		gameCompletedHost.onGameCompleted();
	}
	
	/**
	 * Updates the bug locations.
	 */
	public void update(){
		if (background != null && bugSpawnThread != null){
			bugSpawnThread.moveBugs(background.getOffsetX(), this.gameFieldWidth, background.getScrolling());
		}
	}
	
	/**
	 * Initializes screen dimensions of the given context.
	 * @param context The given context.
	 */
	@SuppressLint("NewApi")
	private void initScreenDimensions(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		this.screenWidth = size.x;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		if (canvas != null){
			drawBackground(canvas);
			bugSpawnThread.drawBugs(canvas);
		}
	}
	
	/**
	 * Draws the background image to the given canvas.
	 * @param canvas The canvas to be drawn to.
	 */
	private void drawBackground(Canvas canvas){
		if (background != null && canvas != null){
			
			if (background.getOffsetX() < (background.getWidth() - this.screenWidth)){
				background.draw(canvas);
				double x = background.getOffsetX();
				double end = background.getWidth() - this.screenWidth;
				progressHost.progressChanged((int)(x / end * 100));
			} else {
				background.draw(canvas);
				
				if (!mapCompleted){
					mapCompleted = true;
					bugSpawnThread.setRunning(false);
					background.setScrolling(false);
					mPlayer.stop();
					
					Thread completedTimer = new Thread(new Runnable(){
						
						@Override
						public void run(){
							try {
								Thread.sleep(6000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							if (background != null){
								background.cleanUp();
							}
							if (thread != null){
								thread.setRunning(false);
							}
							if (bugSpawnThread != null){
								bugSpawnThread.cleanUp();
							}
							mapHost.mapCompleted(GamePanel.this.touchedAmount, bugSpawnThread.getBugsSpawned(), GamePanel.this.bugsSmashed);
							mapCompleted = false;
						}
					});
					completedTimer.start();
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.touchedAmount++;
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
	     this.gameFieldWidth = xNew;
	     
	     bugSpawnThread.setAreaDimensions(gameFieldWidth, gameFieldHeight);
	     background = new Background(BitmapFactory.decodeResource(getResources(), backgrounds[currentLevel])
					, viewHeight);
	     background.setScrolling(true);
	     background.setScrollingSpeed(1);
	     
	     Log.d(TAG, "GAMEPANEL HEIGHT: " + viewHeight + " GAMEPANEL WIDTH: " + viewWidth);
	}
	
	/**
	 * Checks the collision of bugs with the given coordinates.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	private void checkCollision(float x, float y){
		boolean hit = false;
		hit = bugSpawnThread.checkCollision(x, y, scoreHost);
		
		if (!hit){
			scoreHost.resetFactor();
		} else {
			this.bugsSmashed++;
			int rnd = (int)Math.floor(Math.random()*2);
			sp.play(soundIds[rnd], 1.0f, 1.0f, 1, 0, 1.0f);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		thread.setRunning(false);
		thread = null;
		
		bugSpawnThread.setRunning(false);
		bugSpawnThread = null;
		
		background = null;
		
		mPlayer.stop();
		mPlayer.release();
		mPlayer = null;
		
		System.gc();
	}
	
	/**
	 * Pauses the game by pausing main thread and bug spawn thread.
	 */
	public void pauseGame(){
		thread.setRunning(false);
		bugSpawnThread.pauseThread();
	}
	
	/**
	 * Resumes the game by resuming main thread and bug spawn thread.
	 */
	public void resumeGame(){
		thread.setRunning(true);
		thread.run();
		bugSpawnThread.resumeThread();
	}
}
