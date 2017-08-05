package com.example.rohan.miniproject.Feed;

/**
 * Created by Rohan on 31/03/17.
 */
public class FeedPOJO {

    private String Name;
    private String PhotoUrl;
    private String disconnect;

    public Long getMaxBPM() {
        return MaxBPM;
    }

    public void setMaxBPM(Long maxBPM) {
        MaxBPM = maxBPM;
    }

    public Long getEmergencyContact() {
        return EmergencyContact;
    }

    public void setEmergencyContact(Long emergencyContact) {
        EmergencyContact = emergencyContact;
    }

    private Long MaxBPM;
    private Long EmergencyContact;



    public String getDisconnect() {
        return disconnect;
    }

    public void setDisconnect(String disconnect) {
        this.disconnect = disconnect;
    }

    private FeedPOJO() {


    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoURL() {
        return PhotoUrl;
    }

    public void setPhotoURL(String photoURL) {
        PhotoUrl = photoURL;
    }
}
