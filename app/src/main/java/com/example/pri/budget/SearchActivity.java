package com.example.pri.budget;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pri on 3/15/2016.
 */
public class SearchActivity extends Activity {


    ListView lstSearch ;
    EditText txtSearch;
    DBhelper helper;
    String selected_did;

    Button btn;
    private ListView wordsList;
    private static final int REQUEST_CODE = 1234;







    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);

        helper = new DBhelper(this);

        Button speakButton = (Button) findViewById(R.id.speakButton);
        wordsList = (ListView) findViewById(R.id.lstSearch);

        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }


        txtSearch = (EditText) findViewById(R.id.txtSearch);
        btn = (Button) findViewById(R.id.btnSearch);
        lstSearch = (ListView) findViewById(R.id.lstSearch);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String search = txtSearch.getText().toString();



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),

                        android.R.layout.simple_list_item_1,android.R.id.text1, helper.getCategoryDetail(search));
                lstSearch.setAdapter(adapter);

            }
        });


        lstSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String expense;

                String row = (String) adapter.getItemAtPosition(position);
               // selected_did = row.getString(0);
                //expense = row. //get the name of the selected item


                Intent myIntent = new Intent(SearchActivity.this, Just.class);
                myIntent.putExtra("passed data key",row);// pass your values and retrieve them in the other Activity using keyName
                startActivity(myIntent);

            }
        });


    }

    public void speakButtonClicked(View v)
    {
        startVoiceRecognitionActivity();
    }

    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database

    }


}
