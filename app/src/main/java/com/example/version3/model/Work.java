package com.example.version3.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "work")
public class Work implements Parcelable {
    private String title;
    private String description;
    private String category;
    long timeInMillis;

    @PrimaryKey(autoGenerate = true)
    private int id;


    public Work(String title, String description, String category, long timeInMillis) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.timeInMillis = timeInMillis;
    }



    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Work(Parcel in) {
        title = in.readString();
        description = in.readString();
        category = in.readString();
        timeInMillis = in.readLong();
    }

    public static final Creator<Work> CREATOR = new Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel in) {
            return new Work(in);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeLong(timeInMillis);
    }


}