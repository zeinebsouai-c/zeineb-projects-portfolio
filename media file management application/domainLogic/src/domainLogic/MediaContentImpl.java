package domainLogic;

import contract.MediaContent;
import contract.Tag;
import contract.Uploader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.*;

public abstract class MediaContentImpl implements MediaContent, Serializable {
    private String address;
    private int duration;
    private long accessCount;
    private Set<Tag> tags;
    private Uploader uploader;
    private BigDecimal cost;
    private int size;
    private LocalDate uploadDate;
    private String title;
    private String type;
    private static final long serialVersionUID = 1L;



    public MediaContentImpl(Uploader uploader, Set<Tag> tags , int size, BigDecimal cost,  int duration,  String address, String title){
        this.address = address;
        this.duration = duration;
        this.uploader = uploader;
        this.cost = cost;
        this.tags = new HashSet<>();
        this.size = size;
        this.accessCount = 0;
        this.uploadDate = LocalDate.now();
        this.title = title;
        this.tags = tags != null ? tags : new HashSet<>();
    }



    @Override
    public String getAddress(){
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public long getAccessCount(){
        return accessCount;
    }

    @Override
    public Collection<Tag> getTags(){
        if (tags == null) {
            tags = new HashSet<>();
        }
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

    public void incrementAccessCount() {
        this.accessCount++;
    }

    @Override
    public int getDuration(){
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public Uploader getUploader(){
        return uploader;
    }

    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    @Override
    public BigDecimal getCost(){
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    // Method to calculate and store availability (days since upload)
    public long getAvailabilityInDays() {
        // Calculate availability in days and store it in a variable
        LocalDate currentDate = LocalDate.now();
        return Duration.between(uploadDate.atStartOfDay(), currentDate.atStartOfDay()).toDays();

    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize(){
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
