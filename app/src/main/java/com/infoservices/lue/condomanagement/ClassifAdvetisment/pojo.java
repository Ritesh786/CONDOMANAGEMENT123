package com.infoservices.lue.condomanagement.ClassifAdvetisment;

import java.io.Serializable;

/**
 * Created by lue on 06-06-2017.
 */

public class pojo  implements Serializable {

    private String title, thumbnailUrl;
    private String year;
    private String rating;
    private String  genre;
    String image2;

    public pojo() {
    }

    public pojo(String name, String thumbnailUrl, String year, String rating,String image2,
                 String genre) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.image2 = image2;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }
}