import domainLogic.*;
import contract.Tag;
import observerPattern.CapacityObserver;
import observerPattern.ConsoleObserver;
import EventSystem.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Simulation1 {
    public static void main(String[] args){

        // default capacity argument
        int capacity = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;

        // EventDispatcher and Admin setup
        EventDispatcher dispatcher = new EventDispatcher();
        Admin admin = new Admin(capacity,dispatcher);

        // setting up observers
        dispatcher.addListener(EventType.CAPACITY_EXCEEDED, new CapacityObserver(0.9*capacity));
        dispatcher.addListener(EventType.MEDIA_UPLOADED, new ConsoleObserver());
        dispatcher.addListener(EventType.MEDIA_DELETED, new ConsoleObserver());

        // creating producer(media insertion) and consumer(media retrieval/deletion) threads

        Thread producerThread = new Thread(new MediaProducer(admin));
        Thread consumerThread = new Thread(new MediaConsumer(admin));


        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class MediaProducer implements Runnable {
        private final Admin admin;
        private final Random random = new Random();
        private final String[] mediaTypes = {"audio", "video", "audiovideo"};

        public MediaProducer(Admin admin){
            this.admin = admin;
        }

        @Override
        public void run(){
            while (true) {
                try {
                    // generating random media details
                    String mediaType = mediaTypes[random.nextInt(mediaTypes.length)];
                    String uploaderName = "Uploader_" + random.nextInt(100);
                    int size = random.nextInt(100) + 1;
                    BigDecimal cost = BigDecimal.valueOf(random.nextDouble() * 10);
                    int duration = random.nextInt(300) + 1 ;
                    String address = "Address_" + random.nextInt(1000);
                    String title = mediaType + "_Title_" + random.nextInt(1000);

                    Set<Tag> tags = new HashSet<>();
                    tags.add(Tag.Music); // fixed tag (simpler)


                    // ensuring uploader exists
                    try {
                        admin.createUploader(uploaderName);
                    } catch (IllegalArgumentException ignored) {}


                    // uploading the media

                    String result = admin.uploadMedia(mediaType, uploaderName, tags, size, cost, duration, address,title,random.nextInt(44100) + 1, random.nextInt(1080) + 1);
                    System.out.println("[Producer] " + result);

                    Thread.sleep(0);

                } catch (Exception e) {
                    System.err.println("[Producer] Error: " + e.getMessage());
                }
            }
        }

    }

    static class MediaConsumer implements Runnable {
        private final Admin admin;
        private final Random random = new Random();

        public MediaConsumer(Admin admin){
            this.admin = admin;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // getting all media files
                    var mediaFiles = admin.getMediaFiles(null);



                    if (!mediaFiles.isEmpty()){
                        // randomly selecting one media file to delete
                        MediaContentImpl mediaToDelete = mediaFiles.get(random.nextInt(mediaFiles.size()));
                        String result = admin.deleteMediaFile(mediaToDelete.getAddress());
                        System.out.println("[Consumer] " + result);
                    } else {
                        System.out.println("[Consumer] No media files available.");
                    }

                    Thread.sleep(0);

                }
                catch (Exception e) {
                    System.err.println("[Consumer] Error: " + e.getMessage());
                }
            }
        }

    }
}
