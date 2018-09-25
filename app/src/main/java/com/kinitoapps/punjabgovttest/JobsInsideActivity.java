package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class JobsInsideActivity extends AppCompatActivity {
    String URL_JOBS = "https://governmentappcom.000webhostapp.com/job_info.php?id=";
    String jobID;
    TextView name, job, info, address;
    ImageView imageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_inside);
        Bundle b;
        b = getIntent().getExtras();
        jobID = b.getString("jobID");
        name = findViewById(R.id.name);
        job = findViewById(R.id.job);
        info = findViewById(R.id.info);
        address = findViewById(R.id.add);
        imageViewLogo = findViewById(R.id.logo);
        jobData();
    }

    private void jobData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_JOBS+jobID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            for(int i=0;i<array.length();i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                name.setText(jsonObject1.getString("org"));
                                job.setText(jsonObject1.getString("job"));
                                info.setText(jsonObject1.getString("info"));
                                address.setText(jsonObject1.getString("address"));
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
}
