/**
 * @author Sami Joutsijoki
 * The class that handles the main execution loop of the app.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	
	/**
	 * The boolean that controls the thread running.
	 */
	private boolean running;
	
	/**
	 * The surface holder callback for rendering.
	 */
	private SurfaceHolder surfaceHolder;
	
	/**
	 * The gamepanel to call updates.
	 */
	private GamePanel gamePanel;
	
	/**
	 * Indicates the maximum fps.
	 */
	private static final int MAX_FPS = 60;
	
	/**
	 * Indicates the maximum frame skips before slowing down the game.
	 */
	private static final int MAX_FRAME_SKIPS = 12;
	
	/**
	 * Indicates the time between frame updates in milliseconds.
	 */
	private static final int FRAME_PERIOD = 1000 / MAX_FPS;
	
	/**
	 * Creates a new mainthread with the given parameters.
	 * @param sh The SurfaceHolder for rendering.
	 * @param gp The GamePanel for updating.
	 */
	public MainThread(SurfaceHolder sh, GamePanel gp){
		super();
		this.surfaceHolder = sh;
		this.gamePanel = gp;
	}
	
	/**
	 * Sets the thread running or not running.
	 * @param flag The boolean to control the running.
	 */
	public void setRunning(boolean flag){
		this.running = flag;
	}
	
	/**
	 * Returns true if the thread is running, false otherwise.
	 * @return True if the thread is running, false otherwise.
	 */
	public boolean getRunning(){
		return this.running;
	}
	
	@SuppressLint("WrongCall")
	@Override
	public void run(){
		Canvas canvas;
		
		long beginTime;
		long timeDiff;
		int sleepTime;
		int framesSkipped;
		
		sleepTime = 0;
		
		while (running){
			canvas = null;
			
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder){
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;
					
					this.gamePanel.update();
					this.gamePanel.onDraw(canvas);
					
					timeDiff = System.currentTimeMillis() - beginTime;
					
					sleepTime = (int) (FRAME_PERIOD - timeDiff);
					
					if (sleepTime > 0){
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e){
							e.printStackTrace();
						}
					}
					
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS){
						this.gamePanel.update();
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {
				if (canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
