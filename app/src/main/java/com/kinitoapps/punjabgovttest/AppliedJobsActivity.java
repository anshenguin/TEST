package com.kinitoapps.punjabgovttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppliedJobsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Jobs> jobsList;
    String URL_JOBS = "http://ngo-link.com/android_api/applied_job_info.php";

    SQLiteHandler db;
    JobsAdapter jobsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);
        jobsList = new ArrayList<>();
        db = new SQLiteHandler(getApplicationContext());
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Applied Jobs");
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerViewAppliedJobs);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        jobsAdapter = new JobsAdapter(this,jobsList);
        recyclerView.setAdapter(jobsAdapter);
        appliedJobList();

    }

    private void appliedJobList() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

//        pDialog.setMessage("Logging in ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_JOBS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    if(!jObj.getBoolean("error")) {
                        JSONArray array = jObj.getJSONArray("array");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            jobsList.add(new Jobs(jsonObject1.getString("job"), jsonObject1.getString("org"),
                                    jsonObject1.getString("logolink"), jsonObject1.getString("id")));
                        }

                        jobsAdapter = new JobsAdapter(AppliedJobsActivity.this, jobsList);
                        recyclerView.setAdapter(jobsAdapter);
                    }
                    else{
                        String error = jObj.getString("error_msg");
                        Toast.makeText(AppliedJobsActivity.this, error, Toast.LENGTH_SHORT).show();
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
                Log.e("TAG", "Unexpected Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", db.getUserDetails().get("email"));
//                params.put("phone", email);
//                params.put("password", password);
                return params;
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
