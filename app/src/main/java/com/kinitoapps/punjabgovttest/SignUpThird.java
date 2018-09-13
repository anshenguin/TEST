package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class SignUpThird extends AppCompatActivity {
    TextInputEditText email,password,confpassword;
    HashMap<String,String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_third);
        Intent intent = getIntent();
        hashMap = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confpassword = findViewById(R.id.confpassword);
        Button reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(confpassword.getText().toString())){
                    hashMap.put("email",email.getText().toString());
                    hashMap.put("password",password.getText().toString());
                    Intent intent1 = new Intent(SignUpThird.this,JobsActivity.class);
                    intent1.putExtra("hashmap",hashMap);
                    startActivity(intent1);
                }
            }
        });
    }
}
