package br.com.silver.dybapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = HomeFragment.newInstance();
                    openFragment(fragment,"Home");
                    return true;
                case R.id.navigation_scanner:
                    fragment = ScannerFragment.newInstance();
                    openFragment(fragment, "Scanner");
                    return true;
                case R.id.navigation_history:
                    fragment = HistoryFragment.newInstance();
                    openFragment(fragment, "History");
                    return true;
                case R.id.navigation_settings:
                    openSettings();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment homeFragment = HomeFragment.newInstance();
        openFragment(homeFragment, null);

        checkGps();
    }

    private void openFragment(Fragment fragment, String title) {
        if(title != null)
            getSupportActionBar().setTitle(title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void checkGps() {
        int accessFine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarse =  ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int perm = PackageManager.PERMISSION_GRANTED;

        if (accessFine != perm && accessCoarse != perm) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    1
            );
        }
    }

    private void openSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

}
