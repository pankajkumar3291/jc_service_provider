package android.com.cleaner.models;

public class Products {
    private int id;
    private String name;
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    private double rating;
    private double price;
    private int image;

    public Products(int id, String title, String shortdesc, double rating, double price, int image) {

        this.id = id;
        this.name = title;
        this.location = shortdesc;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }


}