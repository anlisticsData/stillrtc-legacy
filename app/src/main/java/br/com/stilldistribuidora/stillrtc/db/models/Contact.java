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

public class Contact implements Serializable {

    private static final long serialVersionUID = -2161110911377686463L;

    private int id;
    private int contactID;
    private String name;
    private String number;
    private String email;
    private String created_at;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("contacts")
        public ArrayList<Contact> ar;
    }

    public static String[] columns = new String[]{
            Constants.ID,
            Constants.CONTACT_ID,
            Constants.NAME,
            Constants.NUMBER,
            Constants.EMAIL,
            Constants.KEY_CREATED_AT,
            Constants.IMAGE
    };

    public static String script_db_tblContact = " CREATE TABLE " + Contact.class.getSimpleName() + "("
            + " " + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " " + Constants.CONTACT_ID + " INT, "
            + " " + Constants.NAME + " VARCHAR(512), "
            + " " + Constants.NUMBER + " VARCHAR(30) NULL, "
            + " " + Constants.EMAIL + " VARCHAR(512) NULL, "
            + " " + Constants.KEY_CREATED_AT + " DATETIME NULL, "
            + " " + Constants.IMAGE + " TEXT NULL)";

}