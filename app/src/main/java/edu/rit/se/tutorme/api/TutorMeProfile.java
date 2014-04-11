package edu.rit.se.tutorme.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class that represents a tutor's profile.
 *
 * @author Craig Cabrey
 */
public class TutorMeProfile {

    private JSONObject rawData;
    private Double rate;
    private String description;
    private ArrayList<String> gradeLevels;
    private ArrayList<String> subjects;

    /**
     * Default constructor for the TutorMeProfile class.
     */
    public TutorMeProfile() {
        this.rate = 0.0;
        this.description = "";
        this.gradeLevels = new ArrayList<>();
        this.subjects = new ArrayList<>();
    }

    /**
     * Overloaded constructor for the TutorMeProfile class.
     *
     * @param rawData Raw data with which to build profile object.
     */
    public TutorMeProfile(JSONObject rawData) {
        this.rawData = rawData;
    }

    /**
     * Overloaded constructor for the TutorMeProfile class.
     *
     * @param rate
     * @param description
     * @param gradeLevels
     * @param subjects
     */
    public TutorMeProfile(Double rate,
                          String description,
                          ArrayList<String> gradeLevels,
                          ArrayList<String> subjects) {
        this.rate = rate;
        this.description = description;
        this.gradeLevels = gradeLevels;
        this.subjects = subjects;
    }

    public void load() {
        try {
            this.rate = this.rawData.getDouble("rate");
            this.description = this.rawData.getString("description");

            this.gradeLevels = new ArrayList<>();

            JSONArray profileItems = this.rawData.getJSONArray("profileItems");
            this.subjects = new ArrayList<>();
            for (int i = 0; i < profileItems.length(); i++) {
                String subject = profileItems.getString(i);
                this.subjects.add(subject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Double getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getGradeLevels() {
        return gradeLevels;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }
}
