package com.example.millenniumhotelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editEmail, editPassword;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;

            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //if Email field is empty throw error message
        if (email.isEmpty()) {
            editEmail.setError("Please enter an email address...");
            editEmail.requestFocus();
            return;
        }

        // Prompt user to insert a valid email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please provide a valid email...");
            editEmail.requestFocus();
            return;
        }

        //if Password field is empty throw error message
        if(password.isEmpty()){
            editPassword.setError("Please enter a password...");
            editPassword.requestFocus();
            return;
        }

        // Prompt the user to insert a password of at least 6 characters
        if (password.length() < 6){
            editPassword.setError("The password needs to be of at least 6 characters...");
            editPassword.requestFocus();
            return;
        }

        // setting Progress Bar to be visible once the Login Button is hit
        progressBar.setVisibility(View.VISIBLE);
        // Firebase authentication
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        //redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    } else {
                        //send user email verification confirmation
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Invalid email", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Login failed! Please try again...", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}