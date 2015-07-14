package com.jackdahms;

public class ControllableThread implements Runnable{

	Controllable target;
	Thread base;
	
	boolean running;
	boolean fixedTimestep; //must be set before loop is started
	
	double ups;
	double fps;
	
	int targetUps;
	int targetFps;
	
	public ControllableThread(Controllable target) {
		this.target = target;
		
		running = true;
		fixedTimestep = true;
		
		targetUps = 20;
		targetFps = 60;
				
		base = new Thread(this);
	}
	
	public void run() {
		if (fixedTimestep) { //best for games that need synchronization (e.g. multiplayer)
			
			long lastFpsTime;
			
			long lastLoopTime = System.nanoTime();
			long optimalFps = 1000000000 / targetFps;
			
			while (running) {
				//calculate time since last update
				long now = System.nanoTime();
				long updateLength = now - lastLoopTime;
				lastLoopTime = now;
				double delta = updateLength / (double) optimalFps;
				
				//update frame counter
				
				
				target.update();
				target.render();
			}
		} else { //variable timestep
			
		}
	}
	
	public void start() {
		base.start();
	}

	public boolean isFixedTimestep() {
		return fixedTimestep;
	}

	public void setFixedTimestep(boolean fixedTimestep) {
		this.fixedTimestep = fixedTimestep;
	}
	
}
