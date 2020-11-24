package com.example.gift;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends TabActivity {
    FirebaseAuth auth;  //로그아웃
    private SharedPreferences loginData;
    boolean loginSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();  //로그아웃

        autoLogin(); //자동로그인 -------------------------------

        //내부 저장소 쓰기/읽기 권한 받기
        getStorageAccessPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        getStorageAccessPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        // ------------------------------

        //자동등록 버튼
        ImageButton automaticButton = (ImageButton) findViewById(R.id.automatic_registration_btn);
        automaticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent automaticIntent = new Intent(MainActivity.this, AutomaticActivity.class);
                MainActivity.this.startActivity(automaticIntent);
            }
        });
        //수동등록 버튼
        ImageButton manualButton = (ImageButton) findViewById(R.id.manual_registration_btn);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manualIntent = new Intent(MainActivity.this, ManualActivity.class);
                MainActivity.this.startActivity(manualIntent);
            }
        });

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
    private void getStorageAccessPermission(String permission){
        Log.e("권한 확인", permission + " : " + String.valueOf(ActivityCompat.checkSelfPermission(this, permission)));
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission}, 1);
            }
        }
    }
    public void logout(View view){
        auth.signOut();
        cancelAutoLogin();
        goLogin();
    }
    public void autoLogin(){
        //로그인데이터에서 내용 읽어오기
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        String email = loginData.getString("email", "");
        String password = loginData.getString("password", "");
        //아무것도 없으면 로그인 액티비티 불러옴
        if(email.equals("") || password.equals("")){
            goLogin();
        }else{
            //있으면 파이어베이스 로그인 메소드 불러와서 자동로그인
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        return;
                    }else{
                        //오류나면 토스트나 다이얼로그 띄우고 로그인액티비티로
                        Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        cancelAutoLogin();
                        goLogin();
                    }
                }
            });
        }
    }
    public void cancelAutoLogin(){
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = loginData.edit();

        editor.putString("email", "");
        editor.putString("password", "");
        editor.putString("saved", "false");

        editor.commit();
    }
    public void goLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);
    }
}