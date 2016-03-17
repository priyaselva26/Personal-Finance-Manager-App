package com.example.pri.budget;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pri on 3/17/2016.
 */
public class TodaysTransaction extends ActionBarActivity {

    TextView txtInc, txtExp, txtTotal,txtDate;
    DBhelper helper;
    double tIncome, tExpense, tTotal;

    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
    Date todayDate = new Date();
    String thisDate = currentDate.format(todayDate);




    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todays_transaction);

        txtInc = (TextView) findViewById(R.id.lblIncome);
        txtExp = (TextView) findViewById(R.id.lblExpense);
        txtTotal = (TextView) findViewById(R.id.lblTotal);
        txtDate = (TextView) findViewById(R.id.lblDate);

        helper = new DBhelper(this);

        tIncome = helper.getTodaysIncome();
        tExpense = helper.getTodaysExpense();
        tTotal = tIncome - tExpense;

        txtInc.setText(String.valueOf(tIncome));
        txtExp.setText(String.valueOf(tExpense));
        txtTotal.setText(String.valueOf(tTotal));
        txtDate.setText(thisDate);


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
