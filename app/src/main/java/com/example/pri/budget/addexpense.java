package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : Records the details of new expense
 */

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;


public class Addexpense extends ActionBarActivity {
    // Declare UI elements
    DBhelper helper;
    SQLiteDatabase db;
    EditText txtdescription;
    static TextView date;
    EditText txtRs;
    Spinner spnCat;
    Button btnSave;
    public static String SELECTED_DATE = null;

    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addexpenses);

        txtdescription = (EditText) findViewById(R.id.txtDes);
        txtRs = (EditText) findViewById(R.id.txtRs);
        date = (TextView) findViewById(R.id.txtDate);
        btnSave = (Button) findViewById(R.id.btnSave);
        spnCat = (Spinner) findViewById(R.id.spnCat);


        helper = new DBhelper(this);

        ArrayList<category> mArrayList = helper.getCategories(); //set the category object into the arraylist

        ArrayList<String> catStringArray = new ArrayList<String>();

        for (category cat : mArrayList)
            catStringArray.add(cat.getName()); //get the name from the id

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_row, R.id.tv, catStringArray); //set the items into the adapter
        spnCat.setAdapter(adapter); //set the adapter in to the spinner

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                save();  //Calling the save method on button click


            }

        });

    }


    public static void initializeDate() {
        if (SELECTED_DATE != null) {
            date.setText(SELECTED_DATE);
            date.setVisibility(View.VISIBLE);
        } else {
            date.setVisibility(View.INVISIBLE);
        }
    }

    //method to add expenses to the database
    private void save() {

        String desc = txtdescription.getText().toString();
        String Rs = txtRs.getText().toString();
        String category = spnCat.getSelectedItem().toString();



        ContentValues values = new ContentValues();
        values.put(DBhelper.CATEGORY, category);
        values.put(DBhelper.AMOUNT1, Rs);
        values.put(DBhelper.DETAIL, desc);
        values.put(DBhelper.DATE_T1, date.getText().toString());
        values.put(DBhelper.EX_YEAR, SELECTED_DATE.split("-")[0]);  //Split the date to get the year
        values.put(DBhelper.EX_MONTH, SELECTED_DATE.split("-")[1]);  //split the date to get month

        //Checking for the validation
        if (isValidnum2(Rs) && isValidWord(desc)) {
            db = helper.getWritableDatabase();
            db.insert(DBhelper.TABLE3, null, values); //Insert values to the database
            db.close();



            Toast.makeText(this, "Expenses add Successfully", Toast.LENGTH_LONG).show(); //success message
            Intent st = new Intent(Addexpense.this, Addexpense.class);
            clear();
            startActivity(st);

            checkLimit(category);


        } else {
            if (!isValidnum2(Rs)) {
                txtRs.setError("Invalid Amount"); //set error ,if the validations fails
            }
            if (!isValidWord(desc)) {
                txtdescription.setError("Invalid detail");//set error ,if the validations fails
            }


        }




    }

    /**check the expenses of particular category whether it's exceed the budget limit of particular category */
    public void checkLimit(String category){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);

        String rate = new DBhelper(getApplicationContext()).checkBudget(category,month+1);

        if (rate != null) {
            showMessage("Warning", rate + " Expenses are Exceeded");

        } else {
            System.out.println("false");
        }
    }


    public  void  showMessage(String title,String Message){
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    //clear the edit text fields
    public void clear() {

        txtRs.setText("");
        txtdescription.setText("");
        SELECTED_DATE = null;
        initializeDate();
    }

    public void clear(View view) {
        clear();
    }

    //Method to pick date on button click
    public void pickDate(View v) {
        android.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    public boolean isValidnum2(String e) {
        return e.matches("^(?![.0]*$)\\d+(?:\\.\\d{1,2})?$");
    }

    public boolean isValidWord(String w) {
        return w.matches("[A-Za-z][^.]*");
    }


}
