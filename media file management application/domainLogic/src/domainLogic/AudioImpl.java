
package domainLogic;

import contract.Audio;
import contract.Tag;
import contract.Uploader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.*;

public class AudioImpl extends MediaContentImpl implements Audio, Serializable {

    private static final long serialVersionUID = 1L;
    private long accessCount;
    private int samplingRate;
    private int size;
    private String address;
    private int duration;
    private Uploader uploader;
    private BigDecimal cost;
    private Set<Tag> tags;
    private String title;


    // constructor
    public AudioImpl(Uploader uploader, Set<Tag> tags, int size , BigDecimal cost, int duration,  int samplingRate, String address, String title) {
        super( uploader, tags ,size, cost, duration ,address,title);
        this.address = address;
        this.uploader = uploader;
        this.cost = cost;
        this.duration = duration;
        this.title = title;
        this.tags = tags;
        this.size = size;
        if (samplingRate <= 0) throw new IllegalArgumentException("Sampling rate must be positive");
        if (duration < 0) throw new IllegalArgumentException("Duration cannot be negative");
        if (address == null || address.isEmpty()) throw new IllegalArgumentException("Address cannot be empty");

        this.accessCount = 0;
        this.samplingRate = samplingRate;
        this.setType("audio");
    }

    @Override
    public String getType(){
        return "audio";
    }


    public int getSize(){
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }


    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public void setAccessCount(long l) {
        this.accessCount = l;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void incrementAccessCount() {
        this.accessCount++;
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
        AudioImpl audioImpl = (AudioImpl) o;
        return Objects.equals(address, audioImpl.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
