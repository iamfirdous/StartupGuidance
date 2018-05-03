package com.dev.shehzadi.startupguidance.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.dev.shehzadi.startupguidance.MainActivity;
import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.UserModel;
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

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        holder.buttonLoginGoogle.setOnClickListener(view -> {
            startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
        });

        holder.buttonSignUp.setOnClickListener(view -> {
            startActivity(new Intent(this, SignupActivity.class));
            if (accJustCreated != null)
                if (accJustCreated.equals("accJustCreated"))
                    finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.e("AuthFailed1", "Google sign in failed");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("AuthFailed2", "Google sign in failed");
                signIn(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("AuthFailed", "Google sign in failed : " + e.getMessage(), e);
            }
        }
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

    private void signIn(GoogleSignInAccount account){

        holder.startLoading();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Authentication successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, HomeActivity.class));
                    }
                    else {
                        Log.e("AuthFailed", task.getException().toString());
                        holder.showError("Authentication failed.");
                    }
                    holder.stopLoading();
                });


    }

    class LoginViewHolder {

        EditText etEmail, etPassword;
        TextView tvForgotPassword, tvError;
        Button buttonLogin, buttonLoginGoogle, buttonLoginFacebook, buttonSignUp;
        FrameLayout frameLayoutProgressBar;

        LoginViewHolder(){
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

        void showError(String errorMessage){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(errorMessage);
        }

        void startLoading(){
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

        void stopLoading(){
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
