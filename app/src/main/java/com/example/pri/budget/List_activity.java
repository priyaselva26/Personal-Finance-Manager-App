package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : List downs all the expenses and provide navigation to update and delete them.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class List_activity extends ActionBarActivity {
//Delclare UI elements
    ListView rldlistExp;
    SimpleCursorAdapter adapter3;
    DBhelper helper;
    SQLiteDatabase db;
    String selected_id;
    String value, ex;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        context = this;

        helper = new DBhelper(this); //initializing helper

        rldlistExp = (ListView) findViewById(R.id.listExp);

        //assigning adapter,and position
        rldlistExp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> arg0, View arg1, final int position, long arg3) {

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Manage Expenses");

                // set the custom dialog components
                Button up = (Button) dialog.findViewById(R.id.Update);

                Button del = (Button) dialog.findViewById(R.id.Delete);
                // if button is clicked, close the custom dialog
                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor row1 = (Cursor) arg0.getItemAtPosition(position); //get the position of item into the cursor
                        selected_id = row1.getString(0);
                        ex = row1.getString(0);

                        Intent myIntent = new Intent(List_activity.this, u_expenses.class);//redirecting to another activity
                        myIntent.putExtra("data key", ex);// pass your values and retrieve them in the other Activity using keyName
                        startActivity(myIntent);
                        fetchData3();
                    }
                });
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       //set up the alert dialog box
                        AlertDialog.Builder builder = new AlertDialog.Builder(List_activity.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Do you want to delete this selected expenses?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Cursor row = (Cursor) arg0.getItemAtPosition(position); //Get the position of item
                                selected_id = row.getString(0);

                                db = helper.getWritableDatabase();
                                db.delete(DBhelper.TABLE3, DBhelper.ID1 + "=?", new String[]{selected_id}); //Delete the particular item
                                db.close();

                                fetchData3(); //fetch data again to listview to refresh
                                Toast.makeText(List_activity.this, "successfully deleted", Toast.LENGTH_LONG).show(); //Success message

                                dialog.cancel();
                            }

                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                // Redirecting to another activity

                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show(); //show the alert


                    }
                });

                dialog.show();

            }

        });

        Bundle data_from_list = getIntent().getExtras();
        value = data_from_list.getString("passed data key"); // retrieve the data using keyName
        fetchData3();

    }



    private void fetchData3() {
        db = helper.getReadableDatabase();
        Cursor c3 = db.query(DBhelper.TABLE3, null, DBhelper.CATEGORY + "='" + value + "'", null, null, null, null);
        adapter3 = new SimpleCursorAdapter(
                this,
                R.layout.row3,
                c3,
                new String[]{DBhelper.DATE_T1, DBhelper.DETAIL, DBhelper.AMOUNT1},
                new int[]{R.id.date, R.id.txtDesc, R.id.txtAmount});
        rldlistExp.setAdapter(adapter3);
    }


}
