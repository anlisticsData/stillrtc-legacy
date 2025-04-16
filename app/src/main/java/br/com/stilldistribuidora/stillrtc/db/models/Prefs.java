package br.com.stilldistribuidora.stillrtc.db.models;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.stilldistribuidora.stillrtc.db.Constants;

/**
 * Created by Ack Lay (Cleidimar Viana) on 3/16/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class Prefs implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;

    @SerializedName("id")
    private int id;

    @SerializedName("time_loop_pictures")
    private int time_loop_pictures;

    @SerializedName("time_loop_points")
    private int time_loop_points;

    @SerializedName("creator_id")
    private int creator_id;

    @SerializedName("created_at")
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeLoopPictures() {
        return time_loop_pictures;
    }

    public void setTime_loop_pictures(int time_loop_pictures) {
        this.time_loop_pictures = time_loop_pictures;
    }

    public int getTimeLoopPoints() {
        return time_loop_points;
    }

    public void setTime_loop_points(int time_loop_points) {
        this.time_loop_points = time_loop_points;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("prefs")
        public ArrayList<Prefs> ar;
    }
}