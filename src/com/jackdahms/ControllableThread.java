package com.jackdahms;

public class ControllableThread implements Runnable {

    Controllable target;
    Thread base;

    boolean running;
    volatile boolean paused;
    boolean hog; //if the thread can hog the cpu

    int fps;

    int targetUps;
    int targetFps;

    int maxFrameskip;

    public ControllableThread(Controllable target) {
        this.target = target;

        this.running = true;
        this.paused = false;
        this.hog = false;

        this.targetUps = 20;
        this.targetFps = 60;

        this.maxFrameskip = 5;

        this.base = new Thread(this);
    }

    //fixed timestep loop
    @Override
    public void run() {

        double updateTime = 1000000000 / this.targetUps;
        double targetRenderTime = 1000000000 / this.targetFps;

        int frameCount = 0;

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (this.running) {
            double now = System.nanoTime();
            int updateCount = 0;

            if (!this.paused) {

                //update, catch up if needed
                while (now - lastUpdateTime > updateTime
                        && updateCount < this.maxFrameskip) {
                    this.target.update();
                    lastUpdateTime += updateTime;
                    updateCount++;
                }

                //so the game doesn't do an insane amount of catch ups
                if (now - lastUpdateTime > updateTime) {
                    lastUpdateTime = now - updateTime;
                }

                //render
                float interpolation = Math.min(1.0f,
                        (float) ((now - lastUpdateTime) / updateTime));
                this.target.render(interpolation);
                frameCount++;
                lastRenderTime = now;

                //update frames
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime) {
                    this.fps = frameCount;
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }

                while (now - lastRenderTime < targetRenderTime
                        && now - lastUpdateTime < updateTime) {
                    Thread.yield();

                    if (!this.hog) {
                        try {
                            Thread.sleep(1);
                        } catch (Exception e) {
                        }
                    }

                    now = System.nanoTime();
                }
            }
        }
    }

    public void start() {
        this.base.start();
    }

    public void stop() {
        this.running = false;
    }

    public void setHog(boolean hog) {
        this.hog = hog;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
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

    public int getFps() {
        return this.fps;
    }

}
