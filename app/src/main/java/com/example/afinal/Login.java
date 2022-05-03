package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView register, forgotPassword;
    private EditText editTextEmailMain, editTextPasswordMain;

    private Button signIn;
    private int flag = 0;
    private String encoded_email;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String emai,pas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        signIn =findViewById(R.id.loginButton);
        signIn.setOnClickListener(this);

        editTextEmailMain = (EditText) findViewById(R.id.emailMain);
        editTextPasswordMain = (EditText) findViewById(R.id.passwordMain);
        progressBar = (ProgressBar) findViewById(R.id.progressbarlogin);

        forgotPassword = (TextView) findViewById(R.id.forgotPasswordText);
        forgotPassword.setOnClickListener(this);
        register = (TextView) findViewById(R.id.newuser);
        register.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.loginButton:
                userLogin();
                break;
            case R.id.newuser:
                startActivity(new Intent(Login.this, Signup.class));
                break;
        }
    }

    private void userLogin() {
        String emailMain = editTextEmailMain.getText().toString().trim();
        String passwordMain = editTextPasswordMain.getText().toString().trim();
        encoded_email = EncodeString(emailMain);
        if (emailMain.isEmpty()) {
            editTextEmailMain.setError("Email is a required field !");
            editTextEmailMain.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailMain).matches()) {
            editTextEmailMain.setError("Please provide Valid Email !");
            editTextEmailMain.requestFocus();
            return;
        }
        if (passwordMain.isEmpty()) {
            editTextPasswordMain.setError("Password is a required field !");
            editTextPasswordMain.requestFocus();
            return;
        }
        if (passwordMain.length() < 6) {
            editTextPasswordMain.setError("Minimum length of password should be 6 characters !");
            editTextPasswordMain.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(emailMain,passwordMain).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);


                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, User.class));
                } else {
                    Toast.makeText(Login.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

        });
    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}