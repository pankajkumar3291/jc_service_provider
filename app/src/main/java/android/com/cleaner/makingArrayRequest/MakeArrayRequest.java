package android.com.cleaner.makingArrayRequest;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeArrayRequest {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("servicetypes")
    @Expose
    private List<Servicetype> servicetypes = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    public List<Servicetype> getServicetypes() {
        return servicetypes;
    }

    public void setServicetypes(List<Servicetype> servicetypes) {
        this.servicetypes = servicetypes;
    }


    public static class Servicetype {

        @SerializedName("id")
        @Expose
        private Integer id;


        public Servicetype(Integer id) {
            this.id = id;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }


    }
}