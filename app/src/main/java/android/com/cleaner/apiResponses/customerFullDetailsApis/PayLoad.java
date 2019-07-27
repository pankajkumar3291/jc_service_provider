package android.com.cleaner.apiResponses.customerFullDetailsApis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayLoad {

    @SerializedName("UserDetails")
    @Expose
    private UserDetails userDetails;

    @SerializedName("Address")
    @Expose
    private String address;

    @SerializedName("State_id")
    @Expose
    private Integer stateId;

    @SerializedName("State")
    @Expose
    private String state;

    @SerializedName("City_id")
    @Expose
    private String cityId;

    @SerializedName("Cityname")
    @Expose
    private String cityname;

    @SerializedName("Zipcode_id")
    @Expose
    private String zipcodeId;

    @SerializedName("Zipcode")
    @Expose
    private String zipcode;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getZipcodeId() {
        return zipcodeId;
    }

    public void setZipcodeId(String zipcodeId) {
        this.zipcodeId = zipcodeId;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}