package contract;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

public interface Audio extends MediaContent,Uploadable, Serializable{


    int getSamplingRate();
    long getAccessCount();
    void setAccessCount(long l);
    String getAddress();
    Collection<Tag> getTags();
    Uploader getUploader();
    Duration getAvailability();
    BigDecimal getCost();
}
