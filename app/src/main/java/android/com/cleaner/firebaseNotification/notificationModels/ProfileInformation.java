package android.com.cleaner.firebaseNotification.notificationModels;

public class ProfileInformation {


    private String providerName;
    private String providerProfileImage;
    private String providerBio;
    private int providerReviews;
    private String providerStartTime;
    private String providerEndTime;


    public ProfileInformation(String providerName, String providerProfileImage, String providerBio, String providerStartTime, String providerEndTime) {

        this.providerName = providerName;
        this.providerProfileImage = providerProfileImage;
        this.providerBio = providerBio;
        this.providerReviews = providerReviews;
        this.providerStartTime = providerStartTime;
        this.providerEndTime = providerEndTime;


    }


    public String getProviderStartTime() {
        return providerStartTime;
    }

    public void setProviderStartTime(String providerStartTime) {
        this.providerStartTime = providerStartTime;
    }

    public String getProviderEndTime() {
        return providerEndTime;
    }

    public void setProviderEndTime(String providerEndTime) {
        this.providerEndTime = providerEndTime;
    }


    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderProfileImage() {
        return providerProfileImage;
    }

    public void setProviderProfileImage(String providerProfileImage) {
        this.providerProfileImage = providerProfileImage;
    }


    public String getProviderBio() {
        return providerBio;
    }

    public void setProviderBio(String providerBio) {
        this.providerBio = providerBio;
    }

    public int getProviderReviews() {
        return providerReviews;
    }

    public void setProviderReviews(int providerReviews) {
        this.providerReviews = providerReviews;
    }


}
