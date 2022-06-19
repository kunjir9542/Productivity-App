package com.example.productivityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText passwordInp, emailInp;
    Button loginBtn, registerBtn ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        passwordInp = findViewById(R.id.passwordInp);
        emailInp = findViewById(R.id.emailInp);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);

                startActivity(intent);


            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInp.getText().toString().trim();
                String password = passwordInp.getText().toString().trim();

                if (email.isEmpty()) {
                    emailInp.setError("Name is required");
                    emailInp.requestFocus();
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInp.setError("Please provide valid email");
                    emailInp.requestFocus();
                }

                if (password.isEmpty()) {
                    passwordInp.setError("Password is required");
                    passwordInp.requestFocus();
                }

                if (password.length() < 6) {
                    passwordInp.setError("Password needs to be at least 6 characters");
                    passwordInp.requestFocus();
                }

                else{
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Failed to log in. Try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


            }
        });


    }
}