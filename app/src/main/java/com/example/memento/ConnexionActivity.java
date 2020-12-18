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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConnexionActivity extends Activity implements View.OnClickListener {

    private TextView register, forgotPassword, offlineMode;
    private EditText editEmail, editPassword;
    private Button login;

    private static final String fileName = "UID.txt";
    private File file;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_connexion);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        offlineMode = (TextView)findViewById(R.id.offlineMode);
        offlineMode.setOnClickListener(this);

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

            case R.id.offlineMode:

                //Lecture du dernier UID connecté dans un fichier
                file = new File(getFilesDir()+"/"+fileName);
                singletonData data = singletonData.getInstance();
                if(file.exists()){
                    data.setOfflineModeEnabled(true);

                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(fileName);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String userID;

                        while((userID = br.readLine()) != null){
                            sb.append(userID);
                        }
                        data.setUserID(sb.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(ConnexionActivity.this,"error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }finally {
                        if(fis != null){
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    startActivity(new Intent(this,MainActivity.class));
                }else{
                    offlineMode.setError("");
                    offlineMode.requestFocus();
                    Toast.makeText(ConnexionActivity.this,"Please register before using offline mode", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void UserLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //Vérification des champs
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

        //Connexion
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    singletonData data = singletonData.getInstance();
                    data.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.setOfflineModeEnabled(false);

                    //Ecriture du UID dans un fichier
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(fileName,MODE_PRIVATE);
                        fos.write(data.getUserID().getBytes());
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
