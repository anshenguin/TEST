package com.kinitoapps.punjabgovttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.util.HashMap;

public class SignUpSecondComp extends AppCompatActivity {

    HashMap<String, String> hashMap;

    EditText email,password,confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second_comp);
        Intent intent = getIntent();

        hashMap = (HashMap<String, String>)intent.getSerializableExtra("hashmap");

    }
}
