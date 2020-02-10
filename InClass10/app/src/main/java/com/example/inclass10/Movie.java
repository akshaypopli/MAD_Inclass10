package com.example.inclass10;

import java.io.Serializable;

public class Movie implements Serializable {
    String name;
    String description;
    String genre;
    int rating;
    int year;
    String imdb;

    public Movie(String name, String description, String genre, int rating, int year, String imdb) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.imdb = imdb;

    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                ", imdb='" + imdb + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public int getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

    public String getImdb() {
        return imdb;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

}
