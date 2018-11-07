package com.kinitoapps.punjabgovttest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpSecondComp extends AppCompatActivity {

    HashMap<String, String> hashMap;
    private ProgressDialog pDialog;
    SessionManager session;
    SQLiteHandler db;
    public static String URL_REGISTER = "http://ngo-link.com/android_api/signupcompany.php";
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second_comp);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Company Registration");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplication());
        email = findViewById(R.id.emailedittext);
        final EditText password = findViewById(R.id.passwordedittext);
        final EditText confirmpassword = findViewById(R.id.confirmpassword);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        Button signupcompany = findViewById(R.id.signupcompany);
        signupcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(confirmpassword.getText().toString())){
                    hashMap.put("email",email.getText().toString());
                    hashMap.put("password",password.getText().toString());
                    registerCompany();
                }
            }
        });



    }

    private void registerCompany() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_REGISTER, new Response.Listener<String>() {

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


                        JSONObject user = jObj.getJSONObject("company");
                        String name = user.getString("name");
                        String phone = user.getString("phone");
                        String phonesec = user.getString("phonesec");
                        String email = user.getString("email");
                        String city= user.getString("city");
                        String address = user.getString("address");
                        String CID = user.getString("cid");
                        // Inserting row in users table
                        db.addCompany(name, CID ,email, phone, phonesec, address, city);
                        session.setLogin(true);
                        session.setKeyIsCompany(true);
                        Log.d("LOGGINGME1", String.valueOf(db));
                        Toast.makeText(SignUpSecondComp.this, "DONE", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpSecondComp.this,LauncherActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
