package android.com.cleaner.models;

public class CompletedAppointments {


    String title;
    String typesofJobs;
    String completedJobs;
    String date;

    public CompletedAppointments(String title, String typesofJobs, String completedJobs, String date) {

        this.title = title;
        this.typesofJobs = typesofJobs;
        this.completedJobs = completedJobs;
        this.date = date;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypesofJobs() {
        return typesofJobs;
    }

    public void setTypesofJobs(String typesofJobs) {
        this.typesofJobs = typesofJobs;
    }

    public String getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(String completedJobs) {
        this.completedJobs = completedJobs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
