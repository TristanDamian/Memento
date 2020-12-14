package com.example.memento;

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
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail;
    private Button resetPassword;
    private ProgressBar progressBar;
    private TextView banner;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        editEmail = (EditText)findViewById(R.id.email);

        resetPassword = (Button)findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(this);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        banner = (TextView)findViewById(R.id.banner);
        banner.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.banner :
                startActivity(new Intent(this,ConnexionActivity.class));
                break;

            case R.id.resetPassword:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        String email = editEmail.getText().toString().trim();

        if(email.isEmpty())
        {
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editEmail.setError("Please provide valid email");
            editEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Check your email to reset your password", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this,"Something wrong happened, try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}