package com.kinitoapps.punjabgovttest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddJobSecondActivity extends AppCompatActivity {
    HashMap<String, String> hashMap;
    String cat,cat2;
    public static String URL_INSERTJOB = "https://governmentappcom.000webhostapp.com/insertJob.php";

    private ProgressDialog pDialog;

    SQLiteHandlerCompany db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_second);
        db = new SQLiteHandlerCompany(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        final Spinner coursespinner = findViewById(R.id.coursespinner);
        final Spinner fieldspinner = findViewById(R.id.fieldspinner);
        final EditText minperc = findViewById(R.id.minperc);
        final EditText sal = findViewById(R.id.sal);
        TextView textviewcourse = findViewById(R.id.textviewcourse1);
        TextView textviewfield = findViewById(R.id.textviewfield2);
        Button submitbutton = findViewById(R.id.submitbutton);
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        if(!hashMap.get("qual").equals("12th")){
            textviewcourse.setVisibility(View.VISIBLE);
            textviewfield.setVisibility(View.VISIBLE);
            coursespinner.setVisibility(View.VISIBLE);
            fieldspinner.setVisibility(View.VISIBLE);
            if(hashMap.get("qual").equals("Under Graduate"))
                cat = "0";
            else
                cat = "1";
        }
        else{
            textviewcourse.setVisibility(View.GONE);
            textviewfield.setVisibility(View.GONE);
            coursespinner.setVisibility(View.GONE);
            fieldspinner.setVisibility(View.GONE);
        }

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SignUpSecond.this,SignUpThird.class);
                hashMap.put("minpercent",minperc.getText().toString());
                hashMap.put("salary",sal.getText().toString());
                if(!hashMap.get("qual").equals("12th")) {
                    hashMap.put("course", coursespinner.getSelectedItem().toString());
                    hashMap.put("field", fieldspinner.getSelectedItem().toString());
                    hashMap.put("companyID",db.getUserDetails().get("cid"));

                }
                else{
                    hashMap.put("course", "0");
                    hashMap.put("field", "0");
                }

                insertJob();


            }
        });


    }


    private void insertJob() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Posting Job ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_INSERTJOB, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite



//                        Log.d("LOGGINGME1", String.valueOf(db));
                        Toast.makeText(AddJobSecondActivity.this, "DONE", Toast.LENGTH_SHORT).show();
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
//                // Posting params to register url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", name);
//                params.put("phone", email);
//                params.put("password", password);

                return hashMap;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
