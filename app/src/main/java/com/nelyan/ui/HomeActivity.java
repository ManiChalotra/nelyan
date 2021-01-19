package com.nelyan.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nelyan.R;
import com.nelyan.fragments.MaternalFragmentFragment;
import com.nelyan.fragments.ActivityDetailsFragment;
import com.nelyan.fragments.ActivityFragment;
import com.nelyan.fragments.BabySitterFragment;
import com.nelyan.fragments.ChildCareFragment;
import com.nelyan.fragments.DrawerFragment;
import com.nelyan.fragments.EventFragment;
import com.nelyan.fragments.HomeFragment;
import com.nelyan.fragments.MessageFragment;
import com.nelyan.fragments.NurseFragment;
import com.nelyan.fragments.NurserieFragment;
import com.nelyan.fragments.SectorizationDetailsFragment;
import com.nelyan.fragments.SectorizationListFragment;
import com.nelyan.fragments.TraderPublishFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    Context mContext;
    DrawerLayout drawerLayout;
    BottomNavigationView navigationbar;
    int a=1;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext=this;
        navigationbar=findViewById(R.id.navigationbar);
        loadFragment(new HomeFragment());
        BottomNavigationView navigationbar = findViewById(R.id.navigationbar);
        navigationbar.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
        ActionBar toolbar = getSupportActionBar();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigationbar);
        try {

            try {
                if(getIntent().hasExtra("activity")) {
                    if(getIntent().getStringExtra("activity").equals("map"))
                    {
                        Log.e("vghgv","hhhhhhh");
                        Fragment fragment = null;
                        fragment = new ActivityFragment();
                       // Fragment fragment = new ActivityFragment();
                        Bundle args = new Bundle();
                        args.putString("unamea", "Homeactivity");

                        fragment.setArguments(args);
                        FragmentManager fragmentManager = this.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment);

                        fragmentTransaction.commit();
                      /*  getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, fragment)
                                .commit();*/
                     //   loadFragment( fragment);
                    }
                }

                if(getIntent().hasExtra("activity")) {
                    if(getIntent().getStringExtra("activity").equals("sectrofrag"))
                    {
                        Fragment fragment = null;
                        fragment = new SectorizationDetailsFragment();
                        loadFragment( fragment);
                    }
                }
                if(getIntent().hasExtra("activity")){
                    if(getIntent().getStringExtra("activity").equals("activity"))
                    {
                        Log.e("vghgv","hhhhhhh");
                        Fragment fragment = null;
                        fragment = new ChildCareFragment();
                        loadFragment( fragment);
                    }
                }
                if(getIntent().hasExtra("activity")){
                    if(getIntent().getStringExtra("activity").equals("nurFrag"))
                    {
                        Fragment fragment = null;
                        fragment = new NurseFragment();
                        Bundle args = new Bundle();
                        args.putString("activity", "Nurseriefragment");

                        fragment.setArguments(args);
                        FragmentManager fragmentManager = this.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.commit();
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                } if(getIntent().hasExtra("activity")){
                    if(getIntent().getStringExtra("activity").equals("tarfrag"))
                    {
                        Fragment fragment = null;
                        fragment = new TraderPublishFragment();
                        Bundle args = new Bundle();
                        args.putString("activity", "traderfragment");

                        fragment.setArguments(args);
                        FragmentManager fragmentManager = this.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.commit();
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if(getIntent().hasExtra("activity")){
                    if(getIntent().getStringExtra("activity").equals("mater"))
                    {
                        Fragment fragment = null;
                        fragment = new MaternalFragmentFragment();
                        loadFragment( fragment);
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }if(getIntent().hasExtra("activity")){
                    if(getIntent().getStringExtra("activity").equals("babyfrag"))
                    {
                        Fragment fragment = null;
                        fragment = new BabySitterFragment();
                        loadFragment( fragment);
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }

                if(getIntent().hasExtra("activity")){
                    if(getIntent().getStringExtra("activity").equals("nur"))
                    {
                        Fragment fragment = null;
                        fragment = new NurserieFragment();
                        loadFragment( fragment);
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if(getIntent().hasExtra("activity")){
                    if(getIntent().getStringExtra("activity").equals("seclist"))
                    {
                        Fragment fragment = null;
                        fragment = new SectorizationListFragment();
                        loadFragment( fragment);
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if(getIntent().hasExtra("activity")){
                if(getIntent().getStringExtra("activity").equals("acti"))
                {
                    Fragment fragment = null;
                    fragment = new ActivityDetailsFragment();
                //    loadFragment( fragment);

                    Bundle args = new Bundle();
                    args.putString("activity", "Home");

                    fragment.setArguments(args);
                    FragmentManager fragmentManager = this.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment);
                    fragmentTransaction.commit();
                }}

            }catch (Exception e)
            {
                Fragment fragment = null;
                fragment = new HomeFragment();
                loadFragment( fragment);
            }
        }catch (Exception e)
        {
        }
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home:
              //  drawerLayout.openDrawer(GravityCompat.START);

               fragment = new DrawerFragment();
                break;
            case R.id.msg:
                fragment = new MessageFragment();
                break;
            case R.id.publier:
                Intent i=new Intent(HomeActivity.this,PubilerActivity.class);
                startActivity(i);
                break;

            case R.id.chat:
                Intent a =new Intent(HomeActivity.this,ChatActivity.class);
                startActivity(a);
                break;
              case R.id.event:
                fragment = new EventFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
.addToBackStack("null")
                    .commit();
            return true;
        }
        return false;

    }
    public void call()
    {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void onBackPressed() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (currentFragment.getClass().equals(new HomeFragment().getClass())) {
            if (doubleBackToExitPressedOnce) {

                finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            //  Snackbar.make(login_relative, "", BaseTransientBottomBar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }else if (currentFragment.getClass().equals(new ActivityFragment().getClass())) {
           super.onBackPressed();
        }else if (currentFragment.getClass().equals(new ActivityDetailsFragment().getClass())) {
           super.onBackPressed();
        }else if (currentFragment.getClass().equals(new NurserieFragment().getClass())) {
           super.onBackPressed();
        }else if (currentFragment.getClass().equals(new TraderPublishFragment().getClass())) {
           super.onBackPressed();
        }
        else
        {
            finish();
            super.onBackPressed();
            //navigationbar.getMenu().getItem(0).setChecked(true);
        }
    }

}