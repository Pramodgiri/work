package com.sourcey.materiallogindemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AccountFragment()).commit();
            nv.setCheckedItem(R.id.account);
        }
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AccountFragment()).commit();
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    case R.id.mycart:
                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();break;
                    case R.id.logout:
                        new LogOutActivity().afterLogOutActivity(MainActivity.this);break;
                    default:
                        return true;
                }
                dl.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface df,int i){
                        MainActivity.super.onBackPressed();
                    }
                })

                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface df,int i){
                        df.cancel();
                    }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}