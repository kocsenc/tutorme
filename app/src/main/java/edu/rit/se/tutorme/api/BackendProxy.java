package edu.rit.se.tutorme.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.rit.se.tutorme.api.exceptions.APIResponseException;
import edu.rit.se.tutorme.api.exceptions.AuthenticationException;
import edu.rit.se.tutorme.api.exceptions.InvalidParametersException;
import edu.rit.se.tutorme.api.exceptions.TokenMismatchException;

/**
 * Concrete implementation of the BackendInterface.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class BackendProxy implements BackendInterface {

    /**
     * Limited resource which will be contacting the API server.
     */
    private static HttpRequest httpRequest;

    /**
     * Email of the logged in user.
     */
    private static String email;

    /**
     * Authentication token that is passed with (almost) every request.
     */
    private static String token = "";

    /**
     * URL of the API server.
     */
    private final String uri = "http://zbox.student.rit.edu:3000";

    /**
     * Default constructor for the BackendProxy class.
     */
    public BackendProxy() {
        if (BackendProxy.httpRequest == null) {
            BackendProxy.httpRequest = new HttpRequest();
        }
    }

    /**
     * Optional constructor used to populate the internal token.
     *
     * @param token Authentication token to be used internally.
     */
    public BackendProxy(String token) {
        if (BackendProxy.httpRequest == null) {
            BackendProxy.httpRequest = new HttpRequest();
        }

        BackendProxy.token = token;
    }

    /**
     * Method to register a new user.
     *
     * @param user User object to be registered.
     * @return true if successful, false otherwise.
     */
    @Override
    public boolean register(TutorMeUser user, String password) throws APIResponseException {
        try {
            JSONObject registerRequestBody = user.getJSON();
            registerRequestBody.put("password", password);

            String rawResponse = httpRequest.sendJSONPost(
                    this.uri + "/users/register", registerRequestBody);
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                return true;
            } else {
                throw new APIResponseException((String) response.get("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Method to destroy a user's current session.
     *
     * @return true if successful, false otherwise.
     */
    @Override
    public TutorMeUser login(String email, String password) throws AuthenticationException {
        try {
            JSONObject loginRequestBody = new JSONObject();

            loginRequestBody.put("email", email);
            loginRequestBody.put("password", password);

            String rawResponse = httpRequest.sendJSONPost(this.uri + "/users/login", loginRequestBody);
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                // Store the authentication pair
                BackendProxy.email = email;
                BackendProxy.token = response.getString("token");

                TutorMeUser loginUser = new TutorMeUser(response.getJSONObject("user"));
                loginUser.load();

                return loginUser;
            } else {
                throw new AuthenticationException((String) response.get("message"));
            }
        } catch (JSONException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    /**
     * Method to destroy a user's current session.
     *
     * @return true if successful, false otherwise.
     */
    @Override
    public boolean logout() throws APIResponseException {
        try {
            JSONObject logoutRequestBody = new JSONObject();

            logoutRequestBody.put("email", BackendProxy.email);
            logoutRequestBody.put("token", BackendProxy.token);

            String rawResponse = httpRequest.sendJSONPost(this.uri + "/users/logout", logoutRequestBody);
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                BackendProxy.email = "";
                BackendProxy.token = "";

                return true;
            } else {
                throw new TokenMismatchException((String) response.get("message"));
            }
        } catch (JSONException e) {
            throw new APIResponseException(e.getMessage());
        }
    }

    /**
     * Method to update a user's information.
     *
     * @param user User object with updated information.
     * @return true if successful, false otherwise.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     *                                server side.
     */
    @Override
    public boolean updateUser(TutorMeUser user) throws TokenMismatchException {
        JSONObject userRequestBody = new JSONObject();

        try {
            userRequestBody.put("email", BackendProxy.email);
            userRequestBody.put("token", BackendProxy.token);

            String rawResponse = httpRequest.sendJSONPost(this.uri + "/users/update", userRequestBody);
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                return true;
            } else {
                if (response.get("message").equals("token mismatch")) {
                    throw new TokenMismatchException(BackendProxy.token);
                }

                return false;
            }
        } catch (JSONException e) {
            throw new TokenMismatchException(BackendProxy.token);
        }
    }

    /**
     * Method to remove a user's account.
     *
     * @param user User to be removed.
     * @return true if successful, false otherwise.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     *                                server side.
     */
    @Override
    public boolean deleteUser(TutorMeUser user) throws TokenMismatchException {
        return false;
    }

    /**
     * Method to get the list of newest messages.
     *
     * @return List of new TutorMeMessages.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     *                                server side.
     */
    @Override
    public ArrayList<TutorMeMessage> getMessages() throws TokenMismatchException {
        return null;
    }

    /**
     * Method to send a message to a user.
     *
     * @param message Message to be sent
     * @return true if successful, false otherwise.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     *                                server side.
     */
    @Override
    public boolean sendMessage(TutorMeMessage message) throws TokenMismatchException {
        return false;
    }

    /**
     * Method to fetch a tutor profile.
     *
     * @param email Email of the profile to be get.
     * @return TutorMeProfile object received from server.
     * @throws TokenMismatchException     Thrown if internal token state causes a token mismatch
     *                                    server side.
     * @throws InvalidParametersException Thrown if no such profile (or user is a student) exists.
     */
    @Override
    public TutorMeProfile getProfile(String email) throws InvalidParametersException, TokenMismatchException {
        try {
            JSONObject getProfileRequestBody = new JSONObject();

            getProfileRequestBody.put("email", BackendProxy.email);
            getProfileRequestBody.put("token", BackendProxy.token);

            String rawResponse = httpRequest.sendPost(this.uri + "/profiles/" + email,
                    getProfileRequestBody.toString());
            JSONObject response = new JSONObject(rawResponse);

            if (response.get("status").equals("success")) {
                return new TutorMeProfile((JSONObject) response.get("profile"));
            } else {
                if (response.get("message").equals("user is not a tutor")) {
                    throw new InvalidParametersException(response.getString("message"));
                } else {
                    throw new TokenMismatchException(response.getString("message"));
                }
            }
        } catch (JSONException e) {
            throw new InvalidParametersException(e.getMessage());
        }
    }

    /**
     * Method to search the system for tutors.
     *
     * @param query The search query to be executed.
     * @return List of TutorMeUser results.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     *                                server side.
     */
    @Override
    public ArrayList<TutorMeUser> search(String query) throws TokenMismatchException {
        try {
            JSONObject searchRequestBody = new JSONObject();

            searchRequestBody.put("email", BackendProxy.email);
            searchRequestBody.put("token", BackendProxy.token);
            searchRequestBody.put("query", query);

            String rawResponse = httpRequest.sendJSONPost(this.uri + "/profiles/search", searchRequestBody);
            JSONObject response = new JSONObject(rawResponse);
            ArrayList<TutorMeUser> results = new ArrayList<>();

            if (response.get("status").equals("success")) {

                JSONArray array = response.getJSONArray("results");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    TutorMeUser user = new TutorMeUser(object);
                    user.load();

                    results.add(user);
                }
            } else {
                throw new TokenMismatchException((String) response.get("message"));
            }

            return results;
        } catch (JSONException e) {
            return null;
        }
    }
}
