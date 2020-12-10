package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ecommerce.R.string.please_wait_updating_profile;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView settings_profile_image;
    private EditText et_settings_phone_number, et_settings_full_name, et_settings_address, et_settings_email;
    private TextView txt_close_settings, txt_update_account_settings, txt_profile_image_change;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureRef;
    private StorageTask uploadTask;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings_profile_image = findViewById(R.id.settings_profile_image);
        et_settings_phone_number = findViewById(R.id.et_settings_phone_number);
        et_settings_full_name = findViewById(R.id.et_settings_full_name);
        et_settings_address = findViewById(R.id.et_settings_address);
        et_settings_email = findViewById(R.id.et_settings_email);
        txt_close_settings = findViewById(R.id.txt_close_settings);
        txt_update_account_settings = findViewById(R.id.txt_update_account_settings);
        txt_profile_image_change = findViewById(R.id.txt_profile_image_change);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        userInfoDisplay(settings_profile_image, et_settings_phone_number, et_settings_full_name, et_settings_address, et_settings_email);
        txt_close_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_update_account_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        txt_profile_image_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", et_settings_full_name.getText().toString());
        userMap.put("phone", et_settings_phone_number.getText().toString());
        userMap.put("address", et_settings_address.getText().toString());
        userMap.put("email", et_settings_email.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, SettingsActivity.this.getString(R.string.profile_info_updated_succsessfully), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            settings_profile_image.setImageURI(imageUri);
        } else {
            Toast.makeText(this, SettingsActivity.this.getString(R.string.error_try_again), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(et_settings_full_name.getText().toString())){
            Toast.makeText(this, SettingsActivity.this.getString(R.string.must_input_name), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_settings_phone_number.getText().toString())){
            Toast.makeText(this, SettingsActivity.this.getString(R.string.must_input_phone_number), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_settings_email.getText().toString())){
            Toast.makeText(this, SettingsActivity.this.getString(R.string.must_input_email), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_settings_address.getText().toString())){
            Toast.makeText(this, SettingsActivity.this.getString(R.string.must_input_address), Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(SettingsActivity.this.getString(R.string.updating_profile));
        progressDialog.setMessage(SettingsActivity.this.getString(R.string.please_wait_updating_profile));
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull @NotNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", et_settings_full_name.getText().toString());
                        userMap.put("phone", et_settings_phone_number.getText().toString());
                        userMap.put("address", et_settings_address.getText().toString());
                        userMap.put("email", et_settings_email.getText().toString());
                        userMap.put("image", myUrl);

                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, SettingsActivity.this.getString(R.string.profile_info_updated_succsessfully), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, SettingsActivity.this.getString(R.string.error_try_again), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else {
            Toast.makeText(this, this.getString(R.string.image_not_selected), Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView settings_profile_image, EditText et_settings_phone_number, EditText et_settings_full_name, EditText et_settings_address, EditText et_settings_email) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if (snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(settings_profile_image);
                        et_settings_full_name.setText(name);
                        et_settings_phone_number.setText(phone);
                        et_settings_email.setText(email);
                        et_settings_address.setText(address);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}