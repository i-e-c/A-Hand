package com.example.ahand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText lPass, lEmail;
    Button btnLogin;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    Info info;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        lPass = findViewById(R.id.lPass);
        lEmail = findViewById(R.id.lEmail);
        btnLogin = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        info = new Info(this);

        progressDialog.setTitle("Sign in");
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        String email = lEmail.getText().toString();
        String password = lPass.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            lEmail.setError("Invalid email");
            lEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            lPass.setError("No password provided");
            lPass.requestFocus();
            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        try {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.dismiss();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finishAffinity();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            info.message("Error occurred", e.getMessage());
                        }
                    });
        } catch (Exception e) {
            info.message("", e.getMessage());
        }

    }

    public void register(View view) {
        startActivity(new Intent(this, Register.class));
        finish();
    }
}