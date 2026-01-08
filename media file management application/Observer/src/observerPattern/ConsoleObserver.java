package observerPattern;

import EventSystem.*;
import domainLogic.*;




public class ConsoleObserver implements EventListener {




    @Override
    public void handleEvent(EventType eventType, Object data) {
        switch (eventType) {

            case MEDIA_UPLOADED:
                MediaContentImpl media = (MediaContentImpl) data;
                System.out.println("New media uploaded: " + media);
                break;
            case MEDIA_DELETED:
                String address = (String) data;
                System.out.println("Media deleted at address: " + address);
                break;
            case UPLOADER_DELETED:
                String uploaderName = (String) data;
                System.out.println("Uploader deleted: " + uploaderName);
                break;
            case MEDIA_PLAYED:
                System.out.println("Media played: " + data );
                break;

            // add tags changed and deal with it in admin and adminCLI
            default:
                System.out.println("Unknown event: " + eventType);
                break;
        }
    }
}
