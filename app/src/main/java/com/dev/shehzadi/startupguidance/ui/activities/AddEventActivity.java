package com.dev.shehzadi.startupguidance.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.shehzadi.startupguidance.R;
import com.dev.shehzadi.startupguidance.models.EventModel;
import com.dev.shehzadi.startupguidance.models.LocationModel;
import com.dev.shehzadi.startupguidance.models.UserModel;
import com.dev.shehzadi.startupguidance.ui.fragments.DatePickerFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.math.BigDecimal;

import static com.dev.shehzadi.startupguidance.utils.Util.HYPHENATED_PATTERN;
import static com.dev.shehzadi.startupguidance.utils.Util.getFormattedDate;

public class AddEventActivity extends AppCompatActivity {

    private AddEventViewHolder holder;
    private DatabaseReference eventReference;

    private EventModel event;
    private LocationModel location;
    private String eventDate, regLastDate;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 72;
    private final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 457;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        holder = new AddEventViewHolder();
        event = new EventModel();
        location = new LocationModel();
        event.setEventOrganizerUid(FirebaseAuth.getInstance().getUid());

        holder.buttonSaveEvent.setOnClickListener(view -> {
            event.setEventTitle(holder.etEventTitle.getText().toString().trim());
            event.setDescription(holder.etEventDescription.getText().toString().trim());

            eventDate = holder.etDate.getText().toString().trim();
            if(!TextUtils.isEmpty(eventDate)){
                event.setEventDate(getFormattedDate(eventDate, HYPHENATED_PATTERN));
            }

            location.setAddressLine1(holder.etAddressLine1.getText().toString().trim());
            location.setAddressLine2(holder.etAddressLine2.getText().toString().trim());
            location.setCity(holder.etCity.getText().toString().trim());
            location.setState(holder.etState.getText().toString().trim());
            location.setPinCode(holder.etPinCode.getText().toString().trim());
            location.setLandmark(holder.etLandmark.getText().toString().trim());
            event.setLocation(location);

            if(!TextUtils.isEmpty(holder.etMaxRegCount.getText().toString().trim())){
                event.setMaxRegistrationCount(Integer.parseInt(holder.etMaxRegCount.getText().toString().trim()));
            }

            regLastDate = holder.etRegLastDate.getText().toString().trim();
            if(!TextUtils.isEmpty(regLastDate)){
                event.setRegistrationLastDate(getFormattedDate(regLastDate, HYPHENATED_PATTERN));
            }

            if(!TextUtils.isEmpty(holder.etRegFee.getText().toString().trim())){
                event.setRegistrationFee(Integer.parseInt(holder.etRegFee.getText().toString().trim()));
            }

            if(validateForm()){
                uploadEventPhoto();
            }
            else
                holder.showError("Fill all required* fields");
        });

