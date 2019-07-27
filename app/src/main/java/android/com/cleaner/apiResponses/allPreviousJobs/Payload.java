package android.com.cleaner.apiResponses.allPreviousJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("provider_id")
    @Expose
    private Integer providerId;

    @SerializedName("Provider_profile")
    @Expose
    private String providerProfile;

    @SerializedName("job_id")
    @Expose
    private Integer jobId;

    @SerializedName("Services_names")
    @Expose
    private String servicesNames;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("AverageRating")
    @Expose
    private Float averageRating;


    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getProviderProfile() {
        return providerProfile;
    }

    public void setProviderProfile(String providerProfile) {
        this.providerProfile = providerProfile;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getServicesNames() {
        return servicesNames;
    }

    public void setServicesNames(String servicesNames) {
        this.servicesNames = servicesNames;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

}
