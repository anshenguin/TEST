package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class JobsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String URL_JOBS = "https://governmentappcom.000webhostapp.com/jobslist.php?c=";
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
        Button logout = findViewById(R.id.logout);
        TextView tv = findViewById(R.id.welcome);
        session = new SessionManager(getApplicationContext());

        tv.setText("Welcome "+db.getUserDetails().get("name"));
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();

            }
        });
        hashMap = new HashMap<>();
        hashMap.put("course",db.getUserDetails().get("course"));
        hashMap.put("field",db.getUserDetails().get("field"));
        jobsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        jobsAdapter = new JobsAdapter(this,jobsList);
        recyclerView.setAdapter(jobsAdapter);
        loadSpinnerData();

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(JobsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadSpinnerData() {
        Log.d("LOGGINGME", String.valueOf(hashMap));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_JOBS+hashMap.get("course")+"&f="
                + hashMap.get("field"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                jobsList.add(new Jobs(jsonObject1.getString("job")));

                            }


                            //traversing through all the object
//                            for (int i = 0; i < array.length(); i++) {
//
//                                //getting product object from json array
//                                JSONObject product = array.getJSONObject(i);
//
//                                //adding the product to product list
//
//
//                                productList.add(new Product(
//                                        product.getInt("id"),
//                                        product.getString("title"),
//                                        product.getString("shortdesc"),
//                                        product.getDouble("rating"),
//                                        product.getDouble("price"),
//                                        product.getString("image")
//                                ));
//                            }

                            //creating adapter object and setting it to recyclerview
                            jobsAdapter = new JobsAdapter(JobsActivity.this,jobsList);
                            recyclerView.setAdapter(jobsAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);

    }

}
