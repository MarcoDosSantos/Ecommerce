package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.drawable.CircularProgressDrawable;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText registerNameET, registerPhoneET, registerEmailET, registerAddressET, registerPasswordET;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register_btn);
        registerNameET = findViewById(R.id.register_input_name_et);
        registerPhoneET = findViewById(R.id.register_phone_number_et);
        registerEmailET = findViewById(R.id.register_email_et);
        registerAddressET = findViewById(R.id.register_address_et);
        registerPasswordET = findViewById(R.id.register_enter_password_et);

        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String userName = registerNameET.getText().toString();
        String userPhone = registerPhoneET.getText().toString();
        String userEmail = registerEmailET.getText().toString();
        String userAddress = registerAddressET.getText().toString();
        String userPassword = registerPasswordET.getText().toString();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, RegisterActivity.this.getString(R.string.must_input_name), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userPhone)){
            Toast.makeText(this, RegisterActivity.this.getString(R.string.must_input_phone_number), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, RegisterActivity.this.getString(R.string.must_input_email), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userAddress)){
            Toast.makeText(this, RegisterActivity.this.getString(R.string.must_input_address), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, RegisterActivity.this.getString(R.string.must_input_password), Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle(RegisterActivity.this.getString(R.string.creating_account));
            loadingBar.setMessage(RegisterActivity.this.getString(R.string.please_wait_checking_credentials));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validatePhoneNumber(userName, userPhone, userEmail, userAddress, userPassword);


        }
    }

    private void validatePhoneNumber(String userName, String userPhone, String userEmail, String userAddress, String userPassword) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(userPhone).exists())){
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name", userName);
                    userDataMap.put("phone", userPhone);
                    userDataMap.put("email", userEmail);
                    userDataMap.put("address", userAddress);
                    userDataMap.put("password", userPassword);

                    rootRef.child("Users").child(userPhone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, RegisterActivity.this.getString(R.string.account_successfully_created), Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);

                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, RegisterActivity.this.getString(R.string.please_try_later), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                } else {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getString(R.string.account_already_exists), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}