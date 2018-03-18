package com.dev.firdous.startupguidance.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.firdous.startupguidance.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
    }
}
