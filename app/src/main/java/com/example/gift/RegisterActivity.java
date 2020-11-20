package com.example.gift;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText emailTxt, passwordTxt;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.newEmailText);
        passwordTxt = findViewById(R.id.newPasswordText);
        registerBtn = findViewById(R.id.registerButton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                if(password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "비밀번호는 최소 6글자 이상이어야 합니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "회원가입 성공",
                                    Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            RegisterActivity.this.startActivity(mainIntent);
                        } else {
                            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                Toast.makeText(RegisterActivity.this, "이메일 형식을 확인해주세요.",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "이미 등록된 이메일입니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}