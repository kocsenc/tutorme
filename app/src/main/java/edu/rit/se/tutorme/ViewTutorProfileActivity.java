package edu.rit.se.tutorme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.BackendProxy;
import edu.rit.se.tutorme.api.TutorMeProfile;
import edu.rit.se.tutorme.api.exceptions.APIResponseException;
import edu.rit.se.tutorme.api.exceptions.InvalidParametersException;
import edu.rit.se.tutorme.api.exceptions.TokenMismatchException;

public class ViewTutorProfileActivity extends Activity {

    String tutorName;
    String tutorEmail;
    TutorMeProfile tutorProfile;
    String bioInfo;
    ArrayList<String> subjectList;
    ArrayList<String> gradeList;
    ArrayList<String> dummySubjectList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tutor_profile);

        Intent myIntent = getIntent();
        Bundle userInfo = myIntent.getExtras();

        // Getting TutorMeProfile
        TutorMeProfileTask task = new TutorMeProfileTask(userInfo.getString("userEmail"));
        task.execute();
        // The task should assign this.tutorProfile

        setupUserInfo(userInfo);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.logout_action_bar) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * method called after asynchronous call to backend.
     *
     * @param tutorProfile the tutor profile object
     */
    public void setupUserProfile(TutorMeProfile tutorProfile) {

        //Grab views from screen
        LinearLayout layout = (LinearLayout) findViewById(R.id.subjectList);
        TextView bioField = (TextView) findViewById(R.id.BioField);

        //Set views appropriately
        if (tutorProfile != null) {
            if (tutorProfile.getSubjects() != null) {
                subjectList = tutorProfile.getSubjects();
            }

            if (tutorProfile.getDescription() != null) {
                bioField.setText(tutorProfile.getDescription());
            }

            TextView gradeField = (TextView) findViewById(R.id.GradeLevelField);
            if (tutorProfile.getGradeLevels() != null) {
                for (String s : tutorProfile.getGradeLevels()) {
                    gradeField.setText(s + ", ");
                }
            }

            if (subjectList != null) {
                for (String subject : subjectList) {
                    Button newSkill = new Button(findViewById(R.id.subjectList).getContext());
                    newSkill.setText(subject);
                    layout.addView(newSkill);
                }
            }
        }
    }

    /**
     * Method call to populate fields with correct information
     *
     * @param upUserInfo
     */
    public void setupUserInfo(Bundle upUserInfo) {
        // Getting Info
        String name = upUserInfo.getString("userName");
        String email = upUserInfo.getString("userEmail");


        // Setting local Variables
        this.tutorName = name;
        this.tutorEmail = email;

        TextView nameField = (TextView) findViewById(R.id.UserNameField);
        nameField.setText(name);
    }

    private void logout() {
        LogoutTask mAuthTask = new LogoutTask();
        mAuthTask.execute((Void) null);
    }

    private void goToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private class LogoutTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        /**
         * Do in background, will use api to try and logout
         */
        protected Boolean doInBackground(Void... voids) {
            BackendInterface api = new BackendProxy();
            try {
                return api.logout();
            } catch (APIResponseException e) {
                return false;
            }
        }

        @Override
        /**
         * After a api call
         */
        protected void onPostExecute(final Boolean success) {
            if (success) {
                goToLoginPage();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Error Logging Out",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class TutorMeProfileTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;

        TutorMeProfileTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BackendInterface api = new BackendProxy();

            try {
                try {
                    tutorProfile = api.getProfile(mEmail);
                } catch (TokenMismatchException e) {
                    e.printStackTrace();
                }
            } catch (InvalidParametersException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            setupUserProfile(tutorProfile);
        }
    }
}
