package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.MessageFormat;

public class SecondActivty extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activty);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            drawer =findViewById(R.id.drawer_layout);


        NavigationView navigationview=findViewById(R.id.nav_view);
        navigationview.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragPowerConsumption()).commit();
            navigationview.setCheckedItem(R.id.nav_power_consumption);         }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_power_consumption:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragPowerConsumption()).commit();
                break;
            case R.id.nav_over_consumption:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragOverLoadConsumtpion()).commit();
                break;
            case R.id.nav_save_consumption:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragSavingMethodActivity()).commit();
                break;

    }drawer.closeDrawer(GravityCompat.START);
    return true;
}
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else
{super.onBackPressed();
        }
}
}