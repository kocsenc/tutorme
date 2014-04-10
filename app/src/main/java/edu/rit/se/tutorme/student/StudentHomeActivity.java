package edu.rit.se.tutorme.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.rit.se.tutorme.R;


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
}
