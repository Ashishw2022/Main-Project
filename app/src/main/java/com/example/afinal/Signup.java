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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private TextView login;
    private EditText edname,edph,editTextEmailMain, editTextPasswordMain,edcpwd;
    FirebaseAuth firebaseAuth;

    private Button signUp;
    private int flag = 0;
    private String encoded_email;
    private ProgressBar progressBar;
    String eml, name, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUp =findViewById(R.id.regbtn);
        signUp.setOnClickListener(this);


        edname=(EditText) findViewById(R.id.lname);
        editTextEmailMain = (EditText) findViewById(R.id.emailMain);
        edph=(EditText) findViewById(R.id.inputPhone);
        editTextPasswordMain = (EditText) findViewById(R.id.passwordMain);
        edcpwd=(EditText) findViewById(R.id.cpasswordMain);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressbarlogin);


        login = (TextView) findViewById(R.id.olduser);
        login.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.regbtn:
                userRegister();
                break;
            case R.id.olduser:
                startActivity(new Intent(Signup.this, Login.class));
                break;
        }
    }

    private void userRegister() {
        String lname = edname.getText().toString().trim();
        String emailMain = editTextEmailMain.getText().toString().trim();
        String phMain = edph.getText().toString().trim();
        String passwordMain = editTextPasswordMain.getText().toString().trim();
        String cpasswordMain = edcpwd.getText().toString().trim();

        encoded_email = EncodeString(emailMain);
        if (lname.isEmpty()) {
            edname.setError("Name is a required field !");
            edname.requestFocus();
            return;
        }
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
        if (phMain.isEmpty()) {
            edph.setError("Phone Number is a required field !");
            edph.requestFocus();
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
        if (cpasswordMain.isEmpty()) {
            edcpwd.setError("Password is a required field !");
            edcpwd.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(emailMain,passwordMain).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    sendUserData();
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Signup.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this, Login.class));
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Signup.this, "Email ID is already Registered",Toast.LENGTH_SHORT).show();
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
    private void sendUserData(){
        name =edname.getText().toString();
        eml = editTextEmailMain.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = firebaseDatabase.getReference("UserDetails");
        UserProfile userProfile = new UserProfile(name,eml);
        myref.push().setValue(userProfile);
        //  myref.setValue(userProfile);
    }
}