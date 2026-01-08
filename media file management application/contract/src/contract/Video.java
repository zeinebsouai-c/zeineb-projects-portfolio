package contract;

public interface Video extends MediaContent,Uploadable{
    void incrementAccessCount();

    int getResolution();
}
