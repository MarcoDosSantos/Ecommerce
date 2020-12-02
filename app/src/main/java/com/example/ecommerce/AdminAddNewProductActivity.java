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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String categoryName, downloadImageUrl, productUniqueKey;
    private ImageView inputProductImage;
    private Button btnAddNewProduct;
    private EditText et_input_product_name, et_input_product_description, et_input_product_price;
    private static final int GALLERY_PICK = 1;
    private Uri imageUri;
    private StorageReference productImageRef;
    private DatabaseReference producstReference;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get("Category").toString();
        productImageRef = FirebaseStorage.getInstance().getReference().child("ProductImages");
        producstReference = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);

        et_input_product_name = findViewById(R.id.et_input_product_name);
        et_input_product_description = findViewById(R.id.et_input_product_description);
        et_input_product_price = findViewById(R.id.et_input_product_price);
        inputProductImage = findViewById(R.id.iv_select_product_image);
        btnAddNewProduct = findViewById(R.id.btn_add_new_product);

        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateProductData();
            }
        });

    }

    private void validateProductData() {

        String productName = et_input_product_name.getText().toString();
        String productDescription = et_input_product_description.getText().toString();
        String productPrice = et_input_product_price.getText().toString();

        if(imageUri == null){
            Toast.makeText(AdminAddNewProductActivity.this, AdminAddNewProductActivity.this.getString(R.string.must_input_choose_product_image), Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(productName)){
            Toast.makeText(AdminAddNewProductActivity.this, AdminAddNewProductActivity.this.getString(R.string.must_input_product_name), Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(productDescription)){
            Toast.makeText(AdminAddNewProductActivity.this, AdminAddNewProductActivity.this.getString(R.string.must_input_product_description), Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(productPrice)){
            Toast.makeText(AdminAddNewProductActivity.this, AdminAddNewProductActivity.this.getString(R.string.must_input_product_price), Toast.LENGTH_SHORT).show();
        } else {

            storeProductInformation();

        }
    }

    private void storeProductInformation() {
        loadingBar.setTitle(AdminAddNewProductActivity.this.getString(R.string.adding_product));
        loadingBar.setMessage(AdminAddNewProductActivity.this.getString(R.string.please_wait_adding_product));
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat currentDate = new SimpleDateFormat();
        productUniqueKey = String.valueOf(calendar.getTimeInMillis());

        StorageReference filepath = productImageRef.child(imageUri.getLastPathSegment() + productUniqueKey + ".jpg");
        final UploadTask uploadTask = filepath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, AdminAddNewProductActivity.this.getString(R.string.image_uploaded_succsessfully), Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, AdminAddNewProductActivity.this.getString(R.string.product_image_url_retrieved_succsessfully), Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }

                    }
                });
            }
        });


    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productUniqueKey);
        productMap.put("name", et_input_product_name.getText().toString());
        productMap.put("description", et_input_product_description.getText().toString());
        productMap.put("price", et_input_product_price.getText().toString());
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);

        producstReference.child(productUniqueKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, AdminAddNewProductActivity.this.getString(R.string.product_added_succsessfully), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);
                } else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);
        }
    }
}