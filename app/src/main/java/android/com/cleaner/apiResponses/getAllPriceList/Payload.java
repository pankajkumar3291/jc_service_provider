package android.com.cleaner.apiResponses.getAllPriceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("serviceprovider_id")
    @Expose
    private Integer serviceproviderId;

    @SerializedName("price")
    @Expose
    private Integer price;


    public Integer getServiceproviderId() {
        return serviceproviderId;
    }

    public void setServiceproviderId(Integer serviceproviderId) {
        this.serviceproviderId = serviceproviderId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}