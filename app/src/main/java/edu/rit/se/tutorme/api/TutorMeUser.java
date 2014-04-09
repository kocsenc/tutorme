package edu.rit.se.tutorme.api;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class TutorMeUser implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(new String[] {this.name, this.email});
    }
}
