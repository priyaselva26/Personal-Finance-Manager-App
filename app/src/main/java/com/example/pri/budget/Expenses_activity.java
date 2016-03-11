package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : list downs the available expense categories and provides navigation to add a new expense
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


public class Expenses_activity extends Activity {
    // Declare UI elements
    ListView rldlistEx;
    SimpleCursorAdapter adapter;
    DBhelper helper;
    SQLiteDatabase db;
    String selected_did;

    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_activity);

        helper = new DBhelper(this); //initialize the helper

        rldlistEx = (ListView) findViewById(R.id.rldlistExp);

        fetchData();

        rldlistEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String expense;

                Cursor row = (Cursor) adapter.getItemAtPosition(position);
                selected_did = row.getString(0);
                expense = row.getString(1); //get the name of the selected item


                Intent myIntent = new Intent(Expenses_activity.this, List_activity.class);
                myIntent.putExtra("passed data key", expense);// pass your values and retrieve them in the other Activity using keyName
                startActivity(myIntent);

            }
        });


        final TextView menubtn2 = (TextView) findViewById(R.id.e2);
        menubtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent st = new Intent(Expenses_activity.this, Addexpense.class); //redirecting to another activity
                startActivity(st);
            }
        });


    }

//fetch data from database and load it to database
    private void fetchData() {
        db = helper.getReadableDatabase();
        Cursor c = db.query(DBhelper.TABLE1, null, null, null, null, null, null); //cursor have set of data related to query
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.row2,
                c,
                new String[]{DBhelper.Name},
                new int[]{R.id.lbl});
        rldlistEx.setAdapter(adapter); //set the adapter to listview
    }

    //when back button pressed
    public void onBackPressed() {
        Intent st = new Intent(Expenses_activity.this, MainActivity.class);
        startActivity(st);
        return;
    }


}