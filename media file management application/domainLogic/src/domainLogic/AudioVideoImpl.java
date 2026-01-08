
package domainLogic;

import contract.AudioVideo;
import contract.Tag;
import contract.Uploader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.*;

public class AudioVideoImpl extends MediaContentImpl implements AudioVideo, Serializable {
    public String title;
    private int samplingRate;
    private int resolution;
    private int duration;
    private String address;
    private long accessCount;
    private Uploader uploader;
    private BigDecimal cost;
    private int size;
    private Set<Tag> tags;
    private static final long serialVersionUID = 1L;



    public AudioVideoImpl(Uploader uploader, Set<Tag> tags, int size, BigDecimal cost, int duration , int samplingRate, int resolution, String address ,String title) {
        super(uploader, tags, size, cost, duration, address, title);
        this.address = address;
        this.uploader = uploader;
        this.cost = cost;
        this.duration = duration;
        this.title = title;
        this.tags = tags;
        this.size = size;

        this.samplingRate = samplingRate;
        this.resolution = resolution;

        this.accessCount = 0;
        this.setType("audiovideo");
    }

    @Override
    public String getType(){
        return "audiovideo";
    }


    public int getSize(){
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    @Override
    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }


    @Override
    public int getResolution() {
        return resolution;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }



    @Override
    public void setAccessCount(long l) {
        this.accessCount = 0;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    @Override
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public Uploader getUploader() {
        return uploader;
    }

    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    @Override
    public Duration getAvailability() {
        return null;
    }

    @Override
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public void incrementAccessCount() {
        this.accessCount++;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioVideoImpl that = (AudioVideoImpl) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    public void setResolution(int avResolution) {
        this.resolution = resolution;
    }
}
