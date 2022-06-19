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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText emailRegisterInp, passwordRegisterInp, nameInp;
    Button registerUserBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailRegisterInp = findViewById(R.id.emailRegisterInp);
        passwordRegisterInp = findViewById(R.id.passwordRegisterInp);
        nameInp = findViewById(R.id.nameInp);
        registerUserBtn = findViewById(R.id.registerUserBtn);

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailRegisterInp.getText().toString().trim();
                String password = passwordRegisterInp.getText().toString().trim();
                String name = nameInp.getText().toString().trim();

                if (name.isEmpty()) {
                    nameInp.setError("Name is required");
                    nameInp.requestFocus();
                }



                if (email.isEmpty()) {
                    emailRegisterInp.setError("Name is required");
                    emailRegisterInp.requestFocus();
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailRegisterInp.setError("Please provide valid email");
                    emailRegisterInp.requestFocus();
                }

                if (password.isEmpty()) {
                    passwordRegisterInp.setError("Password is required");
                    passwordRegisterInp.requestFocus();
                }

                if (password.length() < 6) {
                    passwordRegisterInp.setError("Password needs to be at least 6 characters");
                    passwordRegisterInp.requestFocus();
                }

                else {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        User user = new User(name, email);

                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Failed to register. Try again", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register. Try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });


    }
}