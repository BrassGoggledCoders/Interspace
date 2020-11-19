package xyz.brassgoggledcoders.interspace.manager;

import java.util.concurrent.atomic.AtomicBoolean;

public class InterspaceRunnable implements Runnable {
    private final AtomicBoolean running = new AtomicBoolean(true);

    @Override
    public void run() {
        if (running.get()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        running.set(false);
    }
}
