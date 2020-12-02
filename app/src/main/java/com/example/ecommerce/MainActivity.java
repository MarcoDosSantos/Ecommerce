package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerce.Models.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button welcome_create_account_btn, welcome_join_now_btn;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome_create_account_btn = findViewById(R.id.welcome_create_account_btn);
        welcome_join_now_btn = findViewById(R.id.welcome_join_now_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        welcome_join_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        welcome_create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        
        String phone = Paper.book().read(Prevalent.USER_PHONE_KEY);
        String password = Paper.book().read(Prevalent.USER_PASSWORD_KEY);

        if (phone != "" && password != ""){
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)){
                allowAccess(phone, password);
            }
        }

    }

    private void allowAccess(final String userPhoneKey, final String userPasswordKey) {
        loadingBar.setTitle(MainActivity.this.getString(R.string.login_user));
        loadingBar.setMessage(MainActivity.this.getString(R.string.please_wait_checking_credentials));
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(userPhoneKey).exists()){
                    Users userData = snapshot.child("Users").child(userPhoneKey).getValue(Users.class);
                    if (userData.getPhone().equals(userPhoneKey)){
                        if (userData.getPassword().equals(userPasswordKey)){
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.wrong_password), Toast.LENGTH_LONG).show();

                        }
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.account_does_not_exist), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}