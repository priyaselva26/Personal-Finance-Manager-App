package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 */

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;




public class MainActivity extends TabActivity {
    public static TabHost tabHost;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // create the TabHost that will contain the Tabs
        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("1");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("2");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("3");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("4");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected

        tab1.setIndicator("Budget");
        Intent intent1 = new Intent(MainActivity.this, MonthlyBudget.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tab1.setContent(new Intent(intent1));


        tab2.setIndicator("Category Budget");
        Intent intent2 = new Intent(MainActivity.this, Budget_activity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tab2.setContent(new Intent(intent2));


        tab3.setIndicator("New Expense");
        Intent intent3 = new Intent(MainActivity.this, Expenses_activity.class);
        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tab3.setContent(new Intent(intent3));


        tab4.setIndicator("New Income");
        Intent intent4 = new Intent(MainActivity.this, Income_activity.class);
        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tab4.setContent(new Intent(intent4));





        /** Add the tabs  to the TabHost to display. */

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);

        //tabHost.addTab(tab3);
        tabHost.getTabWidget().setBackgroundColor(Color.parseColor("#FFFFFF"));


    }
}