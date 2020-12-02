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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText login_phone_number_et, login_passwordr_et;
    private Button login_btn;
    private CheckBox remember_me_chkb;
    private TextView admin_panel_link, not_admin_panel_link, forgot_password_tv;
    private ProgressDialog loadingBar;
    private String parentDBName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_phone_number_et = findViewById(R.id.login_phone_number_et);
        login_passwordr_et = findViewById(R.id.login_passwordr_et);
        login_btn = findViewById(R.id.login_btn);

        remember_me_chkb = findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        admin_panel_link = findViewById(R.id.admin_panel_link);
        not_admin_panel_link = findViewById(R.id.not_admin_panel_link);
        forgot_password_tv = findViewById(R.id.forgot_password_tv);
        loadingBar = new ProgressDialog(this);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUser();
            }
        });

        admin_panel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setText(LoginActivity.this.getString(R.string.enter_admin));
                admin_panel_link.setVisibility(View.INVISIBLE);
                not_admin_panel_link.setVisibility(View.VISIBLE);
                parentDBName = "Admins";
            }
        });

        not_admin_panel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setText(LoginActivity.this.getString(R.string.login));
                admin_panel_link.setVisibility(View.VISIBLE);
                not_admin_panel_link.setVisibility(View.INVISIBLE);
                parentDBName = "Users";

            }
        });
    }

    private void logUser() {
        String phone = login_phone_number_et.getText().toString();
        String password = login_passwordr_et.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, LoginActivity.this.getString(R.string.must_input_phone_number), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, LoginActivity.this.getString(R.string.must_input_your_password), Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle(LoginActivity.this.getString(R.string.login_user));
            loadingBar.setMessage(LoginActivity.this.getString(R.string.please_wait_checking_credentials));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            
            allowAccessToAccount(phone, password);

        }
    }

    private void allowAccessToAccount(final String phone, final String password) {

        if (remember_me_chkb.isChecked()){
            Paper.book().write(Prevalent.USER_PHONE_KEY, phone);
            Paper.book().write(Prevalent.USER_PASSWORD_KEY, password);
            
        }

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDBName).child(phone).exists()){
                    Users userData = snapshot.child(parentDBName).child(phone).getValue(Users.class);
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            loadingBar.dismiss();

                            if (parentDBName.equals("Admins")){

                                Intent intemt = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intemt);

                            } else if (parentDBName.equals("Users")){

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(intent);

                            }
                            login_phone_number_et.setText("");
                            login_passwordr_et.setText("");

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.wrong_password), Toast.LENGTH_LONG).show();

                        }
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.account_does_not_exist), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}