package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JobsInsideActivity extends AppCompatActivity {
    String URL_JOBS = "http://ngo-link.com/android_api/job_info.php?id=";
    String URL_APPLY = "http://ngo-link.com/android_api/applyForJob.php";
    SQLiteHandler db;
    String jobID;
    TextView name, job, info, address, salary, email, phone, apply,emptype, reqs;
    ImageView imageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_inside);
        Bundle b;
        b = getIntent().getExtras();
        db = new SQLiteHandler(getApplicationContext());
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("About Job");
        actionBar.setDisplayHomeAsUpEnabled(true);
        jobID = b.getString("jobID");
        name = findViewById(R.id.name);
        reqs = findViewById(R.id.reqs);
        email = findViewById(R.id.mail);
        emptype = findViewById(R.id.employmenttype);
        job = findViewById(R.id.job);
        info = findViewById(R.id.info);
        address = findViewById(R.id.add);
        imageViewLogo = findViewById(R.id.logo);
        salary = findViewById(R.id.salary);
        apply = findViewById(R.id.apply_for_job);
        phone = findViewById(R.id.phone);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyForJob();
            }
        });

        jobData();
    }

    private void jobData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_JOBS + jobID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                name.setText(jsonObject1.getString("org"));
                                job.setText(jsonObject1.getString("job"));
                                info.setText(jsonObject1.getString("info"));
                                salary.setText(jsonObject1.getString("salary"));
                                address.setText(jsonObject1.getString("address"));
                                email.setText(jsonObject1.getString("email"));
                                phone.setText(jsonObject1.getString("phone"));
                                emptype.setText(jsonObject1.getString("emptype"));
                                reqs.setText(jsonObject1.getString("course"));

                                Glide.with(JobsInsideActivity.this)
                                        .load(jsonObject1.getString("logolink"))
                                        .apply(new RequestOptions()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .error(R.drawable.ic_no_logo))
                                        .into(imageViewLogo);

                            }


                            //traversing through all the object

                            //creating adapter object and setting it to recyclerview

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

    private void applyForJob() {
        // Tag used to cancel the request
        String tag_string_req = "apply_job";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_APPLY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Apply Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String phone = user.getString("phone");
//                        String course = user.getString("course");
//                        String field = user.getString("field");
//                        String percentage = user.getString("percentage");
//                        String email = user.getString("email");

                        // Inserting row in users table


                        Toast.makeText(getApplicationContext(), "You have successfully registered for the job!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
//                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", db.getUserDetails().get("email"));
                params.put("job_id",jobID);
//                params.put("phone", email);
//                params.put("password", password);

                return params;
            }


        };
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