        holder.ivAddEventImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_REQUEST_CODE);

            }
            else pickImage();
        });
    }

    private void saveEvent() {
        holder.startLoading();
        eventReference = FirebaseDatabase.getInstance().getReference("Events").push();
        event.setEventId(eventReference.getKey());

        eventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventReference.setValue(event).addOnSuccessListener(aVoid -> {
                    holder.stopLoading();
                    Toast.makeText(AddEventActivity.this, "Event created and posted successfully", Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadEventPhoto() {
        if(event != null){
            if(filePath != null){
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading your event photo...");
                progressDialog.show();

                StorageReference ref = FirebaseStorage.getInstance().getReference().child("EventPhotos/"+ event.getEventId());

                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(this, "Event photos uploaded", Toast.LENGTH_SHORT).show();
                            event.setPhotoLocation(taskSnapshot.getDownloadUrl().toString());
                            saveEvent();
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
                saveEvent();
            }
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        if(TextUtils.isEmpty(event.getEventTitle())){
            holder.etEventTitle.setError("Event title is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(event.getDescription())){
            holder.etEventDescription.setError("Event description is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(eventDate)){
            holder.etDate.setError("Event date is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(event.getLocation().getAddressLine1())){
            holder.etAddressLine1.setError("Address line 1 is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(event.getLocation().getCity())){
            holder.etCity.setError("City is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(event.getLocation().getState())){
            holder.etState.setError("State is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(event.getLocation().getPinCode())){
            holder.etPinCode.setError("PIN code is required");
            isValid = false;
        }

        if(TextUtils.isEmpty(holder.etMaxRegCount.getText().toString().trim())){
            holder.etMaxRegCount.setError("Max. registration count is required. Enter (Zero), if there is no limit");
            isValid = false;
        }

        if(TextUtils.isEmpty(holder.etRegFee.getText().toString().trim())){
            holder.etRegFee.setError("Registration fee is required. Enter (Zero), if it's a no fee event");
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
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                holder.ivAddEventImage.setImageBitmap(bitmap);
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

    class AddEventViewHolder {
        EditText etEventTitle, etEventDescription, etDate, etAddressLine1, etAddressLine2,
                 etCity, etState, etPinCode, etLandmark, etMaxRegCount, etRegLastDate, etRegFee;
        ImageView ivAddEventImage;
        Button buttonSaveEvent;
        TextView tvError;
        FrameLayout frameLayoutProgressBar;

        AddEventViewHolder() {
            etEventTitle = findViewById(R.id.editText_title_addEvent);
            etEventDescription = findViewById(R.id.editText_description_addEvent);
            etDate = findViewById(R.id.editText_date_addEvent);
            etAddressLine1 = findViewById(R.id.editText_addressLine1_addEvent);
            etAddressLine2 = findViewById(R.id.editText_addressLine2_addEvent);
            etCity = findViewById(R.id.editText_city_addEvent);
            etState = findViewById(R.id.editText_state_addEvent);
            etPinCode = findViewById(R.id.editText_pinCode_addEvent);
            etLandmark = findViewById(R.id.editText_landmark_addEvent);
            etMaxRegCount = findViewById(R.id.editText_maxRegCount_addEvent);
            etRegLastDate = findViewById(R.id.editText_regLastDate_addEvent);
            etRegFee = findViewById(R.id.editText_regFee_addEvent);
            ivAddEventImage = findViewById(R.id.imageView_addImage_addEvent);
            buttonSaveEvent = findViewById(R.id.button_saveEvent);
            tvError = findViewById(R.id.textView_error_addEvent);
            frameLayoutProgressBar = findViewById(R.id.frameLayout_progressBar_addEvent);

            DatePickerFragment fragment1 = new DatePickerFragment();
            fragment1.setEditText(etDate);

            etDate.setOnClickListener(view -> {
                InputMethodManager imm = (InputMethodManager) etDate.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etDate.getWindowToken(), 0);
                fragment1.show(getFragmentManager(), "EventDate");
            });

            DatePickerFragment fragment2 = new DatePickerFragment();
            fragment2.setEditText(etRegLastDate);

            etRegLastDate.setOnClickListener(view -> {
                InputMethodManager imm = (InputMethodManager) etRegLastDate.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etRegLastDate.getWindowToken(), 0);
                fragment2.show(getFragmentManager(), "EventRegLastDate");
            });
        }

        void showError(String errorMessage){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(errorMessage);
        }

        void startLoading() {
            tvError.setVisibility(View.GONE);
            etEventTitle.setEnabled(false);
            etEventDescription.setEnabled(false);
            etDate.setEnabled(false);
            etAddressLine1.setEnabled(false);
            etAddressLine2.setEnabled(false);
            etCity.setEnabled(false);
            etState.setEnabled(false);
            etPinCode.setEnabled(false);
            etLandmark.setEnabled(false);
            etMaxRegCount.setEnabled(false);
            etRegLastDate.setEnabled(false);
            etRegFee.setEnabled(false);
            ivAddEventImage.setEnabled(false);
            buttonSaveEvent.setEnabled(false);
            frameLayoutProgressBar.setVisibility(View.VISIBLE);
        }

        void stopLoading() {
            etEventTitle.setEnabled(true);
            etEventDescription.setEnabled(true);
            etDate.setEnabled(true);
            etAddressLine1.setEnabled(true);
            etAddressLine2.setEnabled(true);
            etCity.setEnabled(true);
            etState.setEnabled(true);
            etPinCode.setEnabled(true);
            etLandmark.setEnabled(true);
            etMaxRegCount.setEnabled(true);
            etRegLastDate.setEnabled(true);
            etRegFee.setEnabled(true);
            ivAddEventImage.setEnabled(true);
            buttonSaveEvent.setEnabled(true);
            frameLayoutProgressBar.setVisibility(View.GONE);
        }
    }
}
