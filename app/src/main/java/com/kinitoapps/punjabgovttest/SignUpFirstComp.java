package com.kinitoapps.punjabgovttest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class SignUpFirstComp extends AppCompatActivity {

    String URL_SPINNER = "https://governmentappcom.000webhostapp.com/spinner.php";
    ArrayAdapter<String> arrayAdapter;
    private ProgressDialog pDialog;
    private Spinner city;
    HashMap<String,String> hashMap;
    Button reg;
    ArrayList<String> locations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_first_comp);

        reg = findViewById(R.id.registercompanynext);
        hashMap = new HashMap<>();
        locations = new ArrayList<>();
        final EditText fullnameedittext = findViewById(R.id.fullnameedittext);
        final EditText phonenumber = findViewById(R.id.phonenumberedittext);
        final EditText optionalphonenumber = findViewById(R.id.optionalphonenumberedittext);
        final EditText addressedittext = findViewById(R.id.addressedittext);
        arrayAdapter = new ArrayAdapter<String>(SignUpFirstComp.this, android.R.layout.simple_spinner_dropdown_item, locations);
        city = findViewById(R.id.city);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        city.setAdapter(arrayAdapter);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpFirstComp.this,SignUpSecondComp.class);
                hashMap.put("name",fullnameedittext.getText().toString());
                hashMap.put("phone",phonenumber.getText().toString());
                hashMap.put("phonesec",optionalphonenumber.getText().toString());
                hashMap.put("address", addressedittext.getText().toString());
                hashMap.put("city",city.getSelectedItem().toString());
                intent.putExtra("hashmap",hashMap);

                startActivity(intent);

            }
        });
        loadSpinnerData();

    }

//    private void registerCompany() {
//        // Tag used to cancel the request
//        String tag_string_req = "req_register";
//
//        pDialog.setMessage("Registering ...");
//        showDialog();
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                URL_REGISTER, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("TAG", "Register Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {
//                        // User successfully stored in MySQL
//                        // Now store the user in sqlite
//
//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("phone");
//                        String course = user.getString("course");
//                        String field = user.getString("field");
//                        String percentage = user.getString("percentage");
//
//                        // Inserting row in users table
////                        db.addUser(name, email, course, field, percentage );
////                        session.setLogin(true);
////                        Log.d("LOGGINGME1", String.valueOf(db));
////                        Intent intent1 = new Intent(SignUpThird.this,JobsActivity.class);
////                        startActivity(intent1);
//
//
//
////                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
//
//                        // Launch login activity
//                    } else {
//
//                        // Error occurred in registration. Get the error
//                        // message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
////                    Toast.makeText(SignUpThird.this, "error aagya", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
////                // Posting params to register url
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("name", name);
////                params.put("phone", email);
////                params.put("password", password);
//
////                return hashMap;
//            }
//
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

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


}
