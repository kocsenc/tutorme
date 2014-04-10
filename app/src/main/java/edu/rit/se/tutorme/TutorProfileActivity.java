package edu.rit.se.tutorme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;


public class TutorProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent myIntent = getIntent();
        //String email = myIntent.getStringArrayExtra("Email").toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        //Hide save button, give it functionality
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setVisibility(View.GONE);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveDialogue();
            }
        });

        //Give edit button functionality
        Button editButton = (Button) findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile(findViewById(R.id.edit_button));
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor_profile, menu);
        getMenuInflater().inflate(R.menu.tutor_main, menu);
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

    //Changes 'edit' button to 'save' button
    public void editProfile(View v){

        //Start animation
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        final Animation revTranslate = AnimationUtils.loadAnimation(this, R.anim.reverse_translate);
        v.startAnimation(animTranslate);

        //Hide edit button
        Button editButton = (Button) findViewById(R.id.edit_button);
        editButton.setVisibility(View.GONE);

        //Show save button
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.startAnimation(revTranslate);
        saveButton.setVisibility(View.VISIBLE);
    }

    public void saveDialogue(){
        //TODO: Actual logic for editing profile
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Save Changes");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to save these changes?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_tutorme);

        // YES
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Good job",
                        Toast.LENGTH_SHORT).show();

                final Animation animrevTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reverse_translate);
                final Animation animTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);


                Button saveButton = (Button) findViewById(R.id.save_button);
                saveButton.startAnimation(animTranslate);
                saveButton.setVisibility(View.GONE);

                Button editButton = (Button) findViewById(R.id.edit_button);
                editButton.startAnimation(animrevTranslate);
                editButton.setVisibility(View.VISIBLE);

            }
        });

        // NO
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Logic for not saving
                Toast.makeText(getApplicationContext(), "Why not", Toast.LENGTH_SHORT).show();

                final Animation animrevTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.reverse_translate);
                final Animation animTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);

                Button saveButton = (Button) findViewById(R.id.save_button);
                saveButton.startAnimation(animTranslate);
                saveButton.setVisibility(View.GONE);

                Button editButton = (Button) findViewById(R.id.edit_button);
                editButton.startAnimation(animrevTranslate);
                editButton.setVisibility(View.VISIBLE);

            }
        });

        // Cancel
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "Pick a side",
                //      Toast.LENGTH_SHORT).show();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}