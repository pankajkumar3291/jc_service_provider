package android.com.cleaner.models;

public class OfferedServices {

    String title;
    String description;


    public OfferedServices(String tit, String desc) {


        this.title = tit;
        this.description = desc;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
