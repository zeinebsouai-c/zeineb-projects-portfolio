
package Simulation;

import contract.Tag;
import domainLogic.*;

import java.math.BigDecimal;
import java.util.*;

public class SimulationUtils {

    private static final Random random = new Random();

    public static Set<Tag> generateRandomTags() {
        Set<Tag> tags = new HashSet<>();
        Tag[] allTags = Tag.values();
        tags.add(allTags[random.nextInt(allTags.length)]);
        return tags;
    }

    private static String generateRandomMediaType() {
        String[] mediaTypes = {"audio", "video", "audiovideo"};
        return mediaTypes[random.nextInt(mediaTypes.length)];
    }

    // generates random media details based on the provided logic
    public static Map<String, Object> generateRandomMediaDetails(int uploaderId) {
        Map<String, Object> mediaDetails = new HashMap<>();

        String mediaType = generateRandomMediaType();
        String uploaderName = "Uploader_" + uploaderId;
        Set<Tag> tags = generateRandomTags();
        int size = random.nextInt(10) + 1;
        BigDecimal cost = BigDecimal.valueOf(random.nextInt(100) + 1);
        int duration = random.nextInt(300) + 60; // duration between 1 and 5 minutes
        Integer samplingRate = (mediaType.equals("audio") || mediaType.equals("audiovideo") ? 44100 : null);
        Integer resolution = (mediaType.equals("video") || mediaType.equals("audiovideo") ? 1080 : null);
        String address = UUID.randomUUID().toString();
        String title = mediaType + "_Title_" + random.nextInt(1000);

        // populating the media details map

        mediaDetails.put("mediaType", mediaType);
        mediaDetails.put("uploaderName", uploaderName);
        mediaDetails.put("tags", tags);
        mediaDetails.put("size", size);
        mediaDetails.put("cost", cost);
        mediaDetails.put("duration", duration);
        mediaDetails.put("samplingRate", samplingRate);
        mediaDetails.put("resolution", resolution);
        mediaDetails.put("address", address);
        mediaDetails.put("title", title);

        return mediaDetails;

    }

    public static String insertRandomMedia(int uploaderId, Admin admin){
        Map<String, Object> mediaDetails = generateRandomMediaDetails(uploaderId);

        String uploaderName =(String) mediaDetails.get("uploaderName");
        // ensuring uploader exists
        try {
            admin.createUploader(uploaderName);
        } catch (IllegalArgumentException ignored) {}

        return admin.uploadMedia(
                (String) mediaDetails.get("mediaType"),
                uploaderName,
                (Set<Tag>) mediaDetails.get("tags"),
                (int) mediaDetails.get("size"),
                (BigDecimal) mediaDetails.get("cost"),
                (int) mediaDetails.get("duration"),
                (String) mediaDetails.get("address"),
                (String) mediaDetails.get("title"),
                (Integer) mediaDetails.get("samplingRate"),
                (Integer) mediaDetails.get("resolution")
        );

    }


    // find media with the lowest access count
    public static MediaContentImpl getMediaWithLowestAccessCount(Admin admin) {
        return admin.getMediaFiles(null)
                .stream()
                .min(Comparator.comparingLong(MediaContentImpl::getAccessCount))
                .orElse(null);
    }



}
