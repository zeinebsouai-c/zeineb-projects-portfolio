package contract;

import java.math.BigDecimal;
import java.time.Duration;

public interface Uploadable {
    Uploader getUploader();
    Duration getAvailability();
    BigDecimal getCost();
}
