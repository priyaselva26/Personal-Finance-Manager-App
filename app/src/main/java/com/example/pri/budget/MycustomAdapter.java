package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MycustomAdapter extends BaseAdapter implements ListAdapter {
    public ArrayList<category> list = new ArrayList<category>();
    public Context context;


    public MycustomAdapter(ArrayList<category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    //get the position of item
    public long getItemId(int position) {
        return list.get(position).getId();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.lblreload
        );
        listItemText.setText(list.get(position).getName());

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category row = (category) list.get(position);
                int selected_id = row.getId();
                String budget = row.getName();

                Intent myIntent = new Intent(context, Addbudget.class); //Redirecting to another activity
                myIntent.putExtra("passed data key", budget); // pass your values and retrieve them in the other Activity using keyName
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myIntent);
            }
        });

       return view;
    }

}
