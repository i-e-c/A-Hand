package com.example.ahand;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {
    TextView signIn;
    ImageView usrImage;
    EditText rFullName, rLocation, rPhone, rEmail, rPass, rePass;
    Button btnRegister;
    FirebaseAuth auth;
    Info info;
    DatabaseReference refDB;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Uri imgUri;
    private final static int REQUEST_CODE = 1102;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        signIn = findViewById(R.id.signIn);
        usrImage = findViewById(R.id.usrImage);
        rFullName = findViewById(R.id.rFullName);
        rLocation = findViewById(R.id.rLocation);
        rPhone = findViewById(R.id.rPhone);
        rEmail = findViewById(R.id.rEmail);
        rPass = findViewById(R.id.rPass);
        rePass = findViewById(R.id.rePass);
        btnRegister =findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();
        info = new Info(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        usrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void validation() {
        String fullName = rFullName.getText().toString();
        String location = rLocation.getText().toString();
        String phone = rPhone.getText().toString();
        String email = rEmail.getText().toString();
        String pass = rPass.getText().toString();
        String repPass = rePass.getText().toString();

        if (imgUri == null) {
            info.message("", "Image is required");
            return;
        }
        if (TextUtils.isEmpty(fullName)) {
            rFullName.setError("Name is required");
            rFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(location)) {
            rLocation.setError("Location is required");
            rLocation.requestFocus();
            return;
        }if (TextUtils.isEmpty(phone)) {
            rPhone.setError("Phone is required");
            rPhone.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            rEmail.setError("Please type a valid email");
            rEmail.requestFocus();
        }
        if (pass.length() < 6) {
            rPass.setError("Password should be at least six characters");
            rPass.requestFocus();
            return;
        }
        if (!repPass.equals(pass)) {
            rePass.setError("Password not matched");
            rePass.requestFocus();
        }

        progressDialog.setTitle("Sign up");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        try {
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            save();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            info.message("", e.getMessage());
                        }
                    });
        } catch (Exception e) {
            info.message("", e.getMessage());
        }
    }

    private void newUser() {

    }

    private void save() {
        progressDialog.setTitle("Sign up");
        progressDialog.setMessage("Saving image...");
        progressDialog.show();
        String file = "userImages/" + "user" + auth.getUid();
        storageReference = FirebaseStorage.getInstance().getReference(file);

        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveAll(uri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        info.message("", e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        info.message("", e.getMessage());
                    }
                });
    }

    private void saveAll(Uri uri) {
        String fullName = rFullName.getText().toString();
        String location = rLocation.getText().toString();
        String phone = rPhone.getText().toString();
        String email = rEmail.getText().toString();
        progressDialog.setTitle("Saving all data");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        refDB = FirebaseDatabase.getInstance().getReference("UserData");
        User user = new User(uri.toString(), fullName, location, phone, email);
        refDB.child(auth.getUid()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        info.message("", e.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imgUri = data.getData();
            usrImage.setImageURI(imgUri);
        }
    }
}