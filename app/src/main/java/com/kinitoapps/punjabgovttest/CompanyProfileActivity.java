package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompanyProfileActivity extends AppCompatActivity {
    String URL_SPINNER = "https://governmentappcom.000webhostapp.com/spinner.php";
    String URL_COMPANY_DETAILS = "https://governmentappcom.000webhostapp.com/getCompanyDetails.php";
    String URL_USER_EDIT_COMPANY = "https://governmentappcom.000webhostapp.com/edit_company.php";

    Spinner cityspinner;
    ArrayList<String> locations;
    ArrayAdapter<String> arrayAdapter;
    SQLiteHandler db;
    ImageView dp;
    EditText fullname,phonenumber,phonenumbersec,addresstv;
    TextView emailtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        fullname = findViewById(R.id.company_name_edittext);
        phonenumber = findViewById(R.id.phone_number1);
        phonenumbersec = findViewById(R.id.phone_number2);
        addresstv = findViewById(R.id.addresseditcompany);
        Button changepassword = findViewById(R.id.change_password_company);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyProfileActivity.this,ChangePasswordCompanyActivity.class));
            }
        });

        emailtv = findViewById(R.id.emaileditcompany);
        Button savechanges = findViewById(R.id.save_changes_company);
        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        dp = findViewById(R.id.dp);

        locations = new ArrayList<>();
        db = new SQLiteHandler(getApplicationContext());
        cityspinner = findViewById(R.id.cityspinnereditcompany);
        arrayAdapter = new ArrayAdapter<String>(CompanyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, locations);
        cityspinner.setAdapter(arrayAdapter);
        loadSpinnerData();


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

                            getCompanyDetails();
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

    private void getCompanyDetails() {
        // Tag used to cancel the request
        String tag_string_req = "req_details";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_COMPANY_DETAILS, new Response.Listener<String>() {

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
                        String phonesec = user.getString("phonesec");
                        phonenumbersec.setText(phonesec);
                        String email = user.getString("email");
                        emailtv.setText(email);
                        String city= user.getString("city");
                        cityspinner.setSelection(arrayAdapter.getPosition(city));
                        String logo = user.getString("logo");
                        Glide.with(CompanyProfileActivity.this)
                        .load(logo)
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        holder.progressBar.setVisibility(View.GONE);
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        holder.progressBar.setVisibility(View.GONE);
//                                        return false;
//                                    }
//                                })
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.ic_no_logo))
                                .into(dp);
                        String address = user.getString("address");
                        addresstv.setText(address);
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
                params.put("email", db.getCompanyDetails().get("email"));
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
                URL_USER_EDIT_COMPANY, new Response.Listener<String>() {

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
                        String phonesec = user.getString("phonesec");
                        String cid = user.getString("cid");
                        String email = user.getString("email");
                        String address = user.getString("address");
                        String city = user.getString("city");
                        db.deleteCompany();
                        db.addCompany(name,cid,email,phone,phonesec,address,city);


                        // Inserting row in users table
                        Log.d("LOGGINGME1", String.valueOf(db));
                        Toast.makeText(CompanyProfileActivity.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
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
                params.put("name",fullname.getText().toString());
                params.put("email", db.getCompanyDetails().get("email"));
                params.put("phone",phonenumber.getText().toString());
                params.put("phonesec",phonenumbersec.getText().toString());
                params.put("address",addresstv.getText().toString());
                params.put("city",cityspinner.getSelectedItem().toString());

//                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



}
