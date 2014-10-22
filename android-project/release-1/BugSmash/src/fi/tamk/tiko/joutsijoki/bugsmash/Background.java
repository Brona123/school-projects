package fi.tamk.tiko.joutsijoki.bugsmash;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
	private int startX;
	private int startY;
	private int width;
	private int height;
	private int offsetX;
	private Bitmap image;
	private boolean atEnd;
	
	public Background(Bitmap image){
		this.image = image;
		this.startX = 0;
		this.startY = 0;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	public void draw(Canvas canvas) {
        canvas.drawBitmap(image, startX, startY, null);
        startX--;
        offsetX++;
    }
	
	public int getX(){
		return this.startX;
	}
	
	public int getOffsetX(){
		return this.offsetX;
	}
	
	public int getWidth(){
		return this.width;
	}
	
}
