package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class LauncherActivity extends AppCompatActivity {

    SessionManager sessionManager;
    ConstraintLayout seekeroptions;
    ConstraintLayout employeroptions;
    SQLiteHandler sqLiteHandler;
    TextView welcome,welcomecompany,totaljobs;
    LinearLayout lin;
    String URL_NUMBER = "https://governmentappcom.000webhostapp.com/jobcount.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        seekeroptions = findViewById(R.id.seeker_options);
        employeroptions = findViewById(R.id.employer_options);
        welcome = findViewById(R.id.welcome);
        welcomecompany = findViewById(R.id.welcomecompany);
        totaljobs = findViewById(R.id.total_number);
        RelativeLayout view_profile = findViewById(R.id.view_profile);
        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,UserProfileActivity.class));
            }
        });
        lin = findViewById(R.id.lin);
        RelativeLayout viewjobs = findViewById(R.id.view_jobs);
        viewjobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,JobsActivity.class));
            }
        });

        RelativeLayout postjob = findViewById(R.id.post_job);
        postjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,AddJobActivity.class));
            }
        });

        RelativeLayout applied_jobs = findViewById(R.id.view_applied_jobs);
        applied_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,AppliedJobsActivity.class));
            }
        });

        RelativeLayout viewcompany = findViewById(R.id.view_profile_comp);
        viewcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,CompanyProfileActivity.class));
            }
        });

        RelativeLayout logout = findViewById(R.id.logout);
        RelativeLayout logoutcompany = findViewById(R.id.logoutcomp);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setKeyIsCompany(false);
                sessionManager.setLogin(false);
                sqLiteHandler.deleteUsers();
                Toast.makeText(LauncherActivity.this, "You have been successfully logged out.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(LauncherActivity.this,LoginActivity.class));
            }
        });
        logoutcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setKeyIsCompany(false);
                sessionManager.setLogin(false);
                sqLiteHandler.deleteCompany();
                Toast.makeText(LauncherActivity.this, "You have been successfully logged out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LauncherActivity.this, LoginActivityCompany.class));
            }
        });
        SharedPreferences prefs = getSharedPreferences("firstrun", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            sessionManager.setKeyIsCompany(false);
            sessionManager.setLogin(false);
            prefs.edit().putBoolean("firstrun", false).commit();
        }



        RelativeLayout seeker = findViewById(R.id.seeker);
        seeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,LoginActivity.class));
            }
        });

        RelativeLayout giver = findViewById(R.id.giver);
        giver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,LoginActivityCompany.class));
            }
        });

        RelativeLayout jobcalendar = findViewById(R.id.jobcalendar);
        jobcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LauncherActivity.this,CalendarActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sessionManager.isLoggedIn()) {
            lin.setVisibility(View.GONE);
            if (sessionManager.isCompany()){
                seekeroptions.setVisibility(View.GONE);
                employeroptions.setVisibility(View.VISIBLE);
                welcomecompany.setText("Welcome, "+sqLiteHandler.getCompanyDetails().get("name"));
            }
            else{
                seekeroptions.setVisibility(View.VISIBLE);
                employeroptions.setVisibility(View.GONE);
                welcome.setText("Welcome, "+sqLiteHandler.getUserDetails().get("name"));

            }
        }

        else{
            lin.setVisibility(View.VISIBLE);
            seekeroptions.setVisibility(View.GONE);
            employeroptions.setVisibility(View.GONE);
            jobCount();
        }
    }
    private void jobCount() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_NUMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                totaljobs.setText("Total Number of Jobs: " + jsonObject1.getString("num"));
                            }


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
