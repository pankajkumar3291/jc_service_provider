package android.com.cleaner.apiResponses.requestModelsForMonthlyJob;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthlyJobRequestModel {

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

    @SerializedName("datetime")
    @Expose
    private List<Datetime> datetime = null;

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

    public List<Datetime> getDatetime() {
        return datetime;
    }

    public void setDatetime(List<Datetime> datetime) {
        this.datetime = datetime;
    }

}