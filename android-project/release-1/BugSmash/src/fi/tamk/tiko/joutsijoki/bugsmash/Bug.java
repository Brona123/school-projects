package fi.tamk.tiko.joutsijoki.bugsmash;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Matrix;

public class Bug {
	private Bitmap image;
	private int startX;
	private int startY;
	private int width;
	private int height;
	private int degree;
	
	public Bug(Bitmap image, int startX, int startY){
		this.image = image;
		this.startX = startX;
		this.startY = startY;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.degree = 0;
	}
	
	
	
	public void draw(Canvas canvas) {
		if (canvas != null && !image.isRecycled()){
			canvas.drawBitmap(image, startX, startY, null);
        	startX += 2;
        	startY += (int)(Math.random() * 3);
		}
    }
	
	public boolean collidesWith(float x, float y){
		if (x > startX
		    && x < startX + this.width
		    && y > startY
		    && y < startY + this.height){
			return true;
		}
		return false;
	}
	
	public void destroy(){
		this.image.recycle();
		
	}
	
	public void rotate(){
	    degree++;
	    	Matrix matrix = new Matrix();
		    matrix.postRotate(degree/100);
	    	this.image = Bitmap.createBitmap(this.image
								    		, 0
								    		, 0
								    		, image.getWidth()
								    		, image.getHeight()
								    		, matrix
								    		, true);
	    
	}
}
