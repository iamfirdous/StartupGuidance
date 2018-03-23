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
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
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
                            // user is verified, so you can finish this activity or send user to activity which you want.

                            Intent intent = getIntent();
                            UserModel userModel = (UserModel) intent.getSerializableExtra("user");

                            if(userModel != null){
                                DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference("Users")
                                        .child(user.getUid());

                                reference.setValue(userModel);

                                Uri filePath = intent.getParcelableExtra("filePath");

                                if(filePath != null){
                                    final ProgressDialog progressDialog = new ProgressDialog(this);
                                    progressDialog.setTitle("Uploading your profile image...");
                                    progressDialog.show();

                                    StorageReference ref = FirebaseStorage.getInstance().getReference().child("ProfileImages/"+ userModel.getUid());
                                    ref.putFile(filePath)
                                            .addOnSuccessListener(taskSnapshot -> {
                                                progressDialog.dismiss();
                                                Toast.makeText(this, "Profile image uploaded", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                Toast.makeText(this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnProgressListener(taskSnapshot -> {
                                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                        .getTotalByteCount());
                                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                            });
                                }
                            }

                            Toast.makeText(this, "Authentication successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, HomeActivity.class));
                        }
                        else {
                            // email is not verified, so just prompt the message to the user and restart this activity.
                            // NOTE: don't forget to log out the user.
                            holder.showError("Authentication failed. Make sure you verified your email.");
                            FirebaseAuth.getInstance().signOut();
                            //restart this activity
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
