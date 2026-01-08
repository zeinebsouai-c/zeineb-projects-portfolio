package Simulation;

import domainLogic.Admin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Monitor implements Runnable {
    private final Admin admin;
    private final int interval;
    private final ScheduledExecutorService scheduler;

    public Monitor(Admin admin, int interval){
        this.admin = admin;
        this.interval = interval;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void run(){
        System.out.println("=== Current State of Media Files ===");
        admin.getMediaFiles(null).forEach(media -> System.out.println("Title: " + media.getTitle() + ", Address: " + media.getAddress() +
                ", Access Count: " + media.getAccessCount() + ", Uploader: " + media.getUploader().getName()));
        System.out.println("====================================");
    }

    // starting the monitor at fixed intervals
    public void start(){
        scheduler.scheduleAtFixedRate(this, 0, interval, TimeUnit.MILLISECONDS);
    }

    // stopping the monitor gracefully
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1,TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
