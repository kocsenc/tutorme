package edu.rit.se.tutorme;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import edu.rit.se.tutorme.api.TutorMeAPI;
import edu.rit.se.tutorme.api.TutorMeUser;
import edu.rit.se.tutorme.api.UserType;

public class RegisterActivity extends Activity {

    private AutoCompleteTextView mEmailView;
    private EditText mLNameView;
    private EditText mFNameView;
    private EditText mZipCodeView;
    private EditText mPasswordRetype;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mZipCodeView = (EditText) findViewById(R.id.zipcode);
        mFNameView = (EditText) findViewById(R.id.prompt_fname);
        mLNameView = (EditText) findViewById(R.id.prompt_lname);
        mPasswordRetype = (EditText) findViewById(R.id.password_retype);
        mPasswordView = (EditText) findViewById(R.id.password);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });


    }

    public void attemptRegister() {

        if (!validateinput()) {
            return;
        }


    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        UserRegisterTask(String name, String zip, String email, String password, UserType user) {
            
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            TutorMeAPI api = new TutorMeAPI();
            TutorMeUser user = api.login(mEmail, mPassword);
            this.user = user;

            if (user == null) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                onSuccessLogin(this.user);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }

    private boolean validateinput() {
        if (isEmailValid(mEmailView.toString())) {
            mEmailView.setError(getString(R.string.error_invalid_email));

            mEmailView.setText("");

            mEmailView.requestFocus();
            return false;

        } else if (isLastNameValid(mLNameView.toString())) {
            mLNameView.setError(getString(R.string.error_no_name));

            mLNameView.setText("");

            mLNameView.requestFocus();
            return false;

        } else if (isFirstNameValid(mFNameView.toString())) {
            mFNameView.setError(getString(R.string.error_no_name));

            mFNameView.setText("");

            mFNameView.requestFocus();
            return false;

        } else if (isPasswordValid(mPasswordView.toString())) {
            mPasswordView.setError(getString(R.string.error_invalid_password_register));

            mPasswordView.setText("");
            mPasswordRetype.setText("");

            mPasswordView.requestFocus();
            return false;

        } else if (isPasswordCorrect(mPasswordView.toString(), mPasswordRetype.toString())) {
            mPasswordView.setError(getString(R.string.error_password_match));

            mPasswordView.setText("");
            mPasswordRetype.setText("");

            mPasswordView.requestFocus();

            return false;
        } else {
            return true;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
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
        return super.onOptionsItemSelected(item);
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    private boolean isPasswordCorrect(String password, String passwordTwo) {
        return password.equals(passwordTwo);
    }

    private boolean isFirstNameValid(String fname) {
        return fname.length() > 0;
    }

    private boolean isLastNameValid(String lname) {
        return lname.length() > 0;
    }
}
