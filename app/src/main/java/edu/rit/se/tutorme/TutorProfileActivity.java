package edu.rit.se.tutorme;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.*;

import org.w3c.dom.Text;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.BackendProxy;
import edu.rit.se.tutorme.api.TutorMeProfile;
import edu.rit.se.tutorme.api.exceptions.InvalidParametersException;
import edu.rit.se.tutorme.api.exceptions.TokenMismatchException;


public class TutorProfileActivity extends Activity {
    String tutorName;
    String tutorEmail;
    TutorMeProfile tutorProfile;
    String bioInfo;
    ArrayList<String> subjectList;
    ArrayList<String> gradeList;
    ArrayList<String> dummySubjectList = new ArrayList<String>();
    ArrayList<Button> subjectButtons = new ArrayList<Button>();

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
        gradeButton.setVisibility(View.GONE);
        gradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editGrades(findViewById(R.id.editGradeButton));
            }
        });

        //Hide add Subject Button
        Button addSubjButton = (Button) findViewById(R.id.addSubjButton);
        addSubjButton.setVisibility(View.GONE);

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

        //Show add subject button
        Button addSubjButton = (Button) findViewById(R.id.addSubjButton);
        addSubjButton.setVisibility(View.VISIBLE);


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

        enableSkillButtons();
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
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

                //Hide add subject button
                Button addSubjButton = (Button) findViewById(R.id.addSubjButton);
                addSubjButton.setVisibility(View.GONE);

                //Perform update
                yesAction(getTutorInfo());

                //Show confirmation
                Toast.makeText(getApplicationContext(), "Changes saved",
                        Toast.LENGTH_SHORT).show();

            }
        });

        //NO
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

                //Hide add subject button
                Button addSubjButton = (Button) findViewById(R.id.addSubjButton);
                addSubjButton.setVisibility(View.GONE);

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
     * method called after asynchronous call to backend.
     *
     * @param tutorProfile the tutor profile object
     */
    public void setupUserProfile(TutorMeProfile tutorProfile) {

        //Grab views from screen
        LinearLayout layout = (LinearLayout) findViewById(R.id.subjectList);
        Button b = (Button) findViewById(R.id.addSubjButton);
        EditText bioField = (EditText) findViewById(R.id.BioField);

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


            // Getting fields and setting test
            layout.removeView(findViewById(R.id.addSubjButton));
            if (subjectList != null) {
                for (String subject : subjectList) {
                    Button newSkill = new Button(findViewById(R.id.subjectList).getContext());
                    newSkill.setText(subjectList.get(0));

                    //Add functionality to button
                    newSkill.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removeSkill(view);
                        }
                    });
                    subjectButtons.add(newSkill);
                    layout.addView(newSkill);
                    disableSkillButtons();
                }
            }
            //Have the add button stay at the bottom
            layout.addView(b);
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
        //Get the arraylists of old data
        ArrayList<String> arrayBio = (ArrayList<String>) oldInfo.get(1);
        ArrayList<String> arraySubjects = (ArrayList<String>) oldInfo.get(2);

        //Add the old subjects to the list
        LinearLayout subjects = (LinearLayout) findViewById(R.id.subjectList);
        Button addSubjButton = (Button) findViewById(R.id.addSubjButton);
        subjects.removeAllViews();
        if (arraySubjects != null) {
            for (String subject : arraySubjects) {
                Button newSkill = new Button(findViewById(R.id.subjectList).getContext());
                newSkill.setText(subject);
                LinearLayout layout = (LinearLayout) findViewById(R.id.subjectList);
                layout.addView(newSkill);
            }
        }
        subjects.addView(addSubjButton);

        //Rollback the bio
        EditText t = (EditText) findViewById(R.id.BioField);
        if (arrayBio != null) {
            t.setText((CharSequence) arrayBio.get(0));
        } else {
            t.setText("");
        }
    }

    /**
     * Method call for when the user clicks 'yes' on the save popup.
     * Keeps any changes the user has made.
     *
     * @param newInfo a map of all the new information on the screen
     */
    public void yesAction(Map newInfo) {
        //Get the arraylists of new info
        ArrayList<String> array = (ArrayList<String>) newInfo.get(1);

        //remove all undo buttons


        //Add the new subjects to the master subject list
        for (String subject : dummySubjectList) {
            subjectList.add(subject);
        }

        //Update the biofield
        EditText t = (EditText) findViewById(R.id.BioField);
        t.setText((CharSequence) array.get(0));

        removeUndoButtons();
        disableSkillButtons();

    }

    /**
     * Grabs all of the current information entered on screen
     *
     * @return info a dictionary of all the info taken off screen
     */
    public Map getTutorInfo() {
        //Create a map of arraylists to hold all data
        Map<Integer, ArrayList<String>> info = new HashMap<Integer, ArrayList<String>>();

        //Grab info from screen
        EditText t = (EditText) findViewById(R.id.BioField);
        ArrayList<String> bioField = new ArrayList<String>();
        bioField.add(t.getText().toString());

        //Put info in Map
        info.put(1, bioField);
        info.put(2, subjectList);

        return info;
    }

    /**
     * Pulls up the add subject window
     *
     * @param v the button pressed
     */
    public void addSubject(final View v) {
        //final ArrayList<String> dummySubjectList = new ArrayList<String>();

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
                dummySubjectList.add(skill);
                newSkill.setText(skill);
                //newSkill.setBackgroundColor(getResources().getColor(R.color.gray));


                //Add functionality to button
                newSkill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeSkill(view);
                    }
                });
                subjectButtons.add(newSkill);

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


    /**
     * Method which removes a subject from the UI and the backend
     */
    public void removeSkill(View view) {
        //Create animations
        final Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        final Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        //Grab the button
        Button clicked = (Button) view;

        String temp = null;
        //Remove skill from list
        for (String s : dummySubjectList) {
            if (s == clicked.getText()) {
                temp = s;
            }
        }

        dummySubjectList.remove(temp);

        //Get text color so it can be reset later
        final ColorStateList tc = clicked.getTextColors();
        final CharSequence theSkill = clicked.getText();

        //Change the button, fade in/out
        view.startAnimation(fadeOut);
        clicked.setText("Undo");
        clicked.setTextColor(getResources().getColor(R.color.red));
        view.startAnimation(fadeIn);

        Toast.makeText(getApplicationContext(), theSkill + " deleted",
                Toast.LENGTH_SHORT).show();

        //User clicks on 'undo' option
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Reset the button to the skill
                Button clicked = (Button) view;
                view.startAnimation(fadeOut);
                clicked.setText(theSkill);
                clicked.setTextColor(tc);
                view.startAnimation(fadeIn);

                Toast.makeText(getApplicationContext(), theSkill + " undeleted",
                        Toast.LENGTH_SHORT).show();

                //Add skill to list
                dummySubjectList.add(theSkill.toString());

                //User clicks on skill button
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeSkill(view);
                    }
                });
            }
        });

    }

    public void enableSkillButtons() {
        for (Button b : subjectButtons) {
            b.setEnabled(true);
        }

    }

    public void disableSkillButtons() {
        for (Button b : subjectButtons) {
            b.setEnabled(false);
        }
    }

    public void removeUndoButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.subjectList);
        for (Button b : subjectButtons) {
            if (b.getText() == "Undo") {
                layout.removeView(b);
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
