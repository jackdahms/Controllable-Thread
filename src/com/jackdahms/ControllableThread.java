package com.jackdahms;

public class ControllableThread implements Runnable{

	Controllable target;
	Thread base;
	
	boolean running;
	
	double ups;
	double fps;
	
	int targetUps;
	int targetFps;
	
	int maxFrameskip;
	
	public ControllableThread(Controllable target) {
		this.target = target;
		
		running = true;
		
		targetUps = 20;
		targetFps = 60;
				
		maxFrameskip = 5;
		
		base = new Thread(this);
	}
	
	//fixed timestep loop
	public void run() {
		
		double updateTime = 1000000000 / targetUps;
		double targetRenderTime = 1000000000 / targetFps;
		
		int frameCount = 0;
		
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
		
		boolean paused = false;
		
		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;
			
			if (!paused) {
				
				//update, catch up if needed
				while (now - lastUpdateTime > updateTime && updateCount < maxFrameskip) {
					target.update();
					lastUpdateTime += updateTime;
					updateCount++;
				}
				
				//so the game doesn't do an insane amount of catch ups
				if (now - lastUpdateTime > updateTime) {
					lastUpdateTime = now - updateTime;
				}
				
				//render
				float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / updateTime));
				target.render(interpolation);
				frameCount++;
				lastRenderTime = now;
				
				//update frames
				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime) {
//					System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}
				
				while (now - lastRenderTime < targetRenderTime && now - lastUpdateTime < updateTime) {
					Thread.yield();
					
					//stops program from consuming all your cpu
					//makes it slightly less accurate
					//can be removed to improve the game at the cost of the cpu
					try {
						Thread.sleep(1);
					} catch (Exception e) {}
					
					now = System.nanoTime();
				}
			}
		}		
	}
	
	public void start() {
		base.start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void setTargetUps(int targetUps) {
		this.targetUps = targetUps;
	}
	
	public void setTargetFps(int targetFps) {
		this.targetFps = targetFps;
	}
	
	public void setMaxFrameskip(int maxFrameskip) {
		this.maxFrameskip = maxFrameskip;
	}
	
	public double getUps() {
		return ups;
	}
	
	public double getFps() {
		return fps;
	}
	
}
