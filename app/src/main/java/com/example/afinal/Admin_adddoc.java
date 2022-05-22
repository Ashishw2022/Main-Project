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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Admin_adddoc extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private TextView registerText,registerUser;
    private EditText editTextFullName,editTextEmail,editTextPassword;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_adddoc);
        progressBar=(ProgressBar) findViewById(R.id.progressbar_doc_add);
        mAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        registerUser = (Button) findViewById(R.id.registerUser);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                registerUser();

            }
        });
    }

    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(fullName.isEmpty())
        {
            editTextFullName.setError("Full Name is a required field !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextFullName.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is a required field !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please provide Valid Email !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Password is a required field !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextPassword.setError("Minimum length of password should be 6 characters !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user= mAuth.getCurrentUser();
                Map<String,Object> userInfo=new HashMap<>();
                userInfo.put("Name",editTextFullName.getText().toString());
                userInfo.put("email", editTextEmail.getText().toString());
                Toast.makeText(Admin_adddoc.this, "Doctor Registration Successful.", Toast.LENGTH_SHORT).show();
                DocumentReference df=firestore.collection("Users").document(user.getUid());
                userInfo.put("role","Doctor");
                df.set(userInfo);
                startActivity(new Intent(Admin_adddoc.this, Admin_adddoc.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Admin_adddoc.this, "Doctor Registration Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}