package edu.rit.se.tutorme.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
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
     * Optional constructor
     *
     * @param userType
     * @param name
     * @param email
     * @param postal
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
     * @return the userType
     */
    public UserType getUserType() {
        return this.userType;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPostal() {
        return this.postal;
    }

    public TutorMeProfile getProfile() {
        return this.profile;
    }

    public JSONObject getJSON() {
        return this.rawData;
    }
}
