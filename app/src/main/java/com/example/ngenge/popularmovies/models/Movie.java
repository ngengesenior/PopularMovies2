package com.example.ngenge.popularmovies.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.ngenge.popularmovies.utils.Utils;

@Entity(tableName = "movies")
public class Movie implements Parcelable{

    @PrimaryKey
    private int id;
    private String title;
    private String release_date;
    private double vote_average;
    private int vote_count;
    private String poster_path;
    private String overview;


    public Movie(@NonNull int id,@NonNull String title,@NonNull String release_date,@NonNull double
            vote_average,@NonNull int vote_count,@NonNull String poster_path,@NonNull String overview) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.poster_path = poster_path;
        this.overview = overview;
    }


    @Ignore
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        vote_count = in.readInt();
        poster_path = in.readString();
        overview = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = Utils.baseImageUrl+poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeDouble(vote_average);
        dest.writeInt(vote_count);
        dest.writeString(poster_path);
        dest.writeString(overview);
    }


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release_date='" + release_date + '\'' +
                ", vote_average=" + vote_average +
                ", vote_count=" + vote_count +
                ", poster_path='" + poster_path + '\'' +
                ", overview='" + overview + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object obj) {

        Movie movie  = (Movie) obj;
        if(this.overview.equals(movie.overview)
                && this.id == movie.id && this.title.equals(movie.title)
                && this.release_date.equals(movie.release_date)
                && this.vote_average == movie.vote_average
                && this.vote_count == movie.vote_count
                && this.poster_path.equals(movie.poster_path)
                )
        {
            return true;
        }
        else {
            return false;
        }
    }
}
