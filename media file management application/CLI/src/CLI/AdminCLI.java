
package CLI;

import EventSystem.EventType;
import contract.Tag;
import contract.Uploader;
import domainLogic.*;
import observerPattern.CapacityObserver;
import observerPattern.ConsoleObserver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class AdminCLI {
    private Admin admin;
    private ConsoleInputReader inputReader;
    private ConsoleOutputWriter outputWriter;
    private ExecutorService executorService;
    private Mode currentMode;
    private boolean deleteEnabled = true;
    private boolean listTagsEnabled = true;
    private boolean isRunning = true;  // Flag to control loop for testing

    public AdminCLI(ConsoleInputReader reader, ConsoleOutputWriter writer, Admin admin) {
        this.admin = admin;
        this.inputReader = reader;
        this.outputWriter = writer;
        this.executorService = Executors.newFixedThreadPool(3); // 3 threads for example
        this.currentMode = Mode.COMMAND;
        setupObservers();
    }

    private void setupObservers(){
        admin.getEventDispatcher().addListener(EventType.CAPACITY_EXCEEDED, new CapacityObserver(0.9* admin.getCapacity())); // 90% limit
        admin.getEventDispatcher().addListener(EventType.MEDIA_UPLOADED, new ConsoleObserver());
        admin.getEventDispatcher().addListener(EventType.MEDIA_DELETED, new ConsoleObserver());
        admin.getEventDispatcher().addListener(EventType.UPLOADER_DELETED, new ConsoleObserver());
        admin.getEventDispatcher().addListener(EventType.MEDIA_PLAYED, new ConsoleObserver());
    }

    public void start() {
        while (isRunning) {
            if (currentMode == Mode.COMMAND) {
                outputWriter.println("Available commands: :c (insert mode), :r (display mode), :d (delete mode), :u (modify mode), :p (persistence mode), exit");
            }
            String input = inputReader.read("input");
            handleInput(input);
        }
    }

    // Testable start method that accepts a list of commands
    public void start(List<String> commands) {
        for (String input : commands) {
            handleInput(input);
            if (!isRunning) break;  // Stop execution if isRunning is false
        }
    }

    // Handle exiting the CLI in tests
    public void stop() {
        this.isRunning = false;  // Allows exiting the loop
    }

    private void handleInput(String input) {
        switch (currentMode) {
            case COMMAND:
                switch (input) {
                    case ":c":
                        enterInsertMode();
                        break;
                    case ":r":
                        enterDisplayMode();
                        break;
                    case ":d":
                        enterDeleteMode();
                        break;
                    case ":u":
                        enterModifyMode();
                        break;
                    case ":p":
                        enterPersistenceMode();
                        break;
                    case "exit":
                        outputWriter.println("Exiting program.");
                        executorService.shutdown();
                        stop();
                        System.exit(0);
                    default:
                        outputWriter.println("Unknown command. Please enter a valid command.");
                }
                break;
            case INSERT:
                handleInsertMode(input);
                break;
            case DELETE:
                handleDeleteMode(input);
                break;
            case DISPLAY:
                handleDisplayMode(input);
                break;
            case MODIFY:
                handleModifyMode(input);
                break;
            case PERSISTENCE:
                handlePersistenceMode(input);
                break;
        }
    }

    private synchronized void enterInsertMode() {
        currentMode = Mode.INSERT;
        outputWriter.println("Insert Mode. Enter details or type 'back' to return:");
        outputWriter.println("To insert a producer: [P-Name]");
        outputWriter.println("To insert media: [Media-Type] [P-Name] [comma-separated tags, single comma for none] [size] [cost] [duration] [Sampling Rate (if audio/audiovideo)] [Resolution (if video/audiovideo)] [address] [title]");
    }

    synchronized void handleInsertMode(String details){
        if (details.equals("back")) {
            currentMode = Mode.COMMAND;
            return;
        }

        String[] parts = details.split(" ");
        if (parts.length == 1) {
            String result = admin.createUploader(parts[0]);
            outputWriter.println(result);
        }

        if (parts.length > 1) {
            insertMedia(details);
        }
    }






    synchronized void insertMedia(String details) {
        if (details.equals("back")) {
            currentMode = Mode.COMMAND;
            return;
        }
        try {
            String[] parts = details.split(" ");
            if (parts.length < 8) {
                outputWriter.println("Error: Invalid input format for insert or insufficient data provided.");
                outputWriter.println("Usage: [Media-Type] [P-Name] [comma-separated tags, single comma for none] [size] [cost] [duration] [Sampling Rate (if audio/audiovideo)] [Resolution (if video/audiovideo)] [address] [title]");
                outputWriter.println("Examples:");
                outputWriter.println("AudioVideo Producer3 Music,Review 2000 12.99 44100 1080 Address1 AudioVideo1");
                return;
            }

            String mediaType = parts[0].toLowerCase();
            String uploaderName = parts[1];
            String tagsString = parts[2];
            int size = Integer.parseInt(parts[3]);
            BigDecimal cost = new BigDecimal(parts[4]);
            int duration = Integer.parseInt(parts[5]);
            String address = parts[parts.length - 2];
            String title = parts[parts.length - 1];

            Set<Tag> tags = new HashSet<>();
            if (!tagsString.equals(",")) {
                for (String tag : tagsString.split(",")) {
                    try {
                        tags.add(Tag.valueOf(tag.trim())); // Converts string to enum
                    } catch (IllegalArgumentException e) {
                        outputWriter.println("Error: Invalid tag " + tag.trim() + "Available tags: Animal, Review, Music, News");
                        return;
                    }
                }
            }


            Integer samplingRate = null;
            Integer resolution = null;

            // default values for optional fields

            final int DEFAULT_SAMPLING_RATE = 44100;
            final int DEFAULT_RESOLUTION = 1080;

            switch (mediaType) {
                case "audio":

                    samplingRate = (parts.length > 8) ? Integer.parseInt(parts[6]) : DEFAULT_SAMPLING_RATE;

                    break;

                case "video":

                    resolution = (parts.length > 8 ) ? Integer.parseInt(parts[6]) : DEFAULT_RESOLUTION;

                    break;

                case "audiovideo":

                    samplingRate = (parts.length > 8) ? Integer.parseInt(parts[6]) : DEFAULT_SAMPLING_RATE;
                    resolution = (parts.length > 9) ? Integer.parseInt(parts[7]) : DEFAULT_RESOLUTION;

                    break;

                default:
                    outputWriter.println("Error: Unsupported media type.");
                    return;
            }


            String result = admin.uploadMedia(mediaType, uploaderName, tags, size, cost, duration, address, title, samplingRate, resolution);
            outputWriter.println(result);



        } catch (NumberFormatException e) {
            outputWriter.println("Error: Invalid number format.");
        } catch (Exception e) {
            outputWriter.println("Error: " + e.getMessage());
        }
    }

    public void setDeleteEnabled(boolean enabled) {
        this.deleteEnabled = enabled;
    }

    public void setListTagsEnabled(boolean enabled) {
        this.listTagsEnabled = enabled;
    }

    synchronized void enterDeleteMode(){
        if (!deleteEnabled) {
            outputWriter.println("Delete functionality is disabled.");
            return;
        }
        currentMode = Mode.DELETE;
        outputWriter.println("Delete Mode. Enter the producer name or media address to delete, or type 'back' to return:");
    }

    synchronized void handleDeleteMode(String details){
        if (details.equals("back")) {
            currentMode = Mode.COMMAND;
            return;
        }
        // checking if the input corresponds to a media address or uploader name
        MediaContentImpl media = admin.findMediaByAddress(details);
        if (media != null){
            admin.deleteMediaFile(details);
            outputWriter.println("Media file with address " + details + " deleted.");
        } else {
            Uploader uploader = admin.findUploaderByName(details);
            if (uploader != null) {
                admin.deleteUploader(details);
                outputWriter.println("Uploader " + details + " deleted.");
            } else {
                outputWriter.println("Error: No media file or uploader found with the provided details.");
            }
        }
    }

    private synchronized void enterModifyMode() {
        currentMode = Mode.MODIFY;
        outputWriter.println("Modify Mode. Enter address of media to modify or type 'back' to return:");
    }

    private synchronized void handleModifyMode(String details) {
        if (details.equals("back")) {
            currentMode = Mode.COMMAND;
            return;
        }

        MediaContentImpl media = admin.findMediaByAddress(details);
        if (media != null) {
            admin.playMedia(media.getAddress());
            outputWriter.println("Access count for media at address '" + details + "' incremented.");
        } else {
            outputWriter.println("Error: Media not found with address '" + details + "'.");
        }
    }

    private synchronized void enterDisplayMode() {
        outputWriter.println("Display Mode. Options: uploader, content [type], tag [include/exclude]. Type 'back' to return.");


        currentMode = Mode.DISPLAY;
    }

    synchronized void handleDisplayMode(String details) {
        if (details.equals("back")) {
            currentMode = Mode.COMMAND;
            return;
        }

        String[] parts = details.split(" ");

        String command = parts[0].toLowerCase();




        switch (command) {
            case "uploader":
                admin.getAllProducersWithMediaCount().forEach((uploader, count) ->
                        outputWriter.println("Uploader: " + uploader + ", Media Count: " + count));
                break;
            case "content" :
                String type = (parts.length > 1) ? parts[1].toLowerCase() : null;
                displaySortedMediaContent(type);
                break;
            case "tag":
                if (!listTagsEnabled) {
                    outputWriter.println("Listing tags functionality is disabled.");
                    return;
                }
                if (parts.length > 1 && parts[1].equalsIgnoreCase("include")) {
                    admin.getAllTags(true).forEach(tag -> outputWriter.println(tag.name()));
                } else if (parts.length > 1 && parts[1].equalsIgnoreCase("exclude")) {
                    admin.getAllTags(false).forEach(tag -> outputWriter.println(tag.name()));
                } else {
                    outputWriter.println("Error: Invalid tag command. Use 'tag include' or 'tag exclude'.");
                }
                break;
            default:
                outputWriter.println("Invalid display command. " + command);
        }
    }

    private synchronized void displaySortedMediaContent(String type) {
        // Retrieve media files, optionally filtering by type
        List<MediaContentImpl> mediaFiles = admin.getMediaFiles(type);

        // Sort media alphabetically by title
        mediaFiles = mediaFiles.stream()
                .sorted((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()))  // Sort by title alphabetically
                .toList();

        // Print the sorted media files
        if (mediaFiles.isEmpty()) {
            outputWriter.println("No media files found.");
        } else {
            mediaFiles.forEach(this::displayMediaContent);
        }
    }

    private synchronized void enterPersistenceMode() {
        currentMode = Mode.PERSISTENCE;
        outputWriter.println("Persistence Mode. Options: save [JOS|JBP] filename, load [JOS|JBP] filename. Type 'back' to return.");
    }

    private synchronized void displayMediaContent(MediaContentImpl media) {
        outputWriter.println("Title: " + media.getTitle() +
                ", Address: " + media.getAddress() +
                ", Access Count: " + media.getAccessCount() +
                ", Availability (days): " + media.getAvailabilityInDays() +
                ", Uploader: " + media.getUploader().getName() +
                ", Tags: " + media.getTags().stream().map(Tag::name).collect(Collectors.joining(", ")));
    }

    private synchronized void handlePersistenceMode(String details) {
        if (details.equals("back")) {
            currentMode = Mode.COMMAND;
            return;
        }

        String[] parts = details.split(" ");
        if (parts.length < 3) {
            outputWriter.println("Error: Invalid persistence command format. Use 'save/load [JOS|JBP] filename'.");
            return;
        }

        String action = parts[0].toLowerCase();
        String method = parts[1].toLowerCase();
        String filename = parts[2];

        try {
            switch (action) {
                case "save":
                    if (method.equals("jos")) {
                        admin.saveStateJOS(filename + ".jos");
                        outputWriter.println("State saved to " + filename + ".jos using JOS.");
                    } else if (method.equals("jbp")) {
                        admin.saveStateJBP(filename + ".jbp");
                        outputWriter.println("State saved to " + filename + ".jbp using JBP.");
                    } else {
                        outputWriter.println("Error: Invalid method. Use 'JOS' or 'JBP'.");
                    }
                    break;
                case "load":
                    if (method.equals("jos")) {
                        admin = Admin.loadStateJOS(filename + ".jos");
                        outputWriter.println("State loaded from " + filename + ".jos using JOS.");
                    } else if (method.equals("jbp")) {
                        admin = Admin.loadStateJBP(filename + ".jbp");
                        outputWriter.println("State loaded from " + filename + ".jbp using JBP.");
                    } else {
                        outputWriter.println("Error: Invalid method. Use 'JOS' or 'JBP'.");
                    }
                    break;
                default:
                    outputWriter.println("Error: Invalid action. Use 'save' or 'load'.");
            }
        } catch (IOException | ClassNotFoundException e) {
            outputWriter.println("Error during persistence operation: " + e.getMessage());
        }
    }



/*    private synchronized void handleInsertMode() {
        outputWriter.println("enter title: ");
        String title = inputReader.read("title");
        outputWriter.println("enter sampling rate: ");
        int samplingRate = Integer.parseInt(inputReader.read("samplingRate"));
        outputWriter.println("enter duration: ");
        int duration = Integer.parseInt(inputReader.read("duration"));
        outputWriter.println("enter address: ");
        String address = inputReader.read("address");

        AudioImpl audio = new AudioImpl(title, samplingRate, duration, address);
        admin.insert(audio);
        outputWriter.println("insertion successful for " + title + "at address: " + address);
    }

    private synchronized void displayAudios() {
        outputWriter.println("list of all audios: ");
        admin.list().forEach(audio -> outputWriter.println("Title: " + audio.getTitle() + ", Access Count: " + audio.getAccessCount() + ", Address: " + audio.getAddress()));
    }

    private synchronized void modifyAudioAccess() {
        outputWriter.println("enter address of audio to modify: ");
        String address = inputReader.read("address");
        AudioImpl audio = (AudioImpl) admin.findAudioByAddress(address);
        if (audio != null) {
            audio.incrementAccessCount();
            outputWriter.println("access count for '" + address + "' incremented to: " + audio.getAccessCount());
        } else {
            outputWriter.println("audio not found");
        }
    }

    private synchronized void removeAudio() {
        outputWriter.println("enter the address of the audio to remove: ");
        String address = inputReader.read("address");
        if (admin.delete(address)) {
            outputWriter.println("audio removed successfully");
        } else {
            outputWriter.println("no audio found with the address: " + address);
        }
    }

    private synchronized void saveStateJOS() {
        outputWriter.println("enter file name to save: ");
        String filename = inputReader.read("filename");
        try {
            admin.saveStateJOS(filename + ".jos");
            outputWriter.println("State saved to " + filename + ".jos");
        } catch (IOException e) {
            outputWriter.println("failed to save state: " + e.getMessage());
        }
    }

    private synchronized void loadStateJOS() {
        outputWriter.println("enter filename to load state using JOS: ");
        String filename = inputReader.read("filename");
        try {
            admin = Admin.loadStateJOS(filename + ".jos");
            outputWriter.println("State loaded from " + filename + ".jos");
        } catch (IOException | ClassNotFoundException e) {
            outputWriter.println("Failed to load state: " + e.getMessage());
        }
    }

    private synchronized void saveStateJBP() {
        outputWriter.println("enter filename to save using JBP: ");
        String filename = inputReader.read("filename");
        try {
            admin.saveStateJBP(filename + ".jbp");
            outputWriter.println("State saved to " + filename + ".jbp");
        } catch (IOException e) {
            outputWriter.println("failed to save state: " + e.getMessage());
        }
    }

    private synchronized void loadStateJBP() {
        outputWriter.println("enter filename to load state using JBP: ");
        String filename = inputReader.read("filename");
        try {
            admin = Admin.loadStateJBP(filename + ".jbp");
            outputWriter.println("State loaded from " + filename + ".jbp");
        } catch (IOException e) {
            outputWriter.println("Failed to load state: " + e.getMessage());
        }
    }

    public void starts() {
        while (true) {
            outputWriter.println("Available commands: :c (insert), :d (delete), :r (display), :u (modify), :p (persistence), exit");
            String command = inputReader.read("command");
            outputWriter.println("Received command: " + command);
            switch (command) {
                case ":c":
                    handleInsertMode();
                    break;
                case ":r":
                    displayAudios();
                    break;
                case "u:":
                    modifyAudioAccess();
                    break;
                case "d:":
                    removeAudio();
                    break;
                case ":p":
                    saveStateJOS();
                    break;
                case "loadJOS":
                    loadStateJOS();
                    break;
                case "saveJBP":
                    saveStateJBP();
                    break;
                case "loadJBP":
                    loadStateJBP();
                    break;
                case "exit":
                    outputWriter.println("program exited");
                    executorService.shutdown();
                    return;
                default:
                    outputWriter.println("invalid command");
            }
        }
    }
*/




    private enum Mode {
        COMMAND, INSERT, DELETE, DISPLAY, MODIFY, PERSISTENCE
    }
}
