package com.example.memento;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.ForOverride;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ConnexionActivity extends Activity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText editEmail, editPassword;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.login:
                UserLogin();
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void UserLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

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

        if(password.isEmpty())
        {
            editPassword.setError("Email is required");
            editPassword.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            editPassword.setError("Minimum password should be 6 characters");
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Diriger vers la bonne page pour l'instant c'est main
                    startActivity(new Intent(ConnexionActivity.this,MainActivity.class));
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(ConnexionActivity.this,"Failed to login, please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
