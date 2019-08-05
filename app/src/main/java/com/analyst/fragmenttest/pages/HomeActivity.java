package com.analyst.fragmenttest.pages;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.analyst.fragmenttest.Database.DatabaseHelper;
import com.analyst.fragmenttest.pages.fragments.AddTicketFragment;
import com.analyst.fragmenttest.pages.fragments.DiagramCircleFragment;
import com.analyst.fragmenttest.pages.fragments.GraphFragment;
import com.analyst.fragmenttest.pages.fragments.ListFragment;
import com.analyst.fragmenttest.pages.fragments.SettingsFragment;
import com.analyst.fragmenttest.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String TAG = HomeActivity.class.getSimpleName();
    private DatabaseHelper db;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        Log.e(HomeActivity.class.getSimpleName()+": ", "Started");
        db = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        //starting with Client_FragmentFindMerchant()
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_homeLayout);
        navigationView = findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Side Menu info
        /*
        View header = navigationView.getHeaderView(0);
        TextView nav_header_currentUserName_tv = (TextView)header.findViewById(R.id.nav_header_currentUserName);
        nav_header_currentUserName_tv.setText(db.userDao().getUser().get(0).getLogin());
        */

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            //getSupportActionBar().hide();
            //default fragment when activity is running
            getSupportActionBar().setTitle("My Tickets");
            navigationView.setCheckedItem(R.id.navigation_list);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedSragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_list:
                getSupportActionBar().setTitle("My Tickets");
                navigationView.setCheckedItem(R.id.navigation_list);
                drawer.closeDrawers();
                selectedSragment = new ListFragment();
                break;

            case R.id.navigation_addticket:
                Cursor res = db.getAllSettingsData();
                while (res.moveToNext()) {
                    if (res.getDouble(1) != 0.0) {
                        getSupportActionBar().setTitle(getString(R.string.new_ticket));
                        navigationView.setCheckedItem(R.id.navigation_addticket);
                        drawer.closeDrawers();
                        selectedSragment = new AddTicketFragment();

                    } else {
                        getSupportActionBar().setTitle("Settings");
                        navigationView.setCheckedItem(R.id.navigation_settings);
                        Toast.makeText(this, "You need to set your Start Amount before creating tickets!", Toast.LENGTH_LONG).show();
                        drawer.closeDrawers();
                        selectedSragment = new SettingsFragment();
                    }
                }
                break;

            case R.id.navigation_graph:
                getSupportActionBar().setTitle("My Graph");
                navigationView.setCheckedItem(R.id.navigation_graph);
                drawer.closeDrawers();
                selectedSragment = new GraphFragment();
                break;

            case R.id.navigation_diagram_circle:
                getSupportActionBar().setTitle("My Diagram");
                navigationView.setCheckedItem(R.id.navigation_diagram_circle);
                drawer.closeDrawers();
                selectedSragment = new DiagramCircleFragment();
                break;

            case R.id.navigation_settings:
                getSupportActionBar().setTitle("Settings");
                navigationView.setCheckedItem(R.id.navigation_settings);
                drawer.closeDrawers();
                selectedSragment = new SettingsFragment();
                break;

                /** Button Actions **/
                /*
            case R.id.navigation_delete_data:
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("My data");
                    builder.setTitle("You are about to delete all saved data information, Do you want to proceed ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteEveryThing();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setCancelable(false);
                    builder.show();
                break;
            */
                /*
            case R.id.navigation_conditions:
                break;

            case R.id.navigation_legalMentions:
                break;
                */
        }

        if (selectedSragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedSragment).commit();
        }
        return true;
    }
}