package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : list downs the available expense categories to add budget
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class Budget_activity extends Activity implements View.OnClickListener {
    // Declare UI elements
    int selected_id;
    ListView rldlist = null;
    DBhelper helper;
    String budget;
    TextView menubtn;
    Context context;
    EditText txr;
    Button btn1;
    SQLiteDatabase db;

    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_activity);
        context = this;
        helper = new DBhelper(this);

        btn1 = (Button) findViewById(R.id.btnClear);
        menubtn = (TextView) findViewById(R.id.e);
        menubtn.setOnClickListener(this);


        loadList(); //call the loadlist method

    }


    public void loadList() {

        rldlist = (ListView) findViewById(R.id.rldlist);

        ArrayList<category> mArrayList = helper.getCategories(); //calling the method from DBhelper class
        MycustomAdapter adapter = new MycustomAdapter(mArrayList, this);

        //handle listview and assign adapter,view,position
        rldlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                category row = (category) arg0.getItemAtPosition(position); //assigning the position to the adapter
                selected_id = row.getId();
                budget = row.getName();

                Intent myIntent = new Intent(Budget_activity.this, addbudget.class); //redirecting to another activity
                myIntent.putExtra("passed data key", budget); // pass your values and retrieve them in the other Activity using keyName
                startActivity(myIntent);
            }

        });
        rldlist.setAdapter(adapter); //set the adaptor which contain list of items

    }


    @Override
    public void onClick(View v) {

    }

    //when pressing the back button this method will invoked
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }


}
