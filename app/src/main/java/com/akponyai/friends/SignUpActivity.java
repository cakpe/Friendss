package com.akponyai.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextUsername, editTextPassword;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername=(EditText) findViewById(R.id.usernameText);
        editTextPassword=(EditText) findViewById(R.id.passwordText);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signupButton).setOnClickListener(this);
        findViewById(R.id.textViewlogin).setOnClickListener(this);
    }

    private void registerUser(){
        String username=editTextUsername.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();


        if(username.isEmpty()){
            editTextUsername.setError("Email is required");
            editTextUsername.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            editTextUsername.setError("Please enter a valid email");
            editTextUsername.requestFocus();
            return;
        }

        if(password.length()<3){
            editTextPassword.setError("Minimum length of password should be 3");
            editTextPassword.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User Registeration Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.signupButton:
                registerUser();
                break;



            case R.id.textViewlogin:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }
}
