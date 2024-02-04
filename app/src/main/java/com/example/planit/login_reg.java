package com.example.planit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login_reg extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText editText1,editText2;
    Button loginButton,button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        editText1=findViewById(R.id.editText1);
        editText2=findViewById(R.id.editText2);

        loginButton=findViewById(R.id.login1);
        button5=findViewById(R.id.login2);
        fAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=editText1.getText().toString();
                String password= editText2.getText().toString();

                if(TextUtils.isEmpty(email)){
                    editText1.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    editText2.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    editText2.setError("Password Must be >= 6 Characters");
                    return;
                }

                // authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login_reg.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),community.class));
                        }else {
                            Toast.makeText(login_reg.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                          progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login_reg.this, signup_reg.class);
                startActivity(intent);
            }
        });
    }
}