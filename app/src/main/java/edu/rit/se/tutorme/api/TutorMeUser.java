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
    private JSONObject userJson;
    private String name;
    private String email;


    /**
     * Default constructor.
     *
     * @param userData
     */
    public TutorMeUser(JSONObject userData) {
        this.userJson = userData;

        try {
            this.userType = UserType.toUserType(userData.get("type").toString());
            this.name = (String) userData.get("name");
            this.email = (String) userData.get("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TutorMeUser(Parcel in) {
        JSONObject userData = (JSONObject) in.readValue(TutorMeUser.class.getClassLoader());
        this.userJson = userData;

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


    public JSONObject getJSON() {
        return userJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.userJson);
    }

    public static final Parcelable.Creator<TutorMeUser> CREATOR = new Parcelable.Creator<TutorMeUser>() {
        public TutorMeUser createFromParcel(Parcel in) {
            return new TutorMeUser(in);
        }

        public TutorMeUser[] newArray(int size) {
            return new TutorMeUser[size];
        }
    };


}
