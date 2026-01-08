package domainLogic;
/* creating an Audio Data Transfer Object class to ensure encapsulation by only
exposing what is necessary for the transfer of data for JBP */


import java.math.BigDecimal;
import java.util.Set;

public class MediaContentDTO {
    private String title;
    private int duration;
    private long accessCount;
    private String address;
    private String uploaderName;
    private String type;
    private int samplingRate;
    private int resolution;
    private BigDecimal cost;
    private int size;
    private Set<String> tags;

    // Default constructor for JBP
    public MediaContentDTO() {
    }

    // Constructor for audio
    public MediaContentDTO(String uploaderName, Set<String> tags, int size, BigDecimal cost, int duration,
                           int samplingRate,String address, String title,
                            String type) {
        this.title = title;
        this.samplingRate = samplingRate;
        this.duration = duration;
        this.address = address;
        this.accessCount = accessCount;
        this.uploaderName = uploaderName;
        this.type = "audio";
        this.size = size;
        this.cost = cost;
        this.tags = tags;
    }

    // Constructor for video
    public MediaContentDTO(String uploaderName, Set<String> tags, int size, BigDecimal cost, int duration,
                           int resolution, String address, String title,
                           String type, boolean isVideo) {
        this.title = title;
        this.resolution = resolution;
        this.duration = duration;
        this.address = address;
        this.accessCount = accessCount;
        this.uploaderName = uploaderName;
        this.type = "video";
        this.size = size;
        this.cost = cost;
        this.tags = tags;

    }

    // Constructor for audioVideo
    public MediaContentDTO(String uploaderName, Set<String> tags, int size, BigDecimal cost, int duration,
                           int samplingRate, int resolution, String address, String title,
                           String type) {
        this.title = title;
        this.samplingRate = samplingRate;
        this.resolution = resolution;
        this.duration = duration;
        this.address = address;
        this.accessCount = accessCount;
        this.uploaderName = uploaderName;
        this.type = "audiovideo";
        this.size = size;
        this.cost = cost;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
