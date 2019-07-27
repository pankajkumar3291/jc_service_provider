package android.com.cleaner.apiResponses.previousJobsDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("provider_id")
    @Expose
    private Integer providerId;

    @SerializedName("Provider_name")
    @Expose
    private String providerName;

    @SerializedName("Services_names")
    @Expose
    private String servicesNames;

    @SerializedName("lastappointment")
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

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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
