package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String URL_JOBS = "https://governmentappcom.000webhostapp.com/actual_joblists.php";
    JobsAdapter jobsAdapter;
    HashMap<String,String> hashMap;
    List<Jobs> jobsList;
    SessionManager session;

    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        db = new SQLiteHandler(getApplicationContext());
        TextView tv = findViewById(R.id.welcome);
        session = new SessionManager(getApplicationContext());
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Job for You");
        actionBar.setDisplayHomeAsUpEnabled(true);

        hashMap = new HashMap<>();
        hashMap.put("course",db.getUserDetails().get("course"));
        hashMap.put("field",db.getUserDetails().get("field"));
        hashMap.put("percentage",db.getUserDetails().get("percentage"));
        hashMap.put("skills", db.getUserDetails().get("skill"));
        tv.setText("Welcome "+db.getUserDetails().get("name"));


        jobsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        jobsAdapter = new JobsAdapter(this,jobsList);
        recyclerView.setAdapter(jobsAdapter);
        jobList();
    }



//    private void loadSpinnerData() {
//        Log.d("LOGGINGME", String.valueOf(hashMap));
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_JOBS+hashMap.get("course")+"&f="
//                + hashMap.get("field"),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            //converting the string to json array object
//                            JSONArray array = new JSONArray(response);
//                            for(int i=0;i<array.length();i++) {
//                                JSONObject jsonObject1 = array.getJSONObject(i);
//                                jobsList.add(new Jobs(jsonObject1.getString("job"),jsonObject1.getString("org"),
//                                        jsonObject1.getString("logo")));
//                            }
//
//
//                            //traversing through all the object
//
//                            //creating adapter object and setting it to recyclerview
//                            jobsAdapter = new JobsAdapter(JobsActivity.this,jobsList);
//                            recyclerView.setAdapter(jobsAdapter);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        Volley.newRequestQueue(this).add(stringRequest);
//
//    }

    private void jobList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("Logging in ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_JOBS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    if(!jObj.getBoolean("error")) {
                        JSONArray array = jObj.getJSONArray("array");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            jobsList.add(new Jobs(jsonObject1.getString("job"), jsonObject1.getString("org"),
                                    jsonObject1.getString("logolink"), jsonObject1.getString("id")));
                        }

                        jobsAdapter = new JobsAdapter(JobsActivity.this, jobsList);
                        recyclerView.setAdapter(jobsAdapter);
                    }
                    else{
                        String error = jObj.getString("error_msg");
                        Toast.makeText(JobsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    // Inserting row in users table


                    // Launch main activity

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return hashMap;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
