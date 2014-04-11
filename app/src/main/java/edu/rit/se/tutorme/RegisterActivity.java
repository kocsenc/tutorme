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

import edu.rit.se.tutorme.api.TutorMeUser;
import edu.rit.se.tutorme.api.UserType;
import edu.rit.se.tutorme.student.StudentHomeActivity;

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
        } else {
            showProgress(true);
            String userName = mFNameView.toString() + mLNameView.toString();
            String userZip = mZipCodeView.toString();
            String userEmail = mEmailView.toString();
            String userPassword = mPasswordView.toString();
            UserType userType = getRadioButton();
            mRegTask = new UserRegisterTask(userName, userZip, userEmail, userPassword, userType);
            mRegTask.execute((Void) null);
        }


    }

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

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        UserRegisterTask(String name, String zip, String email, String password, UserType user) {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            TutorMeUser newUser = new TutorMeUser();
            this.newUser = newUser;

            if (newUser == null) {
                return false;
            } else {
                return true;
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
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }


    }

    private void onSuccessRegister(TutorMeUser loginUser) {
        UserType type = loginUser.getUserType();
        Intent intent;
        if (type.equals(UserType.Tutor)) {
            intent = new Intent(this, TutorProfileActivity.class);
            intent.putExtra("userName", loginUser.getName());
            intent.putExtra("userEmail", loginUser.getEmail());
            intent.putExtra("userType", loginUser.getUserType());
        } else {
            intent = new Intent(this, StudentHomeActivity.class);
            intent.putExtra("userName", loginUser.getName());
            intent.putExtra("userEmail", loginUser.getEmail());
            intent.putExtra("userType", loginUser.getUserType());
        }
        startActivity(intent);
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

    private int getRadioButton() {
        int radioId = ((RadioGroup) findViewById(R.id.RadioGroup)).getCheckedRadioButtonId();
        
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
