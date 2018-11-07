package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

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

public class AddJobActivity extends AppCompatActivity {

    HashMap<String,String> hashMap;
    String URL_SPINNER = "http://ngo-link.com/android_api/spinner.php";
    String URL_SPINNER2 = "http://ngo-link.com/android_api/spinneredu.php";
    ArrayAdapter<String> arrayAdapter,arrayAdapter2;
    ArrayList<String> locations,education;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        hashMap = new HashMap<>();
        locations = new ArrayList<>();
        education = new ArrayList<>();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add a New Job");
        actionBar.setDisplayHomeAsUpEnabled(true);
        final EditText job = findViewById(R.id.jobnamehere);
        final EditText jobinfo = findViewById(R.id.inf);
        final EditText email = findViewById(R.id.emailet);
        final EditText phoneone = findViewById(R.id.phoneone);
        final EditText phonetwo = findViewById(R.id.phonetwo);
        final EditText address = findViewById(R.id.add);
        final Spinner cityspinner = findViewById(R.id.cityspinner);
        final Spinner qualspinner = findViewById(R.id.qualspinner);
        final RadioButton parttime = findViewById(R.id.parttime);
        final RadioButton fulltime = findViewById(R.id.fulltime);
        Button next = findViewById(R.id.nextbuttonjobpost);
        arrayAdapter2 = new ArrayAdapter<String>(AddJobActivity.this, android.R.layout.simple_spinner_dropdown_item, education);
        arrayAdapter = new ArrayAdapter<String>(AddJobActivity.this, android.R.layout.simple_spinner_dropdown_item, locations);
        cityspinner.setAdapter(arrayAdapter);
        qualspinner.setAdapter(arrayAdapter2);
        loadSpinnerData();
        loadSpinner2Data();
        if(parttime.isChecked()) {
            parttime.setTextColor(Color.parseColor("#FFFFFF"));
            fulltime.setTextColor(Color.parseColor("#9E9E9E"));
        }
        else{
            fulltime.setTextColor(Color.parseColor("#FFFFFF"));
            parttime.setTextColor(Color.parseColor("#9E9E9E"));
        }

        parttime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(parttime.isChecked()) {
                    parttime.setTextColor(Color.parseColor("#FFFFFF"));
                    fulltime.setTextColor(Color.parseColor("#9E9E9E"));
                }
                else{
                    fulltime.setTextColor(Color.parseColor("#FFFFFF"));
                    parttime.setTextColor(Color.parseColor("#9E9E9E"));
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddJobActivity.this,AddJobSecondActivity.class);
                hashMap.put("name",job.getText().toString());
                hashMap.put("jobinfo",jobinfo.getText().toString());
                hashMap.put("email",email.getText().toString());
                hashMap.put("phone",phoneone.getText().toString());
                hashMap.put("phonesec",phonetwo.getText().toString());
                hashMap.put("address",address.getText().toString());
                hashMap.put("city", cityspinner.getSelectedItem().toString());
                hashMap.put("qual",qualspinner.getSelectedItem().toString());
                if(parttime.isChecked())
                    hashMap.put("emptype","Part Time");
                else
                    hashMap.put("emptype", "Full Time");

                intent.putExtra("hashmap",hashMap);
                startActivity(intent);
                    /*
    $companyID = $_POST['companyID'];
    $course = $_POST['course'];
    $field = $_POST['field'];
    $minpercent = $_POST['minpercent'];
    $salary = $_POST['salary'];
     */
            }
        });
    }

        private void loadSpinner2Data() {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SPINNER2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);
                                for(int i=0;i<array.length();i++) {
                                    JSONObject jsonObject1 = array.getJSONObject(i);
                                    education.add(jsonObject1.getString("EDUCATION"));
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
                                arrayAdapter2.notifyDataSetChanged();

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
                                arrayAdapter.notifyDataSetChanged();

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
