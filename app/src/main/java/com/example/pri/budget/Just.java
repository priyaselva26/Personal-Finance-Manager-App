package com.example.pri.budget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by pri on 3/15/2016.
 */
public class Just extends Activity{

    EditText txtName;
    String name;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.just);

        Bundle data_from_list = getIntent().getExtras();
        name = data_from_list.getString("passed data key"); // retrieve the data using keyName

        txtName = (EditText) findViewById(R.id.txtName);

        txtName.setText(name);


    }

}
