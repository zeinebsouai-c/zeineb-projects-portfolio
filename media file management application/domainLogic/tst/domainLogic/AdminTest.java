package domainLogic;

import EventSystem.EventDispatcher;
import domainLogic.Admin;
import domainLogic.MediaContentImpl;
import domainLogic.AudioImpl;
import observerPattern.CapacityObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import contract.Audio;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import contract.Tag;
import contract.Uploader;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminTest {

    private Admin admin;
    private AudioImpl testAudio1;
    private AudioImpl testAudio2;
    private CapacityObserver capacityObserver;
    private EventDispatcher eventDispatcher;

    @BeforeEach
    public void setUp() {
        admin = new Admin(10000, eventDispatcher);

        capacityObserver = Mockito.mock(CapacityObserver.class);


    }
    //@BeforeEach is used to make sure the setup method is executed before every other method
    //source junit.org https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html#:~:text=%40BeforeEach%20is%20used%20to%20signal,in%20the%20current%20test%20class.


    // Helper method to insert media files
    private void insertMediaFiles(int size) {
        Set<Tag> tags = new HashSet<>();

        // Simulating media insertion without exceeding capacity
        admin.uploadMedia(
                "audio",
                "TestUploader",
                tags,
                size,
                BigDecimal.valueOf(10),
                200,
                "Address1",
                "Title1",
                44100,
                null
        );
    }




    @Test
    public void testCapacityObserverNotNotifiedIfCapacityBelow90Percent() {
        // Insert media files within 90% of capacity
        insertMediaFiles(8000); // Less than 90% of capacity

        // Verify that the observer was never called
        verify(capacityObserver, never()).update();
    }

    @Test
    public void testCapacityObserverNotifiesWhenExceeding90Percent() {
        // Insert media content until 90% of the capacity is reached
        insertMediaFiles(8800); // This does not exceed 90% of the capacity

        // Verify that the observer has not been triggered yet
        verify(capacityObserver, never()).update();

        // Insert more media to exceed the 90% threshold
        insertMediaFiles(210); // This will cross the 90% threshold

        // Verify that the observer was notified
        verify(capacityObserver, atLeastOnce()).update();
    }




    @Test
    public void testCreateUploader() {
        String uploaderName = "TestUploader";
        String result = admin.createUploader(uploaderName);
        assertNotNull(admin.findUploaderByName(uploaderName));
        assertEquals("Uploader created successfully: TestUploader", result);
    }


    @Test
    public void testUploadMedia() {
        admin.createUploader("TestUploader");
        Uploader uploader = admin.findUploaderByName("TestUploader");

        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.Music);

        MediaContentImpl media = new AudioImpl(uploader, tags, 500, new BigDecimal("15.99"), 300, 44100, "testAddress", "testTitle");

        assertDoesNotThrow(() -> admin.uploadMedia(media, uploader.getName()));

        MediaContentImpl foundMedia = admin.findMediaByAddress("testAddress");
        assertNotNull(foundMedia);
        assertEquals("testAddress", foundMedia.getAddress());
        assertEquals("testTitle", foundMedia.getTitle());
        assertEquals("TestUploader", foundMedia.getUploader().getName());
        assertEquals(tags, foundMedia.getTags());
    }

    @Test
    public void testFindUploaderByName() {
        String uploaderName = "TestUploader";
        admin.createUploader(uploaderName);
        Uploader uploader = admin.findUploaderByName(uploaderName);
        assertNotNull(uploader);
        assertEquals(uploaderName, uploader.getName());
    }

    @Test
    public void testGetAllProducersWithMediaCount() {
        admin.createUploader("Uploader1");
        admin.createUploader("Uploader2");
        Uploader uploader1 = admin.findUploaderByName("Uploader1");
        Uploader uploader2 = admin.findUploaderByName("Uploader2");

        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.Music);
        MediaContentImpl media1 = new AudioImpl(uploader1, tags, 500, new BigDecimal("15.99"), 300, 44100, "address1", "title1");
        MediaContentImpl media2 = new VideoImpl(uploader2, tags, 700, new BigDecimal("20.00"), 400, 1080, "address2", "title2");

        admin.uploadMedia(media1, "Uploader1");
        admin.uploadMedia(media2, "Uploader2");

        assertEquals(1, admin.getAllProducersWithMediaCount().get("Uploader1"));
        assertEquals(1, admin.getAllProducersWithMediaCount().get("Uploader2"));
    }

    @Test
    public void testGetAllTagsAssigned() {
        Uploader uploader = new UploaderImpl("TestUploader");
        admin.createUploader("TestUploader");

        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.Music);
        MediaContentImpl media = new AudioImpl(uploader, tags, 500, new BigDecimal("15.99"), 300, 44100, "testAddress", "testTitle");

        admin.uploadMedia(media, "TestUploader");

        Set<Tag> assignedTags = admin.getAllTags(true);
        assertTrue(assignedTags.contains(Tag.Music));
    }

    @Test
    public void testDeleteUploader() {
        admin.createUploader("UploaderToDelete");
        assertNotNull(admin.findUploaderByName("UploaderToDelete"));

        String result = admin.deleteUploader("UploaderToDelete");
        assertNull(admin.findUploaderByName("UploaderToDelete"));
        assertEquals("Uploader deleted: UploaderToDelete", result);
    }

    @Test
    public void testSaveStateJOS() throws IOException, ClassNotFoundException {
        admin.createUploader("Uploader1");
        admin.createUploader("Uploader2");
        File file = File.createTempFile("testState", ".jos");
        file.deleteOnExit();

        admin.saveStateJOS(file.getAbsolutePath());

        Admin loadedAdmin = Admin.loadStateJOS(file.getAbsolutePath());
        assertNotNull(loadedAdmin.findUploaderByName("Uploader1"));
        assertNotNull(loadedAdmin.findUploaderByName("Uploader2"));
    }

    @Test
    public void testLoadStateJOS() throws IOException, ClassNotFoundException {
        File file = File.createTempFile("testState", ".jos");
        file.deleteOnExit();

        admin.createUploader("Uploader1");
        admin.saveStateJOS(file.getAbsolutePath());

        Admin loadedAdmin = Admin.loadStateJOS(file.getAbsolutePath());
        assertNotNull(loadedAdmin.findUploaderByName("Uploader1"));
    }

    @Test
    public void testSaveStateJBP() throws IOException {
        admin.createUploader("Uploader1");
        admin.createUploader("Uploader2");
        File file = File.createTempFile("testState", ".jbp");
        file.deleteOnExit();

        admin.saveStateJBP(file.getAbsolutePath());

        Admin loadedAdmin = Admin.loadStateJBP(file.getAbsolutePath());
        assertNotNull(loadedAdmin.findUploaderByName("Uploader1"));
        assertNotNull(loadedAdmin.findUploaderByName("Uploader2"));
    }




    @Test
    public void testLoadStateJBP() throws IOException {
        File file = File.createTempFile("testState", ".jbp");
        file.deleteOnExit();

        admin.createUploader("Uploader1");
        admin.saveStateJBP(file.getAbsolutePath());

        Admin loadedAdmin = Admin.loadStateJBP(file.getAbsolutePath());
        assertNotNull(loadedAdmin.findUploaderByName("Uploader1"));
    }


}