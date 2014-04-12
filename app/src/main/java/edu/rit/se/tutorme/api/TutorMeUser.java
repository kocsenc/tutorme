package edu.rit.se.tutorme.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Model of the TutorMeUser.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class TutorMeUser {

    private JSONObject rawData;
    private UserType userType;
    private String name;
    private String email;
    private String postal;
    private TutorMeProfile profile;

    /**
     * Default constructor.
     *
     * @param rawData Raw user data received from server.
     */
    public TutorMeUser(JSONObject rawData) {
        this.rawData = rawData;
    }

    /**
     * Overloaded constructor for the TutorMeUser class.
     *
     * @param userType type of user
     * @param name name of user
     * @param email email of user
     * @param postal postal code of user
     */
    public TutorMeUser(UserType userType,
                       String name,
                       String email,
                       String postal) {
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.postal = postal;
        this.profile = new TutorMeProfile();
    }

    /**
     * Method to load data into the object
     * if object was constructed using the
     * JSONObject constructor.
     */
    public void load() {
        try {
            this.userType = UserType.toUserType(this.rawData.getString("type"));
            this.name = this.rawData.getString("name");
            this.email = this.rawData.getString("email");
            this.postal = this.rawData.getString("postal");

            if (this.userType == UserType.Tutor) {
                this.profile = new TutorMeProfile(this.rawData.getJSONObject("profile"));
                this.profile.load();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for userType.
     * @return the user's type
     */
    public UserType getUserType() {
        return this.userType;
    }

    /**
     * Getter for name.
     *
     * @return the user's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for email.
     * @return the user's email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Getter for postal address.
     * @return the user's postal address.
     */
    public String getPostal() {
        return this.postal;
    }

    /**
     * Getter for tutor profile.
     * @return the user's profile.
     */
    public TutorMeProfile getProfile() {
        return this.profile;
    }

    /**
     * Generate JSON object representation of the object.
     * @return JSON representation of the object
     */
    public JSONObject getJSON() {
        JSONObject rawData = new JSONObject();

        try {
            rawData.put("type", this.userType.toString());
            rawData.put("name", this.name);
            rawData.put("email", this.email);
            rawData.put("postal", this.postal);

            return rawData;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * toString method for the TutorMeUser class.
     *
     * @return string representation of the object
     */
    public String toString() {
        return this.name;
    }
}
