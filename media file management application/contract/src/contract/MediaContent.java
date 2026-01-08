package contract;

import java.math.BigDecimal;
import java.util.Collection;

public interface MediaContent {
    String getAddress();
    Collection<Tag> getTags();
    int getDuration();
    long getAccessCount();
    int getSize();

    Uploader getUploader();
    String getTitle();
    BigDecimal getCost();
}
