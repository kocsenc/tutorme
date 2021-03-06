package edu.rit.se.tutorme;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.BackendProxy;
import edu.rit.se.tutorme.api.TutorMeUser;
import edu.rit.se.tutorme.api.UserType;
import edu.rit.se.tutorme.api.exceptions.APIResponseException;
import edu.rit.se.tutorme.api.exceptions.AuthenticationException;
import edu.rit.se.tutorme.api.exceptions.TokenMismatchException;

public class SearchTutorsActivity extends ListActivity implements AdapterView.OnItemClickListener {

    ArrayList<TutorMeUser> results = new ArrayList<TutorMeUser>();
    SearchItemAdapter adapter;
    private SearchUserTask searchTask = null;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tutors);
        adapter = new SearchItemAdapter(this, android.R.layout.simple_list_item_1, results);
        setListAdapter(adapter);

        ListView list = (ListView)findViewById(android.R.id.list);
        list.setOnItemClickListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

            searchTask = new SearchUserTask();
            searchTask.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_menu, menu);

        String pkg = "edu.rit.se.tutorme";
        String file = "edu.rit.se.tutorme.SearchTutorsActivity";
        ComponentName name = new ComponentName(pkg, file);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_action_bar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(name));

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TutorMeUser user = adapter.getItem(i);
        Intent intent;
        intent = new Intent(this, ViewTutorProfileActivity.class);
        intent.putExtra("userName", user.getName());
        intent.putExtra("userEmail", user.getEmail());
        intent.putExtra("userType", user.getUserType());
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class SearchUserTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<TutorMeUser> temp = new ArrayList<TutorMeUser>();

        @Override
        protected Boolean doInBackground(Void... voids) {
            BackendInterface api = new BackendProxy();
            temp.clear();
            try {
                temp = api.search(query);
                return true;
            } catch (TokenMismatchException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        /**
         * After a api call
         */
        protected void onPostExecute(final Boolean success) {
            if (success) {
                adapter.clear();
                for(TutorMeUser user: temp){
                    adapter.add(user);
                }

                adapter.notifyDataSetChanged();
            } else {
                results.clear();
                Toast.makeText(getApplicationContext(), "Error Performing Search",
                        Toast.LENGTH_SHORT).show();
            }
        }
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
}
