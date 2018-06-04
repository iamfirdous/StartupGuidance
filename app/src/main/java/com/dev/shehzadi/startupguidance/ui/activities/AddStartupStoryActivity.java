package com.dev.shehzadi.startupguidance.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.StartupStoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import static com.dev.shehzadi.startupguidance.utils.Util.HYPHENATED_PATTERN;
import static com.dev.shehzadi.startupguidance.utils.Util.getCurrentDate;
import static com.dev.shehzadi.startupguidance.utils.Util.getFileExtensionFromUri;
import static com.dev.shehzadi.startupguidance.utils.Util.getFormattedDate;
import static com.dev.shehzadi.startupguidance.utils.Util.getTimeStampForPhotos;

public class AddStartupStoryActivity extends AppCompatActivity {

    private AddStartupStoryViewHolder holder;
    private DatabaseReference startupStoryReference;

    private StartupStoryModel startupStory;

    private Uri fileUri;
    private String fileExtension;
    private final int PICK_IMAGE_REQUEST = 73;
    private final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 458;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_startup_story);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        holder = new AddStartupStoryViewHolder();
        startupStory = new StartupStoryModel();
        startupStory.setPostedByUid(FirebaseAuth.getInstance().getUid());

        holder.buttonSaveStartupStory.setOnClickListener(view -> {
            startupStory.setStoryTitle(holder.etStartupStoryTitle.getText().toString().trim());
            startupStory.setDescription(holder.etDescription.getText().toString().trim());
            startupStory.setAuthorName(holder.etAuthor.getText().toString().trim());
            startupStory.setStory(holder.etStory.getText().toString().trim());

            if(validateForm()){
                uploadStartupStoryPhoto();
            }
            else
                holder.showError("Fill all required* fields");
        });

        holder.ivAddStartupStoryImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_REQUEST_CODE);

            }
            else pickImage();
        });
    }

    private void saveStartupStory() {
        holder.startLoading();
        startupStoryReference = FirebaseDatabase.getInstance().getReference("StartupStories").push();
        startupStory.setStoryId(startupStoryReference.getKey());
        startupStory.setPostedOn(getCurrentDate());

        startupStoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                startupStoryReference.setValue(startupStory).addOnSuccessListener(aVoid -> {
                    holder.stopLoading();
                    Toast.makeText(AddStartupStoryActivity.this, "Startup story created and posted successfully", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadStartupStoryPhoto() {
        if(startupStory != null){
            if(fileUri != null){
                holder.startLoading();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Uploading your startup story photo...");
                progressDialog.show();

                StorageReference ref = FirebaseStorage
                        .getInstance()
                        .getReference()
                        .child("StartupStoryPhotos/startup-story-photo-"
                                + getTimeStampForPhotos()
                                + fileExtension);

                ref.putFile(fileUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(this, "Startup story photos uploaded", Toast.LENGTH_SHORT).show();
                            startupStory.setPhotoLocation(taskSnapshot.getDownloadUrl().toString());
                            saveStartupStory();
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
            else {
                saveStartupStory();
            }
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        if(TextUtils.isEmpty(startupStory.getStoryTitle())){
            holder.etStartupStoryTitle.setError("Startup story title is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(startupStory.getDescription())){
            holder.etDescription.setError("Startup story description is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(startupStory.getAuthorName())){
            holder.etAuthor.setError("Author name is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(startupStory.getStory())){
            holder.etStory.setError("Story is required");
            isValid = false;
        } else if (startupStory.getStory().length() < 501){
            holder.etStory.setError("Story needs at least 500 characters");
            isValid = false;
        }

        return isValid;
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(this, "Read permission was denied.", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "You'll not be able to upload any image to your profile, " +
                            "until you grant the read storage permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            fileUri = data.getData();
            fileExtension = getFileExtensionFromUri(this, fileUri);
            Log.e("Extension", fileExtension);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                holder.ivAddStartupStoryImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    class AddStartupStoryViewHolder {
        EditText etStartupStoryTitle, etDescription, etAuthor, etStory;
        ImageView ivAddStartupStoryImage;
        Button buttonSaveStartupStory;
        TextView tvError;
        FrameLayout frameLayoutProgressBar;

        AddStartupStoryViewHolder() {
            etStartupStoryTitle = findViewById(R.id.editText_title_addStartupStory);
            etDescription = findViewById(R.id.editText_description_addStartupStory);
            etAuthor = findViewById(R.id.editText_authorName_addStartupStory);
            etStory = findViewById(R.id.editText_story_addStartupStory);
            ivAddStartupStoryImage = findViewById(R.id.imageView_addImage_addStartupStory);
            buttonSaveStartupStory = findViewById(R.id.button_saveStartupStory);
            tvError = findViewById(R.id.textView_error_addStartupStory);
            frameLayoutProgressBar = findViewById(R.id.frameLayout_progressBar_addStartupStory);
        }

        void showError(String errorMessage){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(errorMessage);
        }

        void startLoading() {
            tvError.setVisibility(View.GONE);
            etStartupStoryTitle.setEnabled(false);
            etDescription.setEnabled(false);
            etAuthor.setEnabled(false);
            etStory.setEnabled(false);
            ivAddStartupStoryImage.setEnabled(false);
            buttonSaveStartupStory.setEnabled(false);
            frameLayoutProgressBar.setVisibility(View.VISIBLE);
        }

        void stopLoading() {
            etStartupStoryTitle.setEnabled(true);
            etDescription.setEnabled(true);
            etAuthor.setEnabled(true);
            etStory.setEnabled(true);
            ivAddStartupStoryImage.setEnabled(true);
            buttonSaveStartupStory.setEnabled(true);
            frameLayoutProgressBar.setVisibility(View.GONE);
        }
    }
}
