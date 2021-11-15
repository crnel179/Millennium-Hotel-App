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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, register;
    private EditText editFirstName, editLastName, editEmail, editPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        editFirstName = (EditText) findViewById(R.id.firstName);
        editLastName = (EditText) findViewById(R.id.lastName);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                register();
                break;
        }
    }

    private void register() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //if First Name field is empty throw error message
        if(firstName.isEmpty()){
            editFirstName.setError("Please provide a first name...");
            editFirstName.requestFocus();
            return;
        }

        //if Last Name field is empty throw error message
        if(lastName.isEmpty()){
            editLastName.setError("Please enter a last name...");
            editLastName.requestFocus();
            return;
        }

        //if Email field is empty throw error message
        if(email.isEmpty()){
            editEmail.setError("Please enter an email address...");
            editEmail.requestFocus();
            return;
        }

        // Prompt user to insert a valid email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
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

        // setting Progress Bar to be visible once the Register Button is hit
        progressBar.setVisibility(View.VISIBLE);
        // Firebase authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(firstName, lastName, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Registration succsessful!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        // after successful registration user is redirected to login
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration failed! Please try again...", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        // in case the registration fails user is prompted to retry
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed! Please try again...", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            // in case the registration fails user is prompted to retry
                        }
                    }
                });
    }
}