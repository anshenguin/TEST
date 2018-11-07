package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    EditText fullname,phonenumber;
    String URL_SPINNER = "http://ngo-link.com/android_api/spinner.php";
    String URL_USER_DETAILS = "http://ngo-link.com/android_api/getUserDetails.php";
    String URL_USER_EDIT_PROFILE = "http://ngo-link.com/android_api/edit_profile.php";

    Button save_changes;
    ArrayAdapter<String> arrayAdapter;
    SQLiteHandler db;
    ArrayList<String> locations;
    TextView emailtv,ed,perc;
    Spinner cityspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        db = new SQLiteHandler(getApplicationContext());
        locations = new ArrayList<>();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Your Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        save_changes = findViewById(R.id.save_changes);
        fullname = findViewById(R.id.full_name_edittext);
        phonenumber = findViewById(R.id.phone_number_edittext);
        cityspinner = findViewById(R.id.cityspinnereditprofile);
        emailtv = findViewById(R.id.emaileditprofile);
        Button changepassword = findViewById(R.id.change_password);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this,ChangePasswordActivity.class));
            }
        });
        ed = findViewById(R.id.degree);
        perc = findViewById(R.id.percentageeditprofile);
        arrayAdapter = new ArrayAdapter<String>(UserProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, locations);
        cityspinner.setAdapter(arrayAdapter);
        loadSpinnerData();
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
    }
    private void loadSpinnerData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SPINNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                locations.add(jsonObject1.getString("LOCATION"));
                            }

                            arrayAdapter.notifyDataSetChanged();

                            getUserDetails();
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
    private void getUserDetails() {
        // Tag used to cancel the request
        String tag_string_req = "req_details";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_USER_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        fullname.setText(name);
                        String phone = user.getString("phone");
                        phonenumber.setText(phone);
                        String email = user.getString("email");
                        emailtv.setText(email);
                        String city= user.getString("city");
                        cityspinner.setSelection(arrayAdapter.getPosition(city));
                        String percentage = user.getString("percentage");
                        perc.setText(percentage+"%");
                        String education = user.getString("education");
                        String course = user.getString("course");
                        String field = user.getString("field");
                        if(education.equals("12th")){
                            ed.setText("12th Grade");
                        }
                        else{
                            ed.setText(course+" ("+field+")");
                        }

                        // Inserting row in users table
                        Log.d("LOGGINGME1", String.valueOf(db));
//                        Intent intent1 = new Intent(SignUpSecondComp.this,ProfilePicUPload.class);
//                        startActivity(intent1);



//                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

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
//                params.put("name", name);
                params.put("email", db.getUserDetails().get("email"));
//                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void saveChanges() {
        // Tag used to cancel the request
        String tag_string_req = "save_details";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_USER_EDIT_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String phone = user.getString("phone");
                        String email = user.getString("email");
                        String percentage = user.getString("percentage");
                        String course = user.getString("course");
                        String field = user.getString("field");
                        String skills = user.getString("skill");
                        db.deleteUsers();
                        db.addUser(name, phone, course, field, percentage,email, skills);
                        // Inserting row in users table
                        Log.d("LOGGINGME1", String.valueOf(db));
                        Toast.makeText(UserProfileActivity.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
                        finish();
//                        Intent intent1 = new Intent(SignUpSecondComp.this,ProfilePicUPload.class);
//                        startActivity(intent1);



//                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

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
//                params.put("name", name);
                params.put("email", db.getUserDetails().get("email"));
                params.put("name",fullname.getText().toString());
                params.put("city",cityspinner.getSelectedItem().toString());
                params.put("phone",phonenumber.getText().toString());
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
