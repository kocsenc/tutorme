package edu.rit.se.tutorme.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class TutorMeUser {

    private UserType userType;
    private String name;
    private String email;

    /**
     * Default constructor.
     * @param userData
     */
    public TutorMeUser(JSONObject userData) {
        try {
            this.userType = UserType.toUserType(userData.get("type").toString());
            this.name = (String) userData.get("name");
            this.email = (String) userData.get("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public UserType getUserType() {
        return userType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
