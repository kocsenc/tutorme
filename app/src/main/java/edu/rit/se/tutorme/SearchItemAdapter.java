package edu.rit.se.tutorme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.rit.se.tutorme.api.TutorMeUser;

/**
 * Created by Justin on 4/14/2014.
 */
public class SearchItemAdapter extends ArrayAdapter<TutorMeUser> {

    private ArrayList<TutorMeUser> results;

    public SearchItemAdapter(Context context, int textViewResourceId, ArrayList<TutorMeUser> objects){
        super(context, textViewResourceId, objects);
        results = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.search_item, null);
        }

        TutorMeUser user = results.get(position);

        if(user != null){
            TextView name = (TextView)v.findViewById(R.id.name);
            TextView tutorname = (TextView)v.findViewById(R.id.tutorname);
            TextView rate = (TextView)v.findViewById(R.id.rate);
            TextView tutorrate = (TextView)v.findViewById(R.id.tutorrate);

            if(name != null){
                name.setText("Name: ");
            }

            if(tutorname != null){
                tutorname.setText(user.getName());
            }

            if(rate != null){
                rate.setText("Rate: ");
            }

            if(tutorrate != null){
                tutorrate.setText(user.getProfile().getRate().toString());
            }
        }

        return v;
    }
}
