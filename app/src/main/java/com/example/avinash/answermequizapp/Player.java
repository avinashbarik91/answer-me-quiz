package com.example.avinash.answermequizapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Avinash Barik on 31/10/2017.
 */

public class Player implements Parcelable{
    private String name;
    private int topScore;
    private int lastScore;

    public Player(String name, int topScore, int lastScore) {
        this.name = name;
        this.topScore = topScore;
        this.lastScore = lastScore;
    }

    public Player()
    {}

    protected Player(Parcel in) {
        name = in.readString();
        topScore = in.readInt();
        lastScore = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getLastScore() {
        return lastScore;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }

    @Override
    public String toString() {
        return name + "\n"+"Top Score :"+topScore+" \n Last Score :"+lastScore;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(topScore);
        dest.writeInt(lastScore);
    }

}
