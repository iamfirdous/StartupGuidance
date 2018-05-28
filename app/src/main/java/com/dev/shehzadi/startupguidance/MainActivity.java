package com.dev.shehzadi.startupguidance;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dev.shehzadi.startupguidance.ui.activities.HomeActivity;
import com.dev.shehzadi.startupguidance.ui.activities.LoginActivity;
import com.dev.shehzadi.startupguidance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
