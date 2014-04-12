package edu.rit.se.tutorme.student;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import edu.rit.se.tutorme.LoginActivity;
import edu.rit.se.tutorme.R;
import edu.rit.se.tutorme.SearchTutorsActivity;
import edu.rit.se.tutorme.TutorProfileActivity;
import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.BackendProxy;
import edu.rit.se.tutorme.api.exceptions.APIResponseException;


public class StudentHomeActivity extends Activity {

    private String studentName;
    private String studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        Intent intent = getIntent();
        Bundle userInfo = intent.getExtras();
        setupUserInfo(userInfo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_home, menu);
        getMenuInflater().inflate(R.menu.student_menu, menu);

        String pkg = "edu.rit.se.tutorme";
        String file = "edu.rit.se.tutorme.SearchTutorsActivity";
        ComponentName name = new ComponentName(pkg, file);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_action_bar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(name));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.search_action_bar:
//                Intent intent = new Intent(this, SearchTutorsActivity.class);
//                startActivity(intent);
                return true;
            case R.id.logout_action_bar:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method is in charge of setting up the view before showing it.
     * It will get the info from the intent bundle then set any local variables
     * and finally grab the text fields whose values to change and set them.
     */
    public void setupUserInfo(Bundle upUserInfo) {
        // Getting Info
        String name = upUserInfo.getString("userName");
        String email = upUserInfo.getString("userEmail");

        // Setting local Variables
        this.studentName = name;
        this.studentEmail = email;

        // Getting fields and setting test
        TextView nameField = (TextView) findViewById(R.id.StudentNameField);
        nameField.setText(name);
    }

    /**
     * Logs out of the session using the API
     */
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
}
