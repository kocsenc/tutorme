package edu.rit.se.tutorme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.BackendProxy;
import edu.rit.se.tutorme.api.TutorMeUser;
import edu.rit.se.tutorme.api.UserType;
import edu.rit.se.tutorme.api.exceptions.APIResponseException;

public class RegisterActivity extends Activity {

    private AutoCompleteTextView mEmailView;
    private EditText mLNameView;
    private EditText mFNameView;
    private EditText mZipCodeView;
    private EditText mPasswordRetype;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegisterView;
    private UserRegisterTask mRegTask = null;
    private RadioButton mStudentButton;
    private RadioButton mTeacherButton;

    /*
      Creates the screen on startup
     */
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
        mProgressView = findViewById(R.id.login_progress);
        mRegisterView = findViewById(R.id.register_form);
        mStudentButton = (RadioButton) findViewById(R.id.radioButtonStudent);
        mTeacherButton = (RadioButton) findViewById(R.id.radioButtonTeacher);
        ((RadioGroup) findViewById(R.id.RadioGroup)).check(R.id.radioButtonStudent);

        //Add action listener to button
        Button mEmailSignInButton = (Button) findViewById(R.id.register_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    /*
        Attempts to register a user
     */
    public void attemptRegister() {

        if (!validateInput()) {
            return;
        } else {
            showProgress(true);
            String userName = mFNameView.getText().toString().trim() + " " + mLNameView.getText().toString().trim();
            String userZip = mZipCodeView.getText().toString();
            String userEmail = mEmailView.getText().toString();
            String userPassword = mPasswordView.getText().toString();
            UserType userType = UserType.toUserType(getRadioButton());
            mRegTask = new UserRegisterTask(userName, userZip, userEmail, userPassword, userType);
            mRegTask.execute((Void) null);
        }


    }

    /*
        Creates a progress bar on the screen
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*
        Takes user back into login screen
     */
    private void onSuccessRegister(TutorMeUser registerUser) {
        UserType type = registerUser.getUserType();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /*
        Validates inputs so that craig's baby doesn't get destroyed
     */
    private boolean validateInput() {
        if (!isEmailValid(mEmailView.getText().toString())) {
            mEmailView.setError(getString(R.string.error_invalid_email));

            mEmailView.setText("");

            mEmailView.requestFocus();
            return false;
        }
        if (!isLastNameValid(mLNameView.getText().toString())) {
            mLNameView.setError(getString(R.string.error_no_name));

            mLNameView.setText("");

            mLNameView.requestFocus();
            return false;

        } else if (!isFirstNameValid(mFNameView.getText().toString())) {
            mFNameView.setError(getString(R.string.error_no_name));

            mFNameView.setText("");

            mFNameView.requestFocus();
            return false;

        } else if (!isPasswordValid(mPasswordView.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_invalid_password_register));

            mPasswordView.setText("");
            mPasswordRetype.setText("");

            mPasswordView.requestFocus();
            return false;

        } else if (!isPasswordCorrect(mPasswordView.getText().toString(), mPasswordRetype.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_password_match));

            mPasswordView.setText("");
            mPasswordRetype.setText("");

            mPasswordView.requestFocus();

            return false;
        } else {
            return true;
        }
    }

    /**
     * @return 0 if student, 1 if teacher.
     */
    private String getRadioButton() {
        int radioId = ((RadioGroup) findViewById(R.id.RadioGroup)).getCheckedRadioButtonId();
        switch (radioId) {
            case (R.id.radioButtonStudent):
                return "0";
            case (R.id.radioButtonTeacher):
                return "1";
            //Should never happen
            default:
                return "2";
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

    // checks th email validation
    private boolean isEmailValid(String email) {
        return email.contains("@");

    }

    // checks password length
    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    // checks password match
    private boolean isPasswordCorrect(String password, String passwordTwo) {
        return password.equals(passwordTwo);
    }

    // check for a last name
    private boolean isFirstNameValid(String fname) {
        return fname.length() > 0;
    }

    // check for a first name
    private boolean isLastNameValid(String lname) {
        return lname.length() > 0;
    }

    /*
        the async task to register a user
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
        TutorMeUser newUser;
        String mPassword;
        BackendInterface api;

        UserRegisterTask(String name, String zip, String email, String password, UserType user) {
            newUser = new TutorMeUser(user, name, email, zip);
            mPassword = password;
            api = new BackendProxy();


        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                api.register(newUser, mPassword);

                return true;
            } catch (APIResponseException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            showProgress(false);

            if (success) {
                onSuccessRegister(this.newUser);
                finish();
            } else {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }


    }
}
