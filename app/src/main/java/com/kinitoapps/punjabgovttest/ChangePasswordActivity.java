package com.kinitoapps.punjabgovttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ChangePasswordActivity extends AppCompatActivity {
    EditText password, newpassword, confpassword;
    SQLiteHandler db;
    String URL_CHANGE_PASSWORD = "https://governmentappcom.000webhostapp.com/change_password.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        db = new SQLiteHandler(getApplicationContext());
        password = findViewById(R.id.currpassword);
        newpassword = findViewById(R.id.newpassword);
        confpassword = findViewById(R.id.confnewpassword);
        Button savebutton = findViewById(R.id.savechangepassword);
        Button cancelbutton = findViewById(R.id.cancelsavepassword);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newpassword.getText().toString().equals(confpassword.getText().toString()))
                changePassword();
                else
                    Toast.makeText(ChangePasswordActivity.this, "Passwords do not match, please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword() {
        // Tag used to cancel the request
        String tag_string_req = "save_details";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_CHANGE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
//                        // User successfully stored in MySQL
//                        // Now store the user in sqlite
//
//
//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String phone = user.getString("phone");
//                        String email = user.getString("email");
//                        String percentage = user.getString("percentage");
//                        String course = user.getString("course");
//                        String field = user.getString("field");
//                        db.deleteUsers();
//                        db.addUser(name, phone, course, field, percentage,email);
//
//
//                        // Inserting row in users table
//                        Log.d("LOGGINGME1", String.valueOf(db));
                        Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                        Intent intent1 = new Intent(SignUpSecondComp.this,ProfilePicUPload.class);
//                        startActivity(intent1);



//                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
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
                params.put("password",password.getText().toString());
                params.put("newpassword",newpassword.getText().toString());
//                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
