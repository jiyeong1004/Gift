package com.example.gift;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton manualButton = (ImageButton) findViewById(R.id.manual_registration_btn);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manualIntent = new Intent(MainActivity.this, ManualActivity.class);
                MainActivity.this.startActivity(manualIntent);
            }
        });

        ImageButton automaticButton = (ImageButton) findViewById(R.id.automatic_registration_btn);
        automaticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent automaticIntent = new Intent(MainActivity.this, AutomaticActivity.class);
                MainActivity.this.startActivity(automaticIntent);
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
}