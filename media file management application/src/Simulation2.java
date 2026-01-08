
import EventSystem.EventDispatcher;
import EventSystem.EventType;
import Simulation.SimulationUtils;
import domainLogic.Admin;
import observerPattern.CapacityObserver;
import observerPattern.ConsoleObserver;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Simulation2 {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Simulation2 <capacity> <n>");
            return;
        }

        int capacity = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);

        // initializing admin with capacity
        Admin admin = new Admin(capacity, new EventDispatcher());

        // registering observers
        admin.getEventDispatcher().addListener(EventType.CAPACITY_EXCEEDED, new CapacityObserver(capacity * 0.9));
        admin.getEventDispatcher().addListener(EventType.MEDIA_UPLOADED, new ConsoleObserver());
        admin.getEventDispatcher().addListener(EventType.MEDIA_DELETED, new ConsoleObserver());


        ExecutorService executorService = Executors.newFixedThreadPool(n * 2); // n inserting and n deleting threads

        // starting n producer threads and n consumer threads
        for (int i = 0; i < n; i++) {
            executorService.execute(new Producer(admin, i));
        }

        for (int i = 0; i < n; i++) {
            executorService.execute(new Consumer(admin, i));
        }

        // gracefully shutting down the executor service

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }));
    }

    // Producer Thread class

    static class Producer implements Runnable {
        private final Admin admin;
        private final int id;
        private final Random random = new Random();

        public Producer(Admin admin, int id) {
            this.admin = admin;
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {

                System.out.println("[Producer-" + id + "] attempting to upload media...");

                String result = SimulationUtils.insertRandomMedia(id, admin);

                System.out.println("[Producer-" + id + "] " + result);

                try {
                    Thread.sleep(0); // set to 0 for submission
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final Admin admin;
        private final int id;

        public Consumer(Admin admin, int id) {
            this.admin = admin;
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // retrieving all media files
                    var mediaFiles = admin.getMediaFiles(null);

                    if (mediaFiles.isEmpty()) {
                        System.out.println("[Consumer-" + id + " No media files available.");
                    } else {
                        // randomly selecting a media file to delete
                        int index = new Random().nextInt(mediaFiles.size());
                        String address = mediaFiles.get(index).getAddress();

                        System.out.println("[Consumer-" + id + "] attempting to delete media at address:  " + address);

                        admin.deleteMediaFile(address);
                    }

                    Thread.sleep(0); // set to 0 for submission
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }

        }
    }
}


