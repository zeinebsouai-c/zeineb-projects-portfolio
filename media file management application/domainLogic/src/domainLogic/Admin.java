
package domainLogic;


import contract.Audio;
import contract.MediaContent;
import contract.Tag;
import contract.Uploader;
import observerPattern.Observer;
import observerPattern.Subject;
import observerPattern.CapacityObserver;
import EventSystem.*;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.CopyOnWriteArraySet;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Audio> audioList = new ArrayList<>();
    private final List<MediaContentImpl> mediaList = new ArrayList<>();
    private final Map<String, Uploader> uploaders = new HashMap<>();
    private final Set<Tag> tags = new HashSet<>();
    private int capacity ;
    private int usedCapacity;
    private EventDispatcher eventDispatcher;
    boolean observerDeactivate= false;



    public Admin(int capacity, EventDispatcher eventDispatcher){
        this.capacity = capacity;
        this.eventDispatcher = eventDispatcher;
    }

    // constructor that does not accept EventDispatcher (used in Simulation 3)
    public Admin(int capacity) {
        this.capacity = capacity;
        observerDeactivate = true;
    }


    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public int getCapacity(){
        return capacity;
    }


    public int getUsedCapacity(){
        return usedCapacity;
    }


    // CREATE operations

    public synchronized String createUploader(String name) {
        if (uploaders.containsKey(name)){
            throw new IllegalArgumentException("Error: Uploader with the same name already exists");
        }
        uploaders.put(name, new UploaderImpl(name));
        return "Uploader created successfully: " + name;

    }

    public synchronized String uploadMedia(String mediaType, String uploaderName, Set<Tag> tags, int size,
                                           BigDecimal cost, int duration, String address, String title,
                                           Integer samplingRate, Integer resolution){



        Uploader uploader = findUploaderByName(uploaderName);

        if (uploader == null) {
            return "Error: Uploader '" + uploaderName + "' not found.";
        }

        MediaContentImpl media;

        switch (mediaType.toLowerCase()) {
            case "audio":
                if (samplingRate == null) return "Error: Sampling rate required for audio.";
                media = new AudioImpl(uploader, tags, size, cost, duration, samplingRate, address, title);
                break;
            case "video":
                if (resolution == null) return "Error: Resolution required for video.";
                media = new VideoImpl(uploader, tags, size, cost, duration, resolution, address, title);
                break;
            case "audiovideo":
                if (samplingRate == null || resolution == null) return "Error: Sampling rate and resolution required for audio-video.";
                media = new AudioVideoImpl(uploader, tags, size, cost, duration, samplingRate, resolution, address, title);
                break;
            default:
                return "Error: Unsupported media type.";
        }

        media.setUploader(uploader);


        return uploadMedia(media, uploaderName);
    }

    // uploading media to list

    public synchronized String uploadMedia(MediaContentImpl media, String uploaderName){


        // checking total capacity before adding media
        if (usedCapacity + media.getSize() > capacity) {
            return "Error: Exceeding total capacity.";
        }

        if (usedCapacity + media.getSize() > capacity* 0.9) {

            if (!observerDeactivate && eventDispatcher != null) {
                eventDispatcher.dispatchEvent(EventType.CAPACITY_EXCEEDED, usedCapacity + media.getSize());
            }
        }


        // ensuring media uniqueness by address
        if (media.getAddress() == null || media.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }

        for (MediaContent existingMedia : mediaList){
            if (existingMedia.getAddress().equals(media.getAddress())){
                throw new IllegalArgumentException("Address already exists");
            }
        }


        mediaList.add(media);
        usedCapacity += media.getSize();


        if (!observerDeactivate && eventDispatcher != null) {
            eventDispatcher.dispatchEvent(EventType.MEDIA_UPLOADED, media);

        }
        //checkCapacity();

        return "Media uploaded: " + media.getTitle();
    }

    // Method to check capacity and trigger event if needed
    /* private void checkCapacity() {
        if (usedCapacity > capacity * 0.9) {
            eventDispatcher.dispatchEvent(EventType.CAPACITY_EXCEEDED, usedCapacity);
        }
    }

     */




    // READ operations

    public synchronized List<MediaContentImpl> getMediaFiles(String type){
        if (type == null){
            return new ArrayList<>(mediaList);
        }
        return mediaList.stream()
                .filter(media -> media.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public synchronized List<Uploader> getUploaders() {
        return new ArrayList<>(uploaders.values());
    }

    public synchronized Map<String, Long> getAllProducersWithMediaCount() {
        return uploaders.values().stream()
                .collect(Collectors.toMap(
                        Uploader::getName,
                        uploader -> mediaList.stream()
                                .filter(media -> uploader.equals(media.getUploader()))
                                .count()
                ));
    }


    // retrieving all assigned or unassigned tags
    public synchronized Set<Tag> getAllTags(boolean assigned) {
        Set<Tag> allTags = EnumSet.allOf(Tag.class); // All tags defined in the enum

        if (assigned) {
            return mediaList.stream()
                    .flatMap(media -> {
                        Collection<Tag> mediaTags = media.getTags();
                        if (mediaTags != null) {
                            return mediaTags.stream();
                        } else {
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.toSet());
        } else {
            Set<Tag> assignedTags = getAllTags(true);
            allTags.removeAll(assignedTags); // Remove assigned tags to get unassigned ones
            return allTags;
        }
    }



    // UPDATE operations

    public synchronized void playMedia(String address){
        MediaContentImpl media = findMediaByAddress(address);
        if (media != null){
            media.incrementAccessCount();
            if (!observerDeactivate && eventDispatcher != null) {
                eventDispatcher.dispatchEvent(EventType.MEDIA_PLAYED, media.getTitle());
            }
        } else {
            throw new IllegalArgumentException("Media not found");
        }
    }


    // DELETE operations

    public synchronized String deleteUploader(String name){
        if (!uploaders.containsKey(name)){
            throw new IllegalArgumentException("Uploader does not exist");
        }
        uploaders.remove(name);
        mediaList.removeIf(media -> media.getUploader().getName().equals(name));

        if (!observerDeactivate && eventDispatcher != null) {
            eventDispatcher.dispatchEvent(EventType.UPLOADER_DELETED, name);
        }

        return "Uploader deleted: " + name;
    }

    public synchronized String deleteMediaFile(String address){
        MediaContentImpl media = findMediaByAddress(address);
        if (media == null){
            throw new IllegalArgumentException("Media not found");
        }
        mediaList.remove(media);
        usedCapacity -= media.getSize();

        if (!observerDeactivate && eventDispatcher != null) {
            eventDispatcher.dispatchEvent(EventType.MEDIA_DELETED, address);
        }

        return "Media deleted: " + address;
    }





    public synchronized MediaContentImpl findMediaByAddress(String address){
        return mediaList.stream().filter(media -> media.getAddress().equals(address))
                .findFirst().orElse(null);
    }

    public synchronized Uploader findUploaderByName(String name) {
        return uploaders.get(name);
    }





    // Saving the state using JOS
    public synchronized void saveStateJOS(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    // Loading the state using JOS
    public static Admin loadStateJOS(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {

            return (Admin) in.readObject();
        }
    }

    // Converting to DTOs for JBP
    private List<MediaContentDTO> toDTOs() {
        return mediaList.stream()
                .map(media -> {
                    Set<String> tagNames = media.getTags().stream()
                            .map(Tag::name)
                            .collect(Collectors.toSet());
                    if (media instanceof AudioVideoImpl) {
                        AudioVideoImpl av = (AudioVideoImpl) media;
                        return new MediaContentDTO(av.getUploader().getName(), tagNames, av.getSize(), av.getCost(), av.getDuration(),
                                av.getSamplingRate(), av.getResolution(), av.getAddress(), av.getTitle(), "audiovideo");
                    } else if (media instanceof VideoImpl) {
                        VideoImpl video = (VideoImpl) media;
                        return new MediaContentDTO(video.getUploader().getName(), tagNames, video.getSize(), video.getCost(), video.getDuration(),
                                0, video.getResolution(), video.getAddress(), video.getTitle(), "video");
                    } else if (media instanceof AudioImpl) {
                        AudioImpl audio = (AudioImpl) media;
                        return new MediaContentDTO(audio.getUploader().getName(), tagNames, audio.getSize(), audio.getCost(), audio.getDuration(),
                                audio.getSamplingRate(), 0, audio.getAddress(), audio.getTitle(), "audio");
                    }
                    return null;  // This should never happen
                })
                .collect(Collectors.toList());
    }

    // Converting from DTOs for JBP
    private void fromDTOs(List<MediaContentDTO> dtos) {
        mediaList.clear();
        for (MediaContentDTO dto : dtos) {
            Uploader uploader = uploaders.get(dto.getUploaderName());
            if (uploader == null) {
                throw new IllegalArgumentException("Uploader not found: " + dto.getUploaderName());
            }

            Set<Tag> tags = dto.getTags().stream()
                    .map(Tag::valueOf)
                    .collect(Collectors.toSet());

            MediaContentImpl media;
            switch (dto.getType().toLowerCase()) {
                case "audio":
                    media = new AudioImpl(uploader, tags, dto.getSize(), dto.getCost(), dto.getDuration(), dto.getSamplingRate(), dto.getAddress(), dto.getTitle());
                    break;
                case "video":
                    media = new VideoImpl(uploader, tags, dto.getSize(), dto.getCost(), dto.getDuration(), dto.getResolution(), dto.getAddress(), dto.getTitle());
                    break;
                case "audiovideo":
                    media = new AudioVideoImpl(uploader, tags, dto.getSize(), dto.getCost(), dto.getDuration(), dto.getSamplingRate(), dto.getResolution(), dto.getAddress(), dto.getTitle());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown media type: " + dto.getType());
            }
            mediaList.add(media);
        }
    }


    // Saving the state using JBP
    public synchronized void saveStateJBP(String filename) throws IOException {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)))) {
            encoder.writeObject(toDTOs());
        }
    }

    // Loading the state using JBP
    public static Admin loadStateJBP(String filename) throws IOException {
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)))) {
            @SuppressWarnings("unchecked")
            List<MediaContentDTO> dtos = (List<MediaContentDTO>) decoder.readObject();
            EventDispatcher eventDispatcher1 = new EventDispatcher();
            Admin admin = new Admin(100, eventDispatcher1);
            admin.fromDTOs(dtos);
            return admin;
        }
    }
}
