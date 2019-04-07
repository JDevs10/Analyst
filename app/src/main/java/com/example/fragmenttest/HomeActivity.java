package com.example.fragmenttest;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.Fragments.DiagramCircleFragment;
import com.example.fragmenttest.Fragments.GraphFragment;
import com.example.fragmenttest.Fragments.ListFragment;
import com.example.fragmenttest.Fragments.SettingsFragment;

public class HomeActivity extends AppCompatActivity {

    private String TAG = HomeActivity.class.getSimpleName();
    private DatabaseHelper db;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedSragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_list:
                    selectedSragment = new ListFragment();
                    break;
                case R.id.navigation_graph:
                    selectedSragment = new GraphFragment();
                    break;
                case R.id.navigation_diagram_circle:
                    selectedSragment = new DiagramCircleFragment();
                    break;
                case R.id.navigation_settings:
                    selectedSragment = new SettingsFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedSragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        Log.e(HomeActivity.class.getSimpleName()+": ", "Started");
        db = new DatabaseHelper(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListFragment()).commit();
    }


}
