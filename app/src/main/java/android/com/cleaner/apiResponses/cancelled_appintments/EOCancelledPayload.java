package android.com.cleaner.apiResponses.cancelled_appintments;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class EOCancelledPayload {

    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("Provider_name")
    @Expose
    private String providerName;
    @SerializedName("Provider_profile")
    @Expose
    private String providerProfile;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("customer_address")
    @Expose
    private String customerAddress;
    @SerializedName("Services_names")
    @Expose
    private String servicesNames;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;

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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
