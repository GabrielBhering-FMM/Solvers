package com.example.solvers.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.solvers.R;
import com.example.solvers.views.fragments.AccountFragment;
import com.example.solvers.views.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

//    Button logout;
    BottomNavigationView bNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bNavView = findViewById(R.id.bottomNavigationView);
        bNavView.setOnNavigationItemSelectedListener(this);

        Fragment homeFragment = HomeFragment.newInstance();
        openFragment(homeFragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                Fragment homeFragment = HomeFragment.newInstance();
                openFragment(homeFragment);
                break;

            case R.id.navigation_account:
                Fragment accountFragment = AccountFragment.newInstance();
                openFragment(accountFragment);
                break;

            default:
                break;
        }

        return true;
    }
}
