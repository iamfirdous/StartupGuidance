package com.dev.firdous.startupguidance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.firdous.startupguidance.ui.activities.HomeActivity;
import com.dev.firdous.startupguidance.ui.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }
}
