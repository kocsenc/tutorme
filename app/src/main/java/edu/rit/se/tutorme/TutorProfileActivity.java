package edu.rit.se.tutorme;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class TutorProfileActivity extends Activity {
    String tutorName;
    String tutorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        //Set bioField to be uneditable
        EditText bioField = (EditText) findViewById(R.id.BioField);
        bioField.setEnabled(false);
        bioField.setTextIsSelectable(false);

        //Hide editGradeButton, give it functionality
        Button gradeButton = (Button) findViewById(R.id.editGradeButton);
        //gradeButton.setVisibility(View.GONE);
        gradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGrades(findViewById(R.id.editGradeButton));
            }
        });


        //Hide save button, give it functionality
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setVisibility(View.GONE);


        //Give edit button functionality
        Button editButton = (Button) findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile(findViewById(R.id.edit_button));
            }
        });

        Intent myIntent = getIntent();
        Bundle userInfo = myIntent.getExtras();
        setupUserInfo(userInfo);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor_profile, menu);
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

    /**
     * Method call for when user clicks on the 'edit' button.
     *
     * @param v the View (button) that was clicked
     */
    public void editProfile(View v) {
        //Create map of all old info
        final Map tutorInfo = getTutorInfo();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);

        //Create animations
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        final Animation revTranslate = AnimationUtils.loadAnimation(this, R.anim.reverse_translate);

        //Enable Bio EditText, change color
        EditText bioField = (EditText) findViewById(R.id.BioField);
        bioField.clearFocus();
        bioField.setTextIsSelectable(true);
        bioField.setEnabled(true);
        bioField.setBackgroundResource(android.R.color.black);
        imm.showSoftInput(bioField, 0);


        //Hide edit button
        v.startAnimation(animTranslate);
        v.setVisibility(View.GONE);

        //Show save button
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDialogue(tutorInfo);
            }
        });
        saveButton.startAnimation(revTranslate);
        saveButton.setVisibility(View.VISIBLE);
    }

    /**
     * Method call for when user clicks on the 'save' button.
     *
     * @param tutorInfo a map of all the old information on screen
     */
    public void saveDialogue(final Map tutorInfo) {
        //Creates the alert dialogue
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Setting Dialog Title
        alertDialog.setTitle("Save Changes");

        //Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to save these changes?");

        //Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_tutorme);

        //YES
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Create animations
                final Animation animrevTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reverse_translate);
                final Animation animTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);

                //Animate save button, hide it
                Button saveButton = (Button) findViewById(R.id.save_button);
                saveButton.startAnimation(animTranslate);
                saveButton.setVisibility(View.GONE);

                //Animate edit button, show it
                Button editButton = (Button) findViewById(R.id.edit_button);
                editButton.startAnimation(animrevTranslate);
                editButton.setVisibility(View.VISIBLE);

                //Turn off the Bio EditText, change color
                EditText bioField = (EditText) findViewById(R.id.BioField);
                bioField.setTextIsSelectable(false);
                bioField.setEnabled(false);
                bioField.setBackgroundResource(android.R.color.transparent);

                //Perform update
                yesAction(getTutorInfo());

                //Show confirmation
                Toast.makeText(getApplicationContext(), "Changes saved",
                        Toast.LENGTH_SHORT).show();

            }
        });

        //NO
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Create animations
                final Animation animrevTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reverse_translate);
                final Animation animTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);

                //Animate save button, hide it
                Button saveButton = (Button) findViewById(R.id.save_button);
                saveButton.startAnimation(animTranslate);
                saveButton.setVisibility(View.GONE);

                //Animate edit button, show it
                Button editButton = (Button) findViewById(R.id.edit_button);
                editButton.startAnimation(animrevTranslate);
                editButton.setVisibility(View.VISIBLE);

                //Turn off the Bio EditText, change color
                EditText bioField = (EditText) findViewById(R.id.BioField);
                bioField.setTextIsSelectable(false);
                bioField.setEnabled(false);
                bioField.setBackgroundResource(android.R.color.transparent);

                //Perform rollback
                noAction(tutorInfo);

                //Show confirmation
                Toast.makeText(getApplicationContext(), "Changes discarded", Toast.LENGTH_SHORT).show();

            }
        });

        //Cancel
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //Showing Alert Message
        alertDialog.show();
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

        // Getting fields and setting test
        TextView nameField = (TextView) findViewById(R.id.UserNameField);
        nameField.setText(name);
    }

    //TODO: make popup work
    public void editGrades(View v) {
        PopupWindow PW = new PopupWindow(100, 100);
        PW.showAtLocation(v, 10, 1, 1);
    }

    /**
     * Method call for when user clicks 'no' on save popup.
     * Replaces any edits with old information
     *
     * @param oldInfo a map of all the old information on the screen
     */
    public void noAction(Map oldInfo) {
        EditText t = (EditText) findViewById(R.id.BioField);
        t.setText((CharSequence) oldInfo.get(1));
    }

    /**
     * Method call for when the user clicks 'yes' on the save popup.
     * Keeps any changes the user has made.
     *
     * @param newInfo a map of all the new information on the screen
     */
    public void yesAction(Map newInfo) {
        EditText t = (EditText) findViewById(R.id.BioField);
        t.setText((CharSequence) newInfo.get(1));

    }

    /**
     * Grabs all of the current information entered on screen
     *
     * @return info a dictionary of all the info taken off screen
     */
    public Map getTutorInfo() {
        Map<Integer, String> info = new HashMap<Integer, String>();
        EditText t = (EditText) findViewById(R.id.BioField);
        info.put(1, t.getText().toString());
        return info;
    }

    /**
     * Pulls up the add subject window
     *
     * @param v the button pressed
     */
    public void addSubject(final View v) {
        final ArrayList<String> subjectList = new ArrayList<String>();

        //Creates the alert dialogue
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alertDialog.setView(input);

        //Setting Dialog Title
        alertDialog.setTitle("Add subject");

        //Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_tutorme);

        //Add skills visually, and pass to the server
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String skill = input.getText().toString();
                Button newSkill = new Button(findViewById(R.id.subjectList).getContext());
                subjectList.add(skill);
                newSkill.setText(skill);
                LinearLayout layout = (LinearLayout) findViewById(R.id.subjectList);
                layout.removeView(v);
                layout.addView(newSkill);
                layout.addView(v);

                //TODO: Push arraylist to server

            }
        });

        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialog.show();


    }

}
