package com.example.gift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";

    public boolean loginSaved;
    private SharedPreferences loginData;

    private FirebaseAuth auth;
    EditText emailTxt, passwordTxt;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.emailText);
        passwordTxt = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String email = emailTxt.getText().toString();
                final String password = passwordTxt.getText().toString();
                if(email.getBytes().length == 0 || password.getBytes().length == 0){
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인해주세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "email : "+email+"  password : "+password);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "로그인 성공",
                                            Toast.LENGTH_SHORT).show();
                                    saveLogin(email, password);
                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    LoginActivity.this.startActivity(mainIntent);
                                    //updateUI(user);
                                } else {
                                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인해주세요.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });
            }
        });

        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }
    public void saveLogin(String email, String password){
        //sharedpreferences 이용해서 입력한 아이디 비밀번호 저장하기...
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);

        SharedPreferences.Editor editor = loginData.edit();
        editor.putString("email", email);
        editor.putString("password", password);

        editor.commit();
    }
}