package fi.tamk.tiko.joutsijoki.bugsmash;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;
	private static final String TAG = MainThread.class.getSimpleName();
	private static final int MAX_FPS = 60;
	private static final int MAX_FRAME_SKIPS = 12;
	private static final int FRAME_PERIOD = 1000 / MAX_FPS;
	private int testctr = 0;
	
	public MainThread(SurfaceHolder sh, GamePanel gp){
		super();
		this.surfaceHolder = sh;
		this.gamePanel = gp;
	}
	
	public void setRunning(boolean flag){
		this.running = flag;
	}
	
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
			testctr++;
			Log.d("MainThread", "RUNNING " + testctr);
			
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
