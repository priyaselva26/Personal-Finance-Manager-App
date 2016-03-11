package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : Set monthly budget and view yearly,weekly,daily budget.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class MonthlyBudget extends Activity implements View.OnClickListener {

    DBhelper myDb;
    EditText m_budget;
    Button b_set,b_view;
    TextView t_week,t_year,t_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        myDb = new DBhelper(this);

        m_budget = (EditText)findViewById(R.id.txtMBudget);
        b_set = (Button)findViewById(R.id.btnset);
        b_view =(Button)findViewById(R.id.btnview);

        t_week = (TextView)findViewById(R.id.txtMyBud);
        t_year = (TextView)findViewById(R.id.textView6);
        t_day = (TextView)findViewById(R.id.textView7);



        AddData();
        viewAll();


    }


    public  void  AddData(){
        b_set.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {

                        Calendar c = Calendar.getInstance();
                        //int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);


                        boolean isInserted = myDb.insertMonthlyBudget(month + 1, Double.parseDouble(m_budget.getText().toString()));

                        if (isInserted == true) {
                            Toast.makeText(MonthlyBudget.this, "Budget has been set", Toast.LENGTH_LONG).show();

                        } else
                            Toast.makeText(MonthlyBudget.this, "Cannot proceed", Toast.LENGTH_LONG).show();


                        Cursor res = myDb.calBudget(month + 1);

                        StringBuffer buffer = new StringBuffer();
                        double weekly = 0;
                        double daily = 0;
                        double yearly = 0;

                        while (res.moveToNext()) {
                            String monthly = res.getString(1);

                            daily = Double.parseDouble(monthly) / 30.0;
                            weekly = Double.parseDouble(monthly) / 4.0;
                            yearly = Double.parseDouble(monthly) * 12;

                        }

                        String stringdouble = Double.toString(weekly);
                        t_week.setText(stringdouble);
                        String stringdouble1 = Double.toString(yearly);
                        t_year.setText(stringdouble1);
                        String stringdouble2 = Double.toString(daily);
                        t_day.setText(stringdouble2);

                    }


                }

        );


    }


    public  void  viewAll(){
        b_view.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {
                        String month;
                        int cMonth;

                        Calendar c = Calendar.getInstance();
                        int month1 = c.get(Calendar.MONTH);

                        Cursor res = myDb.getMonthlyBudget(month1 +1);

                        if (res.getCount() == 0) {
                            showMessage("Error", "Nothing found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            cMonth = Integer.parseInt(res.getString(0)) - 1;
                            month = getMonthForInt(cMonth);


                            buffer.append(month + " Budget is : " + res.getString(1) + "\n");

                        }

                        showMessage("Budget", buffer.toString());
                    }
                }
        );
    }

    public  void  showMessage(String title,String Message){
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){

    }

    String getMonthForInt(int num) {
        String month = " ";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}
