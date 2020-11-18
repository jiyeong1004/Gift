package com.example.gift;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends TabActivity {
    private SharedPreferences loginData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoLogin(); //자동로그인 -------------------------------

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecHome = tabHost.newTabSpec("Home").setIndicator("전체");
        tabSpecHome.setContent(R.id.linear_home);
        tabHost.addTab(tabSpecHome);

        TabHost.TabSpec tabSpecUnuse = tabHost.newTabSpec("Unuse").setIndicator("미사용");
        tabSpecUnuse.setContent(R.id.linear_unuse);
        tabHost.addTab(tabSpecUnuse);

        TabHost.TabSpec tabSpecUse = tabHost.newTabSpec("Use").setIndicator("사용");
        tabSpecUse.setContent(R.id.linear_use);
        tabHost.addTab(tabSpecUse);

        tabHost.setCurrentTab(0);
    }
    public void autoLogin(){
        //로그인데이터에서 내용 읽어오기
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        String email = loginData.getString("email", "");
        String password = loginData.getString("password", "");
        //아무것도 없으면 로그인 액티비티 불러옴
        if(email.equals("") || password.equals("")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
        }else{
            //있으면 파이어베이스 로그인 메소드 불러와서 자동로그인
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        return;
                    }else{
                        //오류나면 토스트나 다이얼로그 띄우고 로그인액티비티로
                        Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}