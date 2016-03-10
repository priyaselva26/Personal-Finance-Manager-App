package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : Updates expense details
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class u_expenses extends Activity implements View.OnClickListener {
//Declare UI elements
    public static String SELECTED_DATE = null;

    String data2, data3, data4, data5;
    DBhelper helper;
    SQLiteDatabase db;
    EditText rs, disc;
    static TextView date;
    Button btn;
    TextView ed;
    String value_in_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uexpenses_activity);

        btn = (Button) findViewById(R.id.btnSave);
        btn.setOnClickListener(this);

        helper = new DBhelper(u_expenses.this);


        Bundle data_from_list = getIntent().getExtras();
        value_in_tv = data_from_list.getString("data key"); // retrieve the data using keyName

        ed = (TextView) findViewById(R.id.u_spinner);
        disc = (EditText) findViewById(R.id.u_description);
        rs = (EditText) findViewById(R.id.u_rs);
        date = (TextView) findViewById(R.id.txtDate);

        fetchData4(); //call the fetch method

    }

    private void fetchData4() {
        db = helper.getReadableDatabase();
        Cursor c = db.query(DBhelper.TABLE3, null, DBhelper.ID1 + "='" + value_in_tv + "'", null, null, null, null); //get the items from the database for the particular id
        if (c.moveToFirst()) {
            data2 = c.getString(c.getColumnIndex(DBhelper.CATEGORY)); //get the values from the database
            data3 = c.getString(c.getColumnIndex(DBhelper.DETAIL));
            data4 = c.getString(c.getColumnIndex(DBhelper.AMOUNT1));
            data5 = c.getString(c.getColumnIndex(DBhelper.DATE_T1));

            ed.setText(data2); //assign the values to the textfields
            disc.setText(data3);
            rs.setText(data4);
            date.setText(data5);



        }
    }


    public static void initializeDate() {
        if (SELECTED_DATE != null) {
            date.setText(SELECTED_DATE);
            date.setVisibility(View.VISIBLE);
        } else {
            date.setVisibility(View.INVISIBLE);
        }
    }


    //clear method
    public void clear() {
        disc.setText("");
        rs.setText("");
        date.setText("");
        SELECTED_DATE = null;
        initializeDate();
    }

    public void clear(View view) {
        clear();
    }

    //method for pick data on button click
    public void pickDate(View v) {
        android.app.DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    //update method
    public void onClick(View v) {

        String desc = disc.getText().toString();
        String Rs = rs.getText().toString();
        String dat = date.getText().toString();

        ContentValues value = new ContentValues();

        value.put(DBhelper.DETAIL, disc.getText().toString());  //get data from the textfields to database
        value.put(DBhelper.AMOUNT1, rs.getText().toString());
        value.put(DBhelper.DATE_T1, date.getText().toString());
        value.put(DBhelper.EX_YEAR, SELECTED_DATE.split("-")[0]);  //Split the date to get the year
        value.put(DBhelper.EX_MONTH, SELECTED_DATE.split("-")[1]);  //split the date to get month
        if (isValidnum2(Rs) && isValidWord(desc)) {
            db = helper.getWritableDatabase();
            db.update(DBhelper.TABLE3, value, " " + DBhelper.ID1 + "='" + value_in_tv + "'", null); //update the values to the database
            db.close();
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show(); //success message


            Intent i = new Intent(u_expenses.this, MainActivity.class); //redirecting another activity
            startActivity(i);
            clear(); //call the clear method
        } else {
            if (!isValidnum2(Rs)) {
                rs.setError("Invalid Amount"); //set error,if the validations fails
            }
            if (!isValidWord(desc)) {
                disc.setError("Invalid detail");
            }
        }
    }

    //validations for amout and description
    public boolean isValidnum2(String e) {
        return e.matches("^(?![.0]*$)\\d+(?:\\.\\d{1,2})?$");
    }

    public boolean isValidWord(String w) {
        return w.matches("[A-Za-z][^.]*");
    }
}


