package com.dev.firdous.startupguidance.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.firdous.startupguidance.MainActivity;
import com.dev.firdous.startupguidance.R;
import com.dev.firdous.startupguidance.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private LoginViewHolder holder;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        holder = new LoginViewHolder();

        String accJustCreated = getIntent().getStringExtra("accJustCreated");

        if (accJustCreated != null)
            if (accJustCreated.equals("accJustCreated"))
                holder.showError("You need to verify your email before logging in. Please follow the link in your email to verify your account");

        holder.buttonLogin.setOnClickListener(view -> {
            email = holder.etEmail.getText().toString().trim();
            password = holder.etPassword.getText().toString().trim();

            boolean isValidated = true;

            if(TextUtils.isEmpty(email)){
                holder.etEmail.setError("Email is required");
                isValidated = false;
            }
            else
                holder.etEmail.setError(null);

            if(TextUtils.isEmpty(password)){
                holder.etPassword.setError("Password is required");
                isValidated = false;
            }
            else
                holder.etPassword.setError(null);

            if(isValidated){
                signIn(email, password);
            }
        });

        holder.buttonSignUp.setOnClickListener(view -> {
            startActivity(new Intent(this, SignupActivity.class));
            if (accJustCreated != null)
                if (accJustCreated.equals("accJustCreated"))
                    finish();
        });
    }

    private void signIn(String email, String password) {
        Log.d("", "signIn:" + email);

        holder.startLoading();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user.isEmailVerified()) {
                            Toast.makeText(this, "Authentication successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        }
                        else {
                            holder.showError("Authentication failed. Make sure you verified your email.");
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                    else {
                        holder.showError("Authentication failed. Incorrect username or password");
                    }

                    holder.stopLoading();
                });
    }

    class LoginViewHolder {

        EditText etEmail, etPassword;
        TextView tvForgotPassword, tvError;
        Button buttonLogin, buttonLoginGoogle, buttonLoginFacebook, buttonSignUp;
        FrameLayout frameLayoutProgressBar;

        public LoginViewHolder(){
            etEmail = findViewById(R.id.editText_email_login);
            etPassword = findViewById(R.id.editText_password_login);
            tvForgotPassword = findViewById(R.id.textView_forgotPassword_login);
            tvError = findViewById(R.id.textView_error_login);
            buttonLogin = findViewById(R.id.button_login);
            buttonLoginGoogle = findViewById(R.id.button_login_google);
            buttonLoginFacebook = findViewById(R.id.button_login_facebook);
            buttonSignUp = findViewById(R.id.button_signup_login);
            frameLayoutProgressBar = findViewById(R.id.frameLayout_progressBar_login);
        }

        public void showError(String errorMessage){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(errorMessage);
        }

        public void startLoading(){
            tvError.setVisibility(View.GONE);
            etEmail.setEnabled(false);
            etPassword.setEnabled(false);
            tvForgotPassword.setEnabled(false);
            buttonLogin.setEnabled(false);
            buttonLoginGoogle.setEnabled(false);
            buttonLoginFacebook.setEnabled(false);
            buttonSignUp.setEnabled(false);
            frameLayoutProgressBar.setVisibility(View.VISIBLE);
        }

        public void stopLoading(){
            etEmail.setEnabled(true);
            etPassword.setEnabled(true);
            tvForgotPassword.setEnabled(true);
            buttonLogin.setEnabled(true);
            buttonLoginGoogle.setEnabled(true);
            buttonLoginFacebook.setEnabled(true);
            buttonSignUp.setEnabled(true);
            frameLayoutProgressBar.setVisibility(View.GONE);
        }
    }
}
