package fi.tamk.tiko.joutsijoki.bugsmash;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;
	private static final String TAG = MainThread.class.getSimpleName();
	
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
		while (running){
			canvas = null;
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder){
					this.gamePanel.onDraw(canvas);
				}
			} finally {
				if (canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
