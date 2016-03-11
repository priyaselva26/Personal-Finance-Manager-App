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


public class addbudget extends ActionBarActivity implements View.OnClickListener {
    // Declare UI elements
    String data;
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
        helper = new DBhelper(addbudget.this);
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

        fetchData2();
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


        //Cursor res = db.rawQuery("select amount from " + DBhelper.TABLE_NAME + " where YEAR  = '2016' " + " and MONTH = '1'", null);

        Calendar c = Calendar.getInstance();
        int month1 = c.get(Calendar.MONTH);

        Cursor res = helper.getMonthlyBudget(month1+1);

        double cat_bud =0;

        while (res.moveToNext()) {
            if (res.getCount() == 0) {
                Toast.makeText(this, "Budget hasn't been allocated", Toast.LENGTH_LONG).show();
            }
            else {
                cat_bud = Double.parseDouble(res.getString(1)) * Double.parseDouble(bud) /100.0;
            }

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
                fetchData2();

                Intent i = new Intent(addbudget.this, MainActivity.class); //redirecting to another activity
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

                fetchData2();
                Intent i = new Intent(addbudget.this, MainActivity.class); //redirecting to another activity
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Enter a valid amount!", Toast.LENGTH_LONG).show(); //error message

            }
        }
        db.close();

        return ret;

    }

    //fetching data from database
    private void fetchData2() {
        db = helper.getReadableDatabase();
        double total = 0.0;
        double balance = 0.0;



        Cursor c = db.query(DBhelper.TABLE2, null, DBhelper.Description + "='" + category + "'", null, null, null, null); //assign the value to the cursor
        if (c.moveToFirst()) {
            data = c.getString(c.getColumnIndex(DBhelper.Amount)); //get the amount of particular description into the textview
            txtMyBudget.setText(data);

            balance = Double.parseDouble(data);

            txtCatBal.setText(String.valueOf(balance));

            ArrayList<Expence> expences = new DBhelper(this).getCategoryExpences(category);
            if (expences != null) {

                for (Expence ex : expences) {
                    total = total + Double.parseDouble(ex.getAmount());
                }
                txtCatExp.setText(String.valueOf(total));
                balance = Double.parseDouble(data) - total;
                txtCatBal.setText(String.valueOf(balance));


            }



        }


    }

    //validation for edit  text
    public boolean isValidnum(String w) {
        return w.matches("^(?![.0]*$)\\d+(?:\\.\\d{1,2})?$");
    }



}

