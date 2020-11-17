package com.example.gift;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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