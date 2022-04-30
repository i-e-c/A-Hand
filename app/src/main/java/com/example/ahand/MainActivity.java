package com.example.ahand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button fServe;
    Info info;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fServe = findViewById(R.id.fServe);
        info = new Info(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        fServe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ServiceActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mnuLogin = menu.findItem(R.id.mnuLogin);
        MenuItem mnuLogout = menu.findItem(R.id.mnuLogout);
        try {
            if (user != null){
                mnuLogin.setVisible(false);
                mnuLogout.setVisible(true);
            } else {
                mnuLogin.setVisible(true);
                mnuLogout.setVisible(false);
            }
        } catch (Exception e) {
            info.message("Error occurred", e.getMessage());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuService:
                startActivity(new Intent(this, ServiceActivity.class));
                break;

            case R.id.mnuLogin:
                startActivity(new Intent(this, Login.class));
                break;

            case R.id.mnuLogout:
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}