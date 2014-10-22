package fi.tamk.tiko.joutsijoki.bugsmash;

import java.util.ArrayList;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BugSpawnThread extends Thread {
	private boolean running = false;
	private Resources res;
	private ArrayList<Bug> bugContainer;
	private int bugAmount = 20;
	public static final int TRIANGLE_FORMATION = 1;
	public static final int LINE_FORMATION = 2;
	private int formation;
	

	public BugSpawnThread(){
		super();
		bugContainer = new ArrayList<Bug>();
		formation = LINE_FORMATION;
	}
	
	@Override
	public void run(){
		while(running){
			randomizeFormation();
			spawnBugs();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void randomizeFormation(){
		int[] formationArray = {TRIANGLE_FORMATION, LINE_FORMATION};
		Random ran = new Random();
		int index = ran.nextInt(2);
		this.formation = formationArray[index];
	}
	
	private void spawnBugs(){
		switch (formation){
			case TRIANGLE_FORMATION:
				spawnTriangleFormation();
				break;
			case LINE_FORMATION:
				spawnLineFormation((int)(Math.random()*3) + 1);
				break;
		}
	}
	
	private void spawnTriangleFormation(){
		for (int i = 0; i < bugAmount; i++){
			Bug ant = null;
			int j = i - bugAmount/2;
			if (i < bugAmount/2){
				ant = new Bug(BitmapFactory.decodeResource(res, R.drawable.ant_spritesheet)
							  , i*(bugAmount) - (bugAmount*(bugAmount/2))
							  , i*32
							  , (int)(Math.random()*3)+2
							  , 2);
			} else {
				ant = new Bug(BitmapFactory.decodeResource(res, R.drawable.ant_spritesheet)
						  , -j*bugAmount
						  , i*32
						  , (int)(Math.random()*3)+2
						  , 2);
				
			}
			bugContainer.add(ant);
		}
	}
	
	private void spawnLineFormation(int lines){
		for (int i = 0; i < lines; i++){
			for (int j = 0; j < bugAmount/lines; j++){
				Bug ant = new Bug(BitmapFactory.decodeResource(res, R.drawable.ant_spritesheet)
								  , -j * 40
								  , 32*(i*lines)
								  , (int)(Math.random()*3)+2
								  , 2);
				bugContainer.add(ant);
			}
		}
	}
	
	public void setRunning(boolean flag){
		this.running = flag;
	}
	
	public void drawBugs(Canvas canvas){
		for (int i = 0; i < bugContainer.size(); i++){
			Bug temp;
			if ((temp = bugContainer.get(i)) != null){
				temp.draw(canvas);
			}
		}
	}
	
	public void moveBugs(){
		for (int i = 0; i < bugContainer.size(); i++){
			Bug temp;
			if ((temp = bugContainer.get(i)) != null){
				temp.update(System.currentTimeMillis());
				temp.setPosX(temp.getPosX() + 2);
			}
		}
	}
	
	public boolean checkCollision(float x, float y, ScoreChangedListener host){
		boolean hit = false;
		for (int i = 0; i < bugContainer.size(); i++){
			Bug temp;
			if ((temp = bugContainer.get(i)) != null){
				if (temp.collidesWith(x, y)){
					temp.destroy();
					host.onScoreChanged();
					hit = true;
				}
			}
		}
		return hit;
	}
	
	public ArrayList<Bug> getBugContainer(){
		return this.bugContainer;
	}
	
	public void setBugAmount(int amount){
		this.bugAmount = amount;
	}
	
	public void setResources(Resources res){
		this.res = res;
	}
}
