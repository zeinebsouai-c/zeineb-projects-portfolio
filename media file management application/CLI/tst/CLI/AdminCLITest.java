package CLI;   /*
    package CLI;

    import contract.Tag;
    import domainLogic.Admin;
    import domainLogic.*;
    import domainLogic.MediaContentImpl;
    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.Mockito;
    import CLI.AdminCLI;

    import java.math.BigDecimal;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    import static org.mockito.ArgumentMatchers.contains;
    import static org.mockito.Mockito.*;


    public class AdminCLITest {

            private AdminCLI adminCLI;
            private Admin admin;
            private ConsoleInputReader inputReader;
            private ConsoleOutputWriter outputWriter;

            @BeforeEach
            void setUp() {
                admin = Mockito.mock(Admin.class);
                inputReader = Mockito.mock(ConsoleInputReader.class);
                outputWriter = Mockito.mock(ConsoleOutputWriter.class);
                adminCLI = new AdminCLI(inputReader, outputWriter, admin);
            }


        @Test
        public void testExitCommand() {
            // Simulate just the exit command
            List<String> commands = List.of("exit");

            // Run the CLI with the exit command
            adminCLI.start(commands);

            // Verify that the CLI outputs the exit message
            verify(outputWriter, times(1)).println("Exiting program.");
            verifyNoMoreInteractions(admin);  // No admin actions since we only exit
        }




        @Test
        public void testInsertMode_CreateUploader() {
            // Simulate entering insert mode, creating an uploader, and exiting
            when(inputReader.read("input")).thenReturn(":c", "UploaderName", "exit");

            // Start CLI logic
            new Thread(() -> adminCLI.start()).start();

            // Verify the instructional output for insert mode
            verify(outputWriter).println("Available commands: :c (insert mode), :r (display mode), :d (delete mode), :u (modify mode), :p (persistence mode), exit");
            verify(outputWriter).println("Insert Mode. Enter details or type 'back' to return:");
            verify(outputWriter).println("To insert a producer: [P-Name]");
            verify(outputWriter).println("To insert media: [Media-Type] [P-Name] [comma-separated tags, single comma for none] [size] [cost] [duration] [Sampling Rate (if audio/audiovideo)] [Resolution (if video/audiovideo)] [address] [title]");

            // Verify uploader creation
            verify(admin, times(1)).createUploader("UploaderName");

            // Verify that "Uploader created" message was printed
            verify(outputWriter).println("Uploader created: UploaderName");

            // Ensure the CLI exits correctly
            verify(outputWriter).println("Exiting program.");
        }


        // Test insert mode for media insertion
        @Test
        public void testInsertMode_InsertMedia() {
            // Simulate entering insert mode and inserting a media file
            when(inputReader.read("input")).thenReturn(":c", "Audio TestUploader Music, 1000 15.00 120 44100 Address Title", "back");

            // Create necessary mocks for tags
            Set<Tag> tags = new HashSet<>();
            tags.add(Tag.Music);

            // Simulate successful media upload
            when(admin.uploadMedia("audio", "TestUploader", tags, 1000, new BigDecimal("15.00"), 120, "Address", "Title", 44100, null))
                    .thenReturn("Media uploaded successfully: Title");

            // Run the CLI logic
            adminCLI.start();

            // Verify interactions for media upload
            verify(admin, times(1)).uploadMedia("audio", "TestUploader", tags, 1000, new BigDecimal("15.00"), 120, "Address", "Title", 44100, null);
            verify(outputWriter, times(1)).println("Media uploaded successfully: Title");
        }

        @Test
            public void testInsertAudioMedia() {
                // Given
                admin.createUploader("zozo");
                Set<Tag> tags = new HashSet<>();
                tags.add(Tag.Music);
                when(inputReader.read("input")).thenReturn("audio zozo Music 67 78 788 44100 tokyo echoes");

                // When
                adminCLI.insertMedia("audio zozo Music 67 78 788 44100 tokyo echoes");

                // Then
                MediaContentImpl media = admin.findMediaByAddress("tokyo");
                Assertions.assertNotNull(media);
                Assertions.assertEquals("zozo", media.getUploader().getName());
                Assertions.assertEquals("echoes", media.getTitle());
                Assertions.assertEquals(tags, media.getTags());
                verify(outputWriter).println("Media uploaded successfully.");
            }

            @Test
            public void testHandleInsertModeBack() {
                // Given
                when(inputReader.read("input")).thenReturn("back");

                // When
                adminCLI.handleInsertMode("back");

                // Then
                verify(outputWriter).println("Available commands: :c (insert mode), :r (delete mode), :d (display mode), :u (modify mode), :p (persistence mode), exit");
            }

            @Test
            public void testInsertMediaVideo() {
                // Given
                when(inputReader.read("input")).thenReturn("zozo");
                admin.createUploader("zozo");
                String mediaDetails = "video zozo Music 24 30 300 1080 tokyo videoTitle";

                // When
                adminCLI.handleInsertMode(mediaDetails);

                // Then
                MediaContentImpl media = admin.findMediaByAddress("tokyo");
                Assertions.assertNotNull(media);
                Assertions.assertEquals("videoTitle", media.getTitle());
                verify(outputWriter).println("Media uploaded successfully.");
            }

            @Test
            public void testDisplayMode() {
                // Given
                admin.createUploader("zozo");
                admin.uploadMedia(new AudioImpl(admin.findUploaderByName("zozo"), Set.of(Tag.Music), 24, new BigDecimal("30.00"), 300, 44100, "tokyo", "echoes"), "zozo");
                when(inputReader.read("display")).thenReturn("content");

                // When
                adminCLI.handleDisplayMode("content");

                // Then
                verify(outputWriter).println(contains("Title: echoes, Address: tokyo, Access Count: 0, Uploader: zozo"));
            }

            @Test
            public void testHandleDisplayModeUploader() {
                // Given
                admin.createUploader("zozo");
                admin.createUploader("zozo");
                adminCLI.insertMedia("audio zozo Music 67 78 788 44100 tokyo echoes");
                when(inputReader.read("display")).thenReturn("uploader");

                // When
                adminCLI.handleDisplayMode("uploader");

                // Then
                verify(outputWriter).println("Uploader: zozo, Media Count: 1");
            }


            @Test
            public void testHandleDisplayModeContent() {
                // Given
                admin.createUploader("zozo");
                admin.createUploader("zozo");
                adminCLI.insertMedia("audio zozo Music 67 78 788 44100 tokyo echoes");
                when(inputReader.read("display")).thenReturn("content");

                // When
                adminCLI.handleDisplayMode("content");

                // Then
                verify(outputWriter).println("Title: echoes, Address: tokyo, Access Count: 0, Uploader: zozo, Tags: Music");
            }

            @Test
            public void testHandleDisplayModeTagInclude() {
                // Given
                when(inputReader.read("display")).thenReturn("tag include");

                // When
                adminCLI.handleDisplayMode("tag include");

                // Then
                verify(outputWriter).println("Music");
                verify(outputWriter).println("Animal");
                verify(outputWriter).println("Review");
                verify(outputWriter).println("News");
            }

            @Test
            public void testHandleDisplayModeTagExclude() {
                // Given
                admin.createUploader("zozo");
                adminCLI.insertMedia("audio zozo Music 67 78 788 44100 tokyo echoes");
                when(inputReader.read("display")).thenReturn("tag exclude");

                // When
                adminCLI.handleDisplayMode("tag exclude");

                // Then
                verify(outputWriter).println("Animal");
                verify(outputWriter).println("Review");
                verify(outputWriter).println("News");
            }

            @Test
            public void testEnterDeleteModeDisabled() {
                // Given
                adminCLI.setDeleteEnabled(false);

                // When
                adminCLI.enterDeleteMode();

                // Then
                verify(outputWriter).println("Delete functionality is disabled.");
            }

            @Test
            public void testHandleDeleteModeUploader() {
                // Given
                admin.createUploader("zozo");
                when(inputReader.read("input")).thenReturn("zozo");

                // When
                adminCLI.handleDeleteMode("zozo");

                // Then
                Assertions.assertNull(admin.findUploaderByName("zozo"));
                verify(outputWriter).println("Uploader zozo deleted.");
            }

        }


    */

   import org.junit.jupiter.api.BeforeEach;
   import org.junit.jupiter.api.Test;
   import org.mockito.Mockito;

   import java.io.IOException;
   import java.math.BigDecimal;
   import java.util.HashSet;
   import java.util.Map;
   import java.util.Set;

   import contract.Tag;
   import domainLogic.Admin;
   import domainLogic.MediaContentImpl;

   import static org.mockito.Mockito.*;

   public class AdminCLITest {

       private AdminCLI adminCLI;
       private ConsoleInputReader inputReader;
       private ConsoleOutputWriter outputWriter;
       private Admin admin;

       @BeforeEach
       public void setUp() {
           inputReader = Mockito.mock(ConsoleInputReader.class);
           outputWriter = Mockito.mock(ConsoleOutputWriter.class);
           admin = Mockito.mock(Admin.class);
           adminCLI = new AdminCLI(inputReader, outputWriter, admin);
       }

       @Test
       public void testInsertMode_CreateUploader() {
           // Simulate entering insert mode, creating an uploader, and exiting
           when(inputReader.read("input")).thenReturn(":c", "UploaderName", "exit");

           // Start CLI logic
           new Thread(() -> adminCLI.start()).start();
           verify(outputWriter).println("Available commands: :c (insert mode), :r (display mode), :d (delete mode), :u (modify mode), :p (persistence mode), exit");

           // Verify instructional output for insert mode
           verify(outputWriter).println("Insert Mode. Enter details or type 'back' to return:");
           verify(outputWriter).println("To insert a producer: [P-Name]");
           verify(outputWriter).println("To insert media: [Media-Type] [P-Name] [comma-separated tags, single comma for none] [size] [cost] [duration] [Sampling Rate (if audio/audiovideo)] [Resolution (if video/audiovideo)] [address] [title]");

           // Verify uploader creation
           verify(admin, times(1)).createUploader("UploaderName");

           // Verify that "Uploader created" message was printed
           verify(outputWriter).println("Uploader created: UploaderName");

           // Ensure the CLI exits correctly
           verify(outputWriter).println("Exiting program.");
       }

       @Test
       public void testInsertMode_InsertMedia() {
           // Simulate entering insert mode, inserting media, and then exiting
           when(inputReader.read("input")).thenReturn(":c", "Audio UploaderName Music, 1000 15.00 120 44100 Address Title", "exit");

           // Create necessary mock for the tags
           Set<Tag> tags = new HashSet<>();
           tags.add(Tag.Music);

           // Simulate successful media upload
           when(admin.uploadMedia("audio", "UploaderName", tags, 1000, new BigDecimal("15.00"), 120, "Address", "Title", 44100, null))
                   .thenReturn("Media uploaded successfully: Title");

           // Start CLI logic
           new Thread(() -> adminCLI.start()).start();

           // Verify media insertion
           verify(admin, times(1)).uploadMedia("audio", "UploaderName", tags, 1000, new BigDecimal("15.00"), 120, "Address", "Title", 44100, null);

           // Verify that the success message was printed
           verify(outputWriter).println("Media uploaded successfully: Title");

           // Ensure the CLI exits correctly
           verify(outputWriter).println("Exiting program.");
       }

       @Test
       public void testDeleteMode() {
           // Simulate entering delete mode, deleting a media file, and then exiting
           when(inputReader.read("input")).thenReturn(":d", "Address", "exit");

           // Mock finding media by address
           MediaContentImpl media = Mockito.mock(MediaContentImpl.class);
           when(admin.findMediaByAddress("Address")).thenReturn(media);

           // Start CLI logic
           new Thread(() -> adminCLI.start()).start();

           // Verify media deletion
           verify(admin, times(1)).deleteMediaFile("Address");
           verify(outputWriter).println("Media file with address Address deleted.");

           // Ensure the CLI exits correctly
           verify(outputWriter).println("Exiting program.");
       }

       @Test
       public void testDisplayMode_Uploader() {
           // Simulate entering display mode and displaying uploader information, then exiting
           when(inputReader.read("input")).thenReturn(":r", "uploader", "exit");

           // Mock getting all producers with media count
           when(admin.getAllProducersWithMediaCount()).thenReturn(Map.of("Uploader1", 5L));

           // Start CLI logic
           new Thread(() -> adminCLI.start()).start();

           // Verify that the uploader list was printed
           verify(outputWriter, times(1)).println("Uploader: Uploader1, Media Count: 5");

           // Ensure the CLI exits correctly
           verify(outputWriter).println("Exiting program.");
       }

       @Test
       public void testModifyMode() {
           // Simulate entering modify mode and modifying a media file, then exiting
           when(inputReader.read("input")).thenReturn(":u", "Address", "exit");

           // Mock finding media by address
           MediaContentImpl media = Mockito.mock(MediaContentImpl.class);
           when(admin.findMediaByAddress("Address")).thenReturn(media);

           // Start CLI logic
           new Thread(() -> adminCLI.start()).start();

           // Verify media access count increment
           verify(admin, times(1)).playMedia("Address");
           verify(outputWriter).println("Access count for media at address 'Address' incremented.");

           // Ensure the CLI exits correctly
           verify(outputWriter).println("Exiting program.");
       }

       @Test
       public void testPersistenceMode_SaveJOS() throws IOException {
           // Simulate entering persistence mode and saving state with JOS, then exiting
           when(inputReader.read("input")).thenReturn(":p", "save jos filename", "exit");

           // Start CLI logic
           new Thread(() -> adminCLI.start()).start();

           // Verify that the admin state was saved
           verify(admin, times(1)).saveStateJOS("filename.jos");
           verify(outputWriter).println("State saved to filename.jos using JOS.");

           // Ensure the CLI exits correctly
           verify(outputWriter).println("Exiting program.");
       }

       @Test
       public void testPersistenceMode_LoadJOS() throws IOException, ClassNotFoundException {
           // Simulate entering persistence mode and loading state with JOS, then exiting
           when(inputReader.read("input")).thenReturn(":p", "load jos filename", "exit");

           // Start CLI logic
           new Thread(() -> adminCLI.start()).start();

           // Verify that the admin state was loaded
           verify(admin, times(1)).loadStateJOS("filename.jos");
           verify(outputWriter).println("State loaded from filename.jos using JOS.");

           // Ensure the CLI exits correctly
           verify(outputWriter).println("Exiting program.");
       }
   }
