package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : Adds budget for each category,
 * calculates the expenses for each category and displays the balance amount.
 */
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;


public class Addbudget extends ActionBarActivity implements View.OnClickListener {
    // Declare UI elements
    String data;
    double percent;
    DBhelper helper;
    SQLiteDatabase db;
    EditText txtBudget;
    TextView txtCategory, txtMyBudget, txtCatBal,txtCatExp;
    Button btnAB;
    String category,budAmount,e;

    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbudget);

        btnAB = (Button) findViewById(R.id.btnAddBudget);
        btnAB.setOnClickListener(this);
        helper = new DBhelper(Addbudget.this);
        txtCategory = (TextView) findViewById(R.id.txtCat);
        txtMyBudget = (TextView) findViewById(R.id.txtMyBud);
        txtBudget = (EditText) findViewById(R.id.txtBudget);
        txtCatBal = (TextView) findViewById(R.id.txtBal);
        txtCatExp = (TextView) findViewById(R.id.txtExp);


        txtMyBudget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtBudget.setText(data);  //set the value to the edit text
            }
        });

        Bundle data_from_list = getIntent().getExtras(); // Pass data between activities
        category = data_from_list.getString("passed data key"); // retrieve the data using keyName
        txtCategory.setText(category);

        fetchBudgetData();
    }

    //clear the edit text fields
    private void clearfield() {
        txtBudget.setText("");
    }

    //onclick method for button
    public void onClick(View v) {
        if (btnAB == v) {
            checkIfRowPresent(txtCategory.getText().toString()); //on button click call the method
        }
    }

    //method to add or update
    public boolean checkIfRowPresent(String name) {

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor =
                db.query(DBhelper.TABLE2, null, DBhelper.Description + "='" + name + "'", null, null, null,
                        null, null);

        String bud = txtBudget.getText().toString();


        Calendar c = Calendar.getInstance();
        int month1 = c.get(Calendar.MONTH);

        Cursor res = helper.getMonthlyBudget(month1+1);

        double cat_bud =0;

        if (res.moveToNext()) {

                cat_bud = Double.parseDouble(res.getString(1)) * Double.parseDouble(bud) /100.0;
            }





        boolean ret = false;
        //if the count is greater than 0 update otherwise add the values to the database
        if (cursor.getCount() > 0) {

            ContentValues value = new ContentValues();

            value.put(DBhelper.Amount, cat_bud);
            budAmount=txtBudget.getText().toString();
            if(isValidnum(budAmount)) {
                db = helper.getWritableDatabase();
                db.update(DBhelper.TABLE2, value, " " + DBhelper.Description + "='" + category + "'", null); //update the values
                db.close();
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show(); //success message
                fetchBudgetData();

                Intent i = new Intent(Addbudget.this, MainActivity.class); //redirecting to another activity
                startActivity(i);
                clearfield();
            }
            else{
                Toast.makeText(getApplicationContext(), "Enter a valid amount!", Toast.LENGTH_LONG).show(); //error message
            }
        } else {

            ContentValues value = new ContentValues();

            value.put(DBhelper.Amount, cat_bud); //set the values to the contentvalues
            value.put(DBhelper.Description, txtCategory.getText().toString());
            budAmount=txtBudget.getText().toString();
            //checking for validation
            if (isValidnum(budAmount)) {
                db = helper.getWritableDatabase();
                db.insert(DBhelper.TABLE2, null, value);  //insert values to the database
                db.close();
                clearfield();//to clear the fields
                Toast.makeText(this, "Budget added Successfully", Toast.LENGTH_LONG).show();

                fetchBudgetData();
                Intent i = new Intent(Addbudget.this, MainActivity.class); //redirecting to another activity
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Enter a valid amount!", Toast.LENGTH_LONG).show(); //error message

            }
        }
        db.close();

        return ret;

    }

    //fetching budget data from database
    private void fetchBudgetData() {
        db = helper.getReadableDatabase();

        double balance = 0.0;



        Cursor c = db.query(DBhelper.TABLE2, null, DBhelper.Description + "='" + category + "'", null, null, null, null); //assign the value to the cursor
        if (c.moveToFirst()) {
            data = c.getString(c.getColumnIndex(DBhelper.Amount)); //get the amount of particular description into the textview
            txtMyBudget.setText(data);

            Calendar ca = Calendar.getInstance();
            int month1 = ca.get(Calendar.MONTH);

            Cursor res = helper.getMonthlyBudget(month1+1);

            if(res.moveToNext()){
                percent = Double.parseDouble(data) * 100.0  / Double.parseDouble(res.getString(1));
                txtBudget.setText(String.valueOf(percent));
            }


            balance = Double.parseDouble(data);

            txtCatBal.setText(String.valueOf(balance));

            double totalExp = new DBhelper(this).getCategoryExpences(category);
            if (totalExp != 0) {


                txtCatExp.setText(String.valueOf(totalExp));
                balance = Double.parseDouble(data) - totalExp;
                txtCatBal.setText(String.valueOf(balance));


            }



        }


    }

    //validation for edit  text
    public boolean isValidnum(String w) {
        return w.matches("^(?![.0]*$)\\d+(?:\\.\\d{1,2})?$");
    }



}

