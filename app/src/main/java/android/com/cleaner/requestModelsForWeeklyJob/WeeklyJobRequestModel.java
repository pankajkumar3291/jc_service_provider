package android.com.cleaner.requestModelsForWeeklyJob;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeeklyJobRequestModel {

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("service_ids")
    @Expose
    private String serviceIds;

    @SerializedName("DateTime")
    @Expose
    private List<DateTime> dateTime = null;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public List<DateTime> getDateTime() {
        return dateTime;
    }

    public void setDateTime(List<DateTime> dateTime) {
        this.dateTime = dateTime;
    }

}