package com.example.pri.budget;

/**
 * Created by pri on 2/4/2016.
 * Purpose : list downs the available income categories and provides navigation to add a new expense
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Income_activity extends Activity {
    // Declare UI elements
    ListView rldlistInc;
    SimpleCursorAdapter adapter;
    DBhelper helper;
    SQLiteDatabase db;
    String selected_did;

    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_activity);

        helper = new DBhelper(this); //initialize the helper

        rldlistInc = (ListView) findViewById(R.id.rldlistInc);

        fetchData();

        rldlistInc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String income;

                Cursor row = (Cursor) adapter.getItemAtPosition(position);
                selected_did = row.getString(0);
                income = row.getString(1); //get the name of the selected item


                Intent myIntent = new Intent(Income_activity.this, List_activity_income.class);
                myIntent.putExtra("passed data key", income);// pass your values and retrieve them in the other Activity using keyName
                startActivity(myIntent);

            }
        });


        final TextView menubtn2 = (TextView) findViewById(R.id.e2);
        menubtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent st = new Intent(Income_activity.this, Addincome.class); //redirecting to another activity
                startActivity(st);
            }
        });


    }

    //fetch data from database and load it to database
    private void fetchData() {
        db = helper.getReadableDatabase();
        Cursor c = db.query(DBhelper.TABLE5, null, null, null, null, null, null); //cursor have set of data related to query
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.row2,
                c,
                new String[]{DBhelper.Name},
                new int[]{R.id.lbl});
        rldlistInc.setAdapter(adapter); //set the adapter to listview
    }

    //when back button pressed
    public void onBackPressed() {
        Intent st = new Intent(Income_activity.this, MainActivity.class);
        startActivity(st);
        return;
    }


}
