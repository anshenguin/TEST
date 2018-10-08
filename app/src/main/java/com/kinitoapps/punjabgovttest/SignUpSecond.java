package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class SignUpSecond extends AppCompatActivity {
    TextView textviewcourse,textviewfield;
    Spinner course,field;
    ArrayAdapter<String> arrayAdapter,arrayAdapter2;
    ArrayList<String> fieldArray, courseArray;
    HashMap<String, String> hashMap;
    TextView percentage;
    String cat,cat2;
    String URL_SPINNER = "https://governmentappcom.000webhostapp.com/spinnerfield.php?cat=";
    String URL_SPINNER2 = "https://governmentappcom.000webhostapp.com/spinnercourse.php?cat=";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);
        fieldArray = new ArrayList<>();
        courseArray = new ArrayList<>();
        textviewfield = findViewById(R.id.textviewfield);
        textviewcourse = findViewById(R.id.textviewcourse);
        course = findViewById(R.id.course);
        field = findViewById(R.id.field);
        percentage = findViewById(R.id.percentage);
        Intent intent = getIntent();

        hashMap = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        if(!hashMap.get("education").equals("12th")){
            textviewcourse.setVisibility(View.VISIBLE);
            textviewfield.setVisibility(View.VISIBLE);
            course.setVisibility(View.VISIBLE);
            field.setVisibility(View.VISIBLE);
            if(hashMap.get("education").equals("Under Graduate"))
                cat = "0";
            else

                cat = "1";

        }
        else{
            textviewcourse.setVisibility(View.GONE);
            textviewfield.setVisibility(View.GONE);
            course.setVisibility(View.GONE);
            field.setVisibility(View.GONE);
        }
        arrayAdapter2 = new ArrayAdapter<String>(SignUpSecond.this, android.R.layout.simple_spinner_dropdown_item, courseArray);
        arrayAdapter = new ArrayAdapter<String>(SignUpSecond.this, android.R.layout.simple_spinner_dropdown_item, fieldArray);
        course.setAdapter(arrayAdapter2);
        field.setAdapter(arrayAdapter);
        loadSpinner2Data(cat);
        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat2 = course.getItemAtPosition(course.getSelectedItemPosition()).toString();
                loadSpinnerData(cat2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button btn = findViewById(R.id.nextbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpSecond.this,SignUpThird.class);
                hashMap.put("percentage",percentage.getText().toString());
                if(!hashMap.get("education").equals("12th")) {
                    hashMap.put("course", course.getSelectedItem().toString());
                    hashMap.put("field", field.getSelectedItem().toString());
                }
                else{
                    hashMap.put("course", "0");
                    hashMap.put("field", "0");
                }
                intent.putExtra("hashmap",hashMap);
                startActivity(intent);

            }
        });
    }

    private void loadSpinnerData(String cat2) {
        fieldArray.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SPINNER+cat2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                fieldArray.add(jsonObject1.getString("field"));
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

    private void loadSpinner2Data(String cat) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SPINNER2+cat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                courseArray.add(jsonObject1.getString("course"));
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
}
