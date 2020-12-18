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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private TextView banner;
    private Button registerUser;
    private EditText editFullName, editAge, editEmail, editPassword;
    private ProgressBar progressBar;

    private static final String fileName = "UID.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editFullName = (EditText) findViewById(R.id.fullName);
        editAge = (EditText) findViewById(R.id.age);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.banner :
                startActivity(new Intent(this,ConnexionActivity.class));
                break;

            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String fullName = editFullName.getText().toString().trim();
        final String age = editAge.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String UID = mAuth.getUid();

        //Vérification des champs
        if(fullName.isEmpty())
        {
            editFullName.setError("Full name is required");
            editFullName.requestFocus();
            return;
        }

        if(age.isEmpty())
        {
            editAge.setError("Age is required");
            editAge.requestFocus();
            return;
        }

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
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            editPassword.setError("Minimum password should be 6 characters");
            editPassword.requestFocus();
            return;
        }

        //Enregistrement
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName,age,email, UID);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        //Ecriture du UID dans un fichier
                                        FileOutputStream fos = null;
                                        try {
                                            fos = openFileOutput(fileName,MODE_PRIVATE);
                                            fos.write(FirebaseAuth.getInstance().getCurrentUser().getUid().getBytes());
                                            Toast.makeText(RegisterActivity.this,"saved to " +getFilesDir() + "/"+fileName, Toast.LENGTH_LONG).show();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }finally{
                                            if(fos != null){
                                                try {
                                                    fos.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        try {
                                            AlarmDatabase manager=new AlarmDatabase();
                                            manager.insertUser(user);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        startActivity(new Intent(RegisterActivity.this,ConnexionActivity.class));
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Failed to register, try again", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this,"Failed to register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}