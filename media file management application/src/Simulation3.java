
import Simulation.Monitor;
import Simulation.SimulationUtils;
import domainLogic.Admin;

import domainLogic.MediaContentImpl;
import java.util.*;



public class Simulation3 {
    private final Admin admin;
    private final int n;
    private final int interval;
    private static final Object lock = new Object();
    private final Monitor monitor;

    public Simulation3(int capacity, int n, int interval) {
        this.admin = new Admin(capacity);
        this.n = n;
        this.interval = interval;
        this.monitor = new Monitor(admin, interval); // monitor instance variable
    }

    public void start() {
        // starting the inserting threads
        for (int i = 0; i < n; i++) {
            new Thread(new Producer(admin, i)).start();
        }

        // starting the deleting threads
        for (int i = 0; i < n; i++) {
            new Thread(new Consumer(admin, i)).start();
        }

        // starting the accessing threads
        for (int i = 0; i < n; i++) {
            new Thread(new Accessor(admin, i)).start();
        }

        // starting the monitor thread
        monitor.start();

        // ensuring proper shutdown when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down monitor...");
            monitor.stop();
        } ));
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Simulation3 <capacity> <number of threads> <interval in milliseconds>");
            return;
        }

        int capacity = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int interval = Integer.parseInt(args[2]);

        new Simulation3(capacity, n, interval).start();

    }

    static class Producer implements Runnable {
        private final Admin admin;
        private final int id;

        public Producer(Admin admin, int id) {
            this.admin = admin;
            this.id = id;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(random.nextInt(1000)); // sleep between insertions
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (lock) {
                    while (admin.getUsedCapacity() >= admin.getCapacity()) {
                        try {
                            System.out.println("[Producer " + id + "] Waiting: capacity full.");
                            lock.wait(); // wait until notified by the consumer
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // Inserting random media
                    String result = SimulationUtils.insertRandomMedia(id, admin);
                    if (!result.startsWith("Error")) {
                        System.out.println("[Producer " + id + "] " + result);
                        lock.notifyAll();
                    }
                }

            }
        }
    }

    // Consumer class to delete the media with the lowest access count
    static class Consumer implements Runnable {
        private final Admin admin;
        private final int id;
        private final Random random = new Random();

        public Consumer(Admin admin, int id) {
            this.admin = admin;
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(random.nextInt(1000)); // sleep between deletions
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (lock) {
                    while (admin.getMediaFiles(null).isEmpty()) {
                        try {
                            System.out.println("(Consumer " + id + "] Waiting: no media to delete.");
                            lock.wait(); // wait until notified by the producer
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    MediaContentImpl mediaToDelete = SimulationUtils.getMediaWithLowestAccessCount(admin);

                    if (mediaToDelete != null) {
                        admin.deleteMediaFile(mediaToDelete.getAddress());
                        System.out.println("[Consumer " + id + "] deleted media with address: " + mediaToDelete.getAddress());
                        lock.notifyAll(); // notify other threads
                    }
                }
            }
        }
    }

    // Accessor class to simulate playing media files

    static class Accessor implements Runnable {
        private final Admin admin;
        private final int id;
        private final Random random = new Random();

        public Accessor(Admin admin, int id) {
            this.admin = admin;
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(random.nextInt(1000)); // sleep between accesses
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (lock) {
                    if (admin.getMediaFiles(null).isEmpty()) {
                        System.out.println("[Accessor " + id + "] No media files to access.");
                        continue;
                    }

                    MediaContentImpl mediaToAccess = admin.getMediaFiles(null).get(random.nextInt(admin.getMediaFiles(null).size()));

                    if (mediaToAccess != null) {
                        admin.playMedia(mediaToAccess.getAddress());
                        System.out.println("[Accessor " + id + "] Accessed media: " + mediaToAccess.getTitle() + ", Access count: " + mediaToAccess.getAccessCount());
                    }
                }
            }
        }
    }


}






