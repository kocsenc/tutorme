package edu.rit.se.tutorme.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class TutorMeAPI {

    /**
     * Limited resource which will be contacting the API server.
     */
    private static HttpRequest httpRequest;

    /**
     * URL of the API server.
     */
    private final String url = "http://zbox.student.rit.edu:3000";

    /**
     * Default constructor.
     */
    public TutorMeAPI() {
        if (httpRequest == null) {
            httpRequest = new HttpRequest();
        }
    }

    /**
     * Login API call.
     * @param email of the user
     * @param password of the user
     * @return true if successful, false otherwise.
     */
    public TutorMeUser login(String email, String password) {
        JSONObject loginRequestBody = new JSONObject();

        try {
            loginRequestBody.put("email", email);
            loginRequestBody.put("password", password);

            String rawResponse = httpRequest.sendJSONPost(this.url + "/users/login", loginRequestBody);
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                return new TutorMeUser((JSONObject) response.get("user"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Logout API call.
     * @param email of the user.
     * @param token temporary authentication token.
     * @return true if successful, false otherwise.
     */
    public boolean logout(String email, String token) {
        JSONObject logoutRequestBody = new JSONObject();

        try {
            logoutRequestBody.put("email", email);
            logoutRequestBody.put("token", token);

            String rawResponse = httpRequest.sendJSONPost(this.url + "/users/logout", logoutRequestBody);
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Register API call.
     * @param type student or tutor
     * @param name of the new user
     * @param email of the new user
     * @param password of the new user
     * @param postal of the new user
     * @return true if successful, false otherwise.
     */
    public boolean register(UserType type, String name, String email, String password, String postal) {
        JSONObject registerRequestBody = new JSONObject();

        try {
            registerRequestBody.put("type", type.toString());
            registerRequestBody.put("name", name);
            registerRequestBody.put("email", email);
            registerRequestBody.put("password", password);
            registerRequestBody.put("postal", postal);

            String rawResponse = httpRequest.sendJSONPost(this.url + "/users/register", registerRequestBody);
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Search User API call
     *
     * @param query - the search criteria
     * @return - a list of users found
     */
    public ArrayList<TutorMeUser> searchUsers(String query) {
        JSONObject searchBody = new JSONObject();

        try {
            searchBody.put("query", query);

            String rawResponse = httpRequest.sendJSONPost(this.url + "/profiles/search", searchBody);
            JSONObject response = new JSONObject(rawResponse);
            ArrayList<TutorMeUser> results = new ArrayList<TutorMeUser>();

            if (response.get("status").equals("success")) {

                JSONArray array = response.getJSONArray("results");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    TutorMeUser user = new TutorMeUser(object);
                    results.add(user);
                }
            }

            //TODO Some sort of error message if no results found
            return results;

        } catch (JSONException e) {
            return null;
        }
    }
}
