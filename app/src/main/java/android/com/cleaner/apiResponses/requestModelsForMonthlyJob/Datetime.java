package android.com.cleaner.apiResponses.requestModelsForMonthlyJob;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datetime {

    @SerializedName("date")
    @Expose
    private Integer date;

    @SerializedName("time")
    @Expose
    private String time;


    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}