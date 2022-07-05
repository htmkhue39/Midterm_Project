package com.example.midterm_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailEditText;
    ImageView back_bt;
    private Button reset_bt;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText=(EditText) findViewById(R.id.edit_email);
        reset_bt = findViewById(R.id.reset_pass);
        progressBar = findViewById(R.id.progressBar);
        back_bt = findViewById(R.id.back2);
        auth = FirebaseAuth.getInstance();

        reset_bt.setOnClickListener(view -> resetPassword());
        back_bt.setOnClickListener(view -> finish());
    }

    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()) {
            emailEditText.setError("Email is requires!");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password!",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(ForgotPasswordActivity.this, "Try again! Something wrong happened!",Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.INVISIBLE);
        });
    }
}