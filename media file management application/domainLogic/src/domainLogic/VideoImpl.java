
package domainLogic;

import contract.Video;
import contract.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.*;

public class VideoImpl extends MediaContentImpl implements Video, Serializable {


    private int resolution;
    private int duration;
    private String address;
    private long accessCount;
    private Uploader uploader;
    public int size;
    public BigDecimal cost;
    private Set<Tag> tags;
    private String title;
    private static final long serialVersionUID = 1L;


    public VideoImpl(Uploader uploader, Set<Tag> tags, int size , BigDecimal cost,  int duration , int resolution ,String address, String title){
        super(uploader, tags, size, cost, duration ,address, title);
        this.resolution = resolution;
        this.address = address;
        this.uploader = uploader;
        this.cost = cost;
        this.duration = duration;
        this.title = title;
        this.tags = tags;
        this.size = size;

        this.accessCount = 0;
        this.setType("video");
    }

    @Override
    public String getType(){
        return "video";
    }



    public int getSize(){
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String getAddress(){
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
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public void incrementAccessCount() {
        this.accessCount++;
    }

    @Override
    public int getResolution() {
        return resolution;
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
        VideoImpl videoImpl = (VideoImpl) o;
        return Objects.equals(address, videoImpl.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }


    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
}
